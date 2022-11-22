package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.ithreads.IThreadPool;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.ignis.mpi.Mpi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ISortImpl extends Module {

    private static final Logger LOGGER = LogManager.getLogger();

    public ISortImpl(IExecutorData executorData) {
        super(executorData, LOGGER);
    }

    public void sort(boolean ascending, int numPartitions) {

    }

    public void sortImpl(Comparator<Object> cmp, boolean ascending, int numPartitions, boolean localSort) throws Mpi.MpiException, TException, IOException {
        IPartitionGroup input = this.executorData.getPartitionGroup();
        int executors = this.executorData.getMpi().executors();
        if (input.isCache()) {
            if (this.executorData.getPartitionTools().isMemory(input)) {
                input = input.deepCopy();
            } else {
                input = input.shallowCopy();
            }
        }
        /* Sort each partition */
        if (localSort) {
            LOGGER.info("Sort: sorting " + input.size() + " partitions locally");
            this.parallelLocalSort(input, cmp);
        }
        int localPartitions = input.size();
        int totalPartitions = 0;
//        this.executorData.getMpi().nativ().allReduce(localPartitions, totalPartitions, 1, MPI.INT, MPI.SUM);
        if (totalPartitions < 2) {
            executorData.setPartitions(input);
            return;
        }
        if (numPartitions < 0) {
            numPartitions = totalPartitions;
        }

        /* Generates pivots to separate the elements in order */
        double sr = executorData.getPropertyParser().sortSamples();
        int samples;
        if (sr > -1) { // sr > 1 || sr == 0
            samples = (int) sr;
        } else {
            double[] send = {0, 0};
            double[] rcv = new double[2];
            send[0] = input.size();
            for (IPartition part : input) {
                send[1] += part.size();
            }
//            executorData.getMpi().nativ().allReduce(send, rcv, 2, MPI.LONG, MPI.SUM);
            samples = (int) Math.ceil(rcv[1] / rcv[0] * sr);
        }
        samples = Math.max(numPartitions, samples);
        LOGGER.info("Sort: selecting " + samples + " pivots");
        IPartition pivots = selectPivots(input, samples);

        boolean resampling = this.executorData.getPropertyParser().sortResampling();
        if (sr < 1 && resampling && executors > 1 && localSort) {
            LOGGER.info("Sort: -- resampling pivots begin --");
            IPartitionGroup tmp = this.executorData.getPartitionTools().newPartitionGroup(0);
            tmp.add(pivots);
            this.executorData.setPartitions(tmp);
            this.executorData.setPartitions(tmp);
            this.sortImpl(cmp, ascending, numPartitions, false);
            LOGGER.info("Sort: -- resampling pivots end --");
            samples = numPartitions - 1;
            LOGGER.info("Sort: selecting " + samples + " partition pivots");
            pivots = this.parallelSelectPivots(samples);

            LOGGER.info("Sort: collecting pivots");
            this.executorData.getMpi().gather(pivots, 0);
        } else {
            LOGGER.info("Sort: collecting pivots");
            this.executorData.getMpi().gather(pivots, 0);
            if (this.executorData.getMpi().isRoot(0)) {
                IPartitionGroup group = this.executorData.getPartitionTools().newPartitionGroup(0);
                group.add(pivots);
                this.localSort(group, cmp, ascending);
            }
        }


    }

    private void localSort(IPartitionGroup group, Comparator<Object> cmp, boolean ascending) {
        boolean inMemory = this.executorData.getPartitionTools().isMemory(group);

        for (IPartition part : group) {
            if (!inMemory) {
                IPartition newPart = this.executorData.getPartitionTools().newMemoryPartition(part.size());
                part.moveTo(newPart);
                part = newPart;
            }

            List<Object> elems = part.getElements();

//            if(true){
            if (cmp != null) {
                elems.sort(cmp);
            }
//                else{
//                    Collections.sort(elems);
//                }
//            }
        }
    }

    private IPartition parallelSelectPivots(int samples) throws Mpi.MpiException {
        int rank = this.executorData.getMpi().rank();
        int executors = this.executorData.getMpi().executors();
        IMemoryPartition result = this.executorData.getPartitionTools().newMemoryPartition(samples);
        List<IPartition> tmp = this.executorData.getPartitions();
        List<Integer> aux = new ArrayList<>(Collections.nCopies(executors, 0));
        int sz = tmp.parallelStream().mapToInt(IPartition::size).sum();
        int disp = 0, pos, sample;

//        this.executorData.getMpi().nativ().allGather(sz, 1, MPI.INT, aux, 1, MPI.INT);

        sz = 0;
        for (int i = 0; i < executors; i++) {
            sz += aux.get(i);
        }
        for (int i = 0; i < rank; i++) {
            disp += aux.get(i);
        }
        long skip = (sz - samples) / (samples + 1);
        int rem = (sz - samples) % (samples + 1);

        pos = skip + rem > 0 ? 1 : 0;
        for (sample = 0; sample < samples; sample++) {
            if (pos >= disp)
                break;
            if (sample < rem - 1)
                pos += skip + 2;
            else pos += skip + 1;
        }
        pos -= disp;

        IMemoryPartition.IMemoryWriteIterator writer = result.writeIterator();

        for (IPartition part : tmp) {
//            array = executorData.getPartitionTools()
            while (pos < part.size() && sample < samples) {
                writer.write(part.getElements().get(pos));
                if (sample < rem - 1)
                    pos += skip + 2;
                else pos += skip + 1;
                sample += 1;
            }
            pos -= part.size();
        }

        return result;
    }

    private IPartition selectPivots(IPartitionGroup input, long samples) throws TException {
        IPartition pivots = this.executorData.getPartitionTools().newMemoryPartition();
        boolean inMemory = this.executorData.getPartitionTools().isMemory(input);
        IWriteIterator writer = pivots.writeIterator();

        for (IPartition part : input) {
            if (part.size() < samples) {
                part.copyTo(pivots);
                writer = pivots.writeIterator();
                continue;
            }
            int skip = (int) ((part.size() - samples) / (samples + 1));
            int rem = (int) ((part.size() - samples) % (samples + 1));
            if (inMemory) {
                int pos = skip + (rem > 0 ? 1 : 0);
                for (int n = 0; n < samples; n++) {
                    writer.write(part.getElements().get(pos));
                    if (n < rem - 1)
                        pos += skip + 2;
                    else
                        pos += skip + 1;
                }
            } else {
                IReadIterator reader = part.readIterator();
                for (int n = 0; n < samples; n++) {
                    for (int i = 0; i < skip; i++)
                        reader.next();
                    if (n < rem)
                        reader.next();
                    writer.write(reader.next());
                }
            }
        }
        return pivots;
    }

    private void sortPartition(IMemoryPartition part, Comparator<Object> cmp) {
        part.getElements().sort(cmp);
    }

    private void parallelLocalSort(IPartitionGroup group, Comparator<Object> cmp) {
        boolean inMemory = this.executorData.getPartitionTools().isMemory(group);
        IThreadPool.parallel((i) -> {
            IPartition part = group.get(i);
            if (inMemory) {
                sortPartition((IMemoryPartition) part, cmp);
            } else {
                IPartition newPart = this.executorData.getPartitionTools().newMemoryPartition(part.size());
                part.moveTo(newPart);
                group.set(i, newPart);
                sortPartition((IMemoryPartition) part, cmp);
            }
        }, group.size());
    }

}