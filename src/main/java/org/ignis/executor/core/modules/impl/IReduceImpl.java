package org.ignis.executor.core.modules.impl;

import mpi.MPIException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IContext;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.api.Pair;
import org.ignis.executor.api.function.IFunction2;
import org.ignis.executor.api.function.IVoidFunction0;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.ithreads.IThreadPool;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IReduceImpl extends Module {

    private static final Logger LOGGER = LogManager.getLogger();

    public IReduceImpl(IExecutorData executorData) {
        super(executorData, LOGGER);
    }

    public void basicReduce(IFunction2 f, IPartition result) {

    }

    public void reduce(IFunction2 f) throws TException {
        IContext context = this.executorData.getContext();
        f.before(context);
        IPartition elemPart = this.executorData.getPartitionTools().newMemoryPartition(1);
        this.basicReduce(f, elemPart);
        this.finalReduce(f, elemPart);
        f.after(context);
    }

    private void finalReduce(IFunction2 f, IPartition elemPart) throws TException {
        IPartitionGroup output = this.executorData.getPartitionTools().newPartitionGroup();
        LOGGER.info("Reduce: gathering elements for an executor");
        this.executorData.getMpi().gather(elemPart, 0);
        if (this.executorData.getMpi().isRoot(0) && elemPart.size() > 0) {
            LOGGER.info("Reduce: final reduce");
            IPartition result = this.executorData.getPartitionTools().newMemoryPartition(1);
            result.writeIterator().write(this.reducePartition(f, elemPart));
            output.add(result);
        }
        this.executorData.setPartitions(output);
    }

    private Object reducePartition(IFunction2 f, IPartition elemPart) throws TException {
        IContext context = this.executorData.getContext();
        IReadIterator reader = elemPart.readIterator();
        Object acum = reader.next();
        while (reader.hasNext()) {
            acum = f.call(acum, reader.next(), context);
        }
        return acum;
    }

    public void treeReduce(IFunction2 f) throws TException {
        IContext context = this.executorData.getContext();
        f.before(context);
        IMemoryPartition elemPart = this.executorData.getPartitionTools().newMemoryPartition(1);
        this.basicReduce(f, elemPart);
        this.finalTreeReduce(f, elemPart);
        f.after(context);
    }

    private void finalTreeReduce(IFunction2 f, IMemoryPartition elemPart) throws TException {
        int executors = this.executorData.getMpi().executors();
        int rank = this.executorData.getMpi().rank();
        IContext context = this.executorData.getContext();
        IPartitionGroup output = this.executorData.getPartitionTools().newPartitionGroup();

        LOGGER.info("Reduce: reducing all elements in the executor");
        if (elemPart.size() > 1) {
            if (IThreadPool.defaultCores == 1) {
                elemPart.getElements().set(0, reducePartition(f, elemPart));
            } else {
                int n = elemPart.size();
                int n2 = n / 2;
                while (n2 > 0) {
                    int finalN = n;
                    IThreadPool.parallel((i) -> elemPart.getElements().set(i, f.call(elemPart.getElements().get(i), elemPart.getElements().get(finalN - i - 1), context)), n2);
                    n = (int) Math.ceil(n / 2.0);
                    n2 = n / 2;
                }
            }
            elemPart.clear();
        }

        LOGGER.info("Reduce: performing a final tree reduce");
        int distance = 1;
        int order = 1;

        while (order < executors) {
            order *= 2;
            if (rank % order == 0) {
                int other = rank + distance;
                distance = order;
                if (other >= executors)
                    continue;
                this.executorData.getMpi().recv(elemPart, other, 0);
                Object result = f.call(elemPart.getElements().get(0), elemPart.getElements().get(1), context);
                elemPart.clear();
                elemPart.writeIterator().write(result);
            } else {
                int other = rank - distance;
                this.executorData.getMpi().send(elemPart, other, 0);
                break;
            }
        }

        if (executorData.getMpi().isRoot(0) && elemPart.size() > 0) {
            IPartition result = executorData.getPartitionTools().newMemoryPartition(1);
            result.writeIterator().write(elemPart.getElements().get(0));
            output.add(result);
        }
    }

    public void zero(IVoidFunction0 f) {
        IContext context = this.executorData.getContext();
        f.before(context);
        this.executorData.setVariable("zero", f.call(context));
        f.after(context);
    }

    public void aggregate(IFunction2 f) throws TException {
        IContext context = this.executorData.getContext();
        f.before(context);
        IPartitionGroup output = this.executorData.getPartitionTools().newPartitionGroup();
        IPartitionGroup input = this.executorData.getAndDeletePartitions();
        IPartition partialReduce = this.executorData.getPartitionTools().newMemoryPartition(1);
        LOGGER.info("Reduce: aggregating " + input.size() + " partitions locally");

        Object acum = this.executorData.getVariable("zero");
        for (IPartition part : input) {
            if (part.size() == 0) {
                continue;
            }
            acum = this.aggregatePartition(f, part, acum);
        }
        input.clear();

        partialReduce.writeIterator().write(acum);
        output.add(partialReduce);
        this.executorData.setPartitions(output);
//        f.after(context);
    }

    private Object aggregatePartition(IFunction2 f, IPartition part, Object acum) {
        IContext context = this.executorData.getContext();
        for (Object item : part)
            acum = f.call(acum, item, context);
        return acum;
    }

    public void fold(IFunction2 f) throws TException {
        IContext context = this.executorData.getContext();
        f.before(context);
        IPartitionGroup input = this.executorData.getAndDeletePartitions();
        IPartition partialReduce = this.executorData.getPartitionTools().newMemoryPartition(1);
        LOGGER.info("Reduce: folding " + input.size() + " partitions locally");
        var acum = this.executorData.getVariable("zero");
        for (IPartition part : input) {
            if (part.size() == 0) {
                continue;
            }
            acum = this.aggregatePartition(f, part, acum);
        }
        input.clear();
        partialReduce.writeIterator().write(acum);
        this.finalReduce(f, partialReduce);
    }

    public void groupByKey(int numPartitions) throws TException, MPIException {
        this.keyHashing(numPartitions);
        this.exchanging();

        IPartitionGroup input = this.executorData.getAndDeletePartitions();
        IPartitionGroup output = this.executorData.getPartitionTools().newPartitionGroup(numPartitions);
        LOGGER.info("Reduce: reducing key elements");

        Map<Object, List<Object>> acum = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            IPartition part = input.get(i);
            for (Object elem : part.getElements()) {
                Pair<Object, Object> pair = (Pair<Object, Object>) elem;
                Object key = pair.getKey();
                if (!acum.containsKey(key)) {
                    acum.put(key, new ArrayList<>());
                }
                acum.get(key).add(pair.getValue());
            }
            IWriteIterator writer = output.get(i).writeIterator();
            for (Object obj : acum.keySet()) {
                writer.write(new Pair<>(obj, acum.get(obj)));
            }
            acum.clear();
        }
        input.clear();
        this.executorData.setPartitions(output);
    }

    public void keyHashing(int numPartitions) throws TException {
        IPartitionGroup input = this.executorData.getAndDeletePartitions();
        IPartitionGroup output = this.executorData.getPartitionTools().newPartitionGroup(numPartitions);
        boolean cache = input.isCache();
        LOGGER.info("Reduce: creating " + input.size() + " new partitions with key hashing");
        List<IWriteIterator> writers = new ArrayList<>();
        for (IPartition part : output) {
            writers.add(part.writeIterator());
        }
        int n = writers.size();
        for (IPartition part : input) {
            for (Object elem : part) {
                writers.get(elem.hashCode() % n).write(elem);
            }
            if (!cache)
                part.clear();
        }
        input.clear();
        this.executorData.setPartitions(output);
    }

    public void exchanging() throws MPIException {
        IPartitionGroup input = this.executorData.getAndDeletePartitions();
        IPartitionGroup output = this.executorData.getPartitionTools().newPartitionGroup();
        int numPartitions = input.size();
        LOGGER.info("Reduce: exchanging " + numPartitions + " partitions");

        this.exchange(input, output);
        this.executorData.setPartitions(output);

    }

}
