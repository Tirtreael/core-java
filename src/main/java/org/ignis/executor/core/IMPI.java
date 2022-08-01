package org.ignis.executor.core;

import mpi.Comm;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TMemoryBuffer;
import org.ignis.executor.api.IContext;
import org.ignis.executor.api.Pair;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.storage.IDiskPartition;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class IMPI {

    private static final Logger LOGGER = LogManager.getLogger();
    private final IPropertyParser propertyParser;
    private final IPartitionTools partitionTools;
    private final IContext context;
    private MPI mpi;

    public IMPI(IPropertyParser propertyParser, IPartitionTools partitionTools, IContext context) {
        this.propertyParser = propertyParser;
        this.partitionTools = partitionTools;
        this.context = context;
    }

    public void gather(IPartition part, int root) throws MPIException, TException, IOException {
        if (this.executors() == 1) {

        } else this.gatherImpl(this.nativ(), part, root, true);
    }

    public void bcast(IPartition partition, int root) throws TException, NotSerializableException, MPIException {
        if (this.executors() == 1) {
        } else if (partition.type().equals(IMemoryPartition.TYPE)) {
//            if(partition ) {
//                sz = this.nativ().bcast(partition.size(), root);
//                if(! this.isRoot(root))
//                    partition.setElements();
//            }
            TMemoryBuffer buffer = new TMemoryBuffer((int) partition.bytes());
            if (this.isRoot(root)) {
                boolean nativ = this.propertyParser.nativeSerialization();
                partition.write(buffer, this.propertyParser.msgCompression(), nativ);
            }
//            var sz = this.nativ().bcast(buffer, root, );
            if (!this.isRoot(root)) {
//                buffer.
            }
//            this.nativ().iBcast(buffer.getBuffer())
            if (!this.isRoot(root)) {
//                buffer
                partition.clear();
                partition.read(buffer);
            }
        }

//        else if(partition.type().equals(IRawMemoryPartition.TYPE)) {
//
//        }
//        else if(partition.type().equals(IDiskPartition.TYPE)) {
//
//        }
    }

    public void driverGather(Intracomm group, IPartitionGroup partitionGroup) throws MPIException {
        boolean driver = group.Rank() == 0;
        boolean exec0 = group.Rank() == 1;
        int maxPartition = 0;
        byte protocol = IObjectProtocol.JAVA_PROTOCOL;
        boolean same_protocol;
        List<Byte> storage = new ArrayList<>();
        int storageLength = 0;

        if (driver) {
            protocol = IObjectProtocol.JAVA_PROTOCOL;
        } else {
            maxPartition = partitionGroup.size();
            if (partitionGroup.get(0) != null) {
//                storage = Arrays.copy partitionGroup.get(0).getType().getBytes(StandardCharsets.UTF_8);
//                storageLength = storage.length;
            }
        }
//        @Todo MPI.REPLACE -> MPI.IN_PLACE
//        group.allReduce(MPI.REPLACE, maxPartition, MPI.INT, MPI.MAX);
//        group.bcast(protocol, 1, MPI.BYTE, 0);
        if (maxPartition == 0) {
            return;
        }
//        group.allReduce(MPI.REPLACE, storageLength, MPI.INT, MPI.MAX);

        storage.add((byte) (storageLength - storage.size()));
        List<Byte> storageV;
        if (driver)
            storageV = new ArrayList<>(storageLength * group.Size());
        else storageV = new ArrayList<>();


    }

    public void driverGather0(Intracomm group, IPartitionGroup partitionGroup) {

    }

    public void driverScatter(Intracomm group, IPartitionGroup partitionGroup, List<IPartition> partitions) {

    }

    public void getMsgOpt(Intracomm group, String partitionType, boolean send, Object other, String tag) {

    }

    public void sendGroup(Intracomm group, IPartition partition, Object dest, String tag, MsgOpt opt) {

    }

    public void send(IPartition partition, int dest, int tag) {

    }

    public void recvGroup(Intracomm group, IPartition partition, Object source, String tag, MsgOpt opt) {

    }

    public void recv(IPartition partition, int source, int tag) {
        sendRecv(partition, source, rank(), tag);
    }

    public void sendRecv(Object sendP, Object recvP, int other, int tag) {

    }

    public void barrier() {

    }

    public boolean isRoot(int root) {
        return this.rank() == root;
    }

    public int rank() {
        return this.context.executorId();
    }

    public int executors() throws MPIException {
        return this.context.executors();
    }

    public Intracomm nativ() {
        return this.context.getMPIGroup();
    }

    public List<Integer> displs(List<Integer> szv) {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        Integer v = list.get(0);
        for (Integer sz : szv) {
            v += sz;
            list.add(sz);
        }
        return list;
    }

    public List<List<Integer>> displs2(List<List<Integer>> szv) {
        List<List<Integer>> list = new ArrayList<>(szv);
        list.add(0, new ArrayList<>());
        List<Integer> v = list.get(0);
        for (List<Integer> sz : szv) {
            v.set(0, v.get(0) + sz.get(0));
            v.set(1, v.get(1) + sz.get(1));
        }
        return list;
    }

    public List<Integer> getList(List<List<Integer>> svz, int c) {
        return svz.stream().map(t -> t.get(c)).collect(Collectors.toList());
    }

    public void gatherImpl(Comm group, IPartition partition, int root, boolean sameProtocol) throws MPIException, IOException, TException {
        int rank = group.Rank();
        int executors = group.Size();
        if (partition.type().equals(IMemoryPartition.TYPE)) {
//            cls = null;
            int sz = partition.size();
            List<Integer> szv = new ArrayList<>();
            List<Integer> displs = new ArrayList<>();
            if (sameProtocol) {
//                cls =
//                if(rank == root){
//
//                }
//                group.b
//                group.gather(sz, 1, MPI.INT, szv, 1, MPI.INT, root);
                if (rank == root) {
                    displs = this.displs(szv);
                    if (root > 0) {
//                        partition.getElements() =
                    }
                    partition.getElements().addAll(Collections.singletonList(BigInteger.valueOf(displs.get(displs.size() - 1) - partition.size()).toByteArray()));
//                    group.gatherv(MPI.REPLACE, 0, MPI.BYTE, partition, this.listToArrayInt(szv), this.listToArrayInt(displs), MPI.BYTE, root);
                } else {
//                    group.gatherv(partition, sz, MPI.BYTE, null, null, null, MPI.BYTE, root);
                }
            } else {
//                IMemoryPartition men = partitionTools.newMemoryPartition(partition);
                TMemoryBuffer buffer = new TMemoryBuffer(4096);
                sz = 0;
                if (rank != root) {
//                    partition.write(buffer, propertyParser.msgCompression());
                    buffer.flush();
                }
//                group.gather(sz, 1, MPI.INT, szv, 1, MPI.INT, root);
                if (rank == root) {
                    displs = this.displs(szv);
                    buffer = new TMemoryBuffer(displs.get(displs.size() - 1));
                }
//                group.gatherv(buffer, sz, MPI.BYTE, buffer, this.listToArrayInt(szv), this.listToArrayInt(displs), MPI.BYTE, root);

                if (rank == root) {
                    IPartition rcv = new IMemoryPartition();
                    for (int i = 0; i < executors; i++) {
                        if (i != rank) {
                            rcv.read(buffer);
                        }
                    }
                    partition.moveTo(rcv);
                }
            }
        }
//        else if(partition.type() == IRawMemoryPartition.TYPE){
//        } else {}
    }

    public void sendRecv(IPartition part, Object source, Object dest, String tag) {

    }

    public void sendRecvGroup(IPartition part, Object source, Object dest, String tag, MsgOpt opt) {

    }

    public void sendRecvImpl(IPartition part, Object source, Object dest, String tag, boolean sameProtocol) {

    }

    public void exchangeSync(IPartitionGroup input, IPartitionGroup output) throws MPIException {
        int executors = this.executors();
        if (executors == 1 || input.size() < 2) {
            for (IPartition part : input) {
                part.fit();
                output.add(part);
            }
            return;
        }

        int numPartitions = input.size();
        int block = numPartitions / executors;
        int remainder = numPartitions % executors;
        List<Pair<Integer, Integer>> partsTargets = new ArrayList<>(Collections.nCopies((block + 1) * executors, null));

        int p = 0;
        for (int i = 0; i < executors; i++) {
            for (int j = 0; i < block; i++) {
                partsTargets.set(j * executors + i, new Pair<>(p + j, i));
            }
            p += block;
            if (i < remainder) {
                partsTargets.set(block * executors + i, new Pair<>(p, i));
                p++;
            }
        }
        partsTargets.removeIf(Objects::isNull);
        int cores = this.propertyParser.cores();
        String pType = input.get(0).getType();
        String optType = pType;
        Class<?> cls = null;
        Intracomm comm = this.nativ();
        List<?> wins = new ArrayList<>();
        boolean onlyShared = false;

        if (this.propertyParser.transportCores() > 0 && cores > 1 && pType != IDiskPartition.TYPE) {
            LOGGER.info("Local exchange init");
            class IMPIBuffer extends TMemoryBuffer {
                public IMPIBuffer(int size) {
                    super(size);
                }

                public void realloc(int size) {
//                    super();
//                    aux = TMemoryBuffer
                }
            }

            if (pType == IMemoryPartition.TYPE) {
//                cls = input.get(0).IMemoryPartitionCls
//                if()
            }

        }


    }

    public void exchangeASync(IPartitionGroup input, IPartitionGroup output) throws MPIException {
        int executors = executors();
        int rank = rank();
        int numPartitions = input.size();
        int block = numPartitions / executors;
        int remainder = numPartitions % executors;
        List<Pair<Integer, Integer>> ranges = new ArrayList<>();
        List<Integer> queue = new ArrayList<>();

        int init, end;
        for (int i = 0; i < executors; i++) {
            if (i < remainder) {
                init = (block + 1) * i;
                end = init + block + 1;
            } else {
                init = (block + 1) * remainder + block * (i - remainder);
                end = init + block + 1;
            }
            ranges.add(new Pair<>(init, end));
        }
        int m = (executors % 2 == 0) ? executors : executors + 1;
        int id = 0;
        int id2 = m * m - 2;
        for (int i = 0; i < m - 1; i++) {
            if (rank == id % (m - 1))
                queue.add(m - 1);
            if (rank == m - 1)
                queue.add(id % (m - 1));
            id++;
            for (int j = 1; j < m / 2; j++) {
                if (rank == id % (m - 1))
                    queue.add(id2 % (m - 1));
                if (rank == id2 % (m - 1))
                    queue.add(id % (m - 1));
                id++;
                id2++;
            }
        }
        List<Boolean> ignores = new ArrayList<>(Collections.nCopies(queue.size(), false));
        for (int i = 0; i < queue.size(); i++) {
            int other = queue.get(i);
            boolean ignore = true;
            if (other == executors)
                continue;
            for (int j = ranges.get(other).getKey(); j < ranges.get(other).getValue(); j++) {
                ignore = ignore && input.get(j).isEmpty();
            }
            boolean ignoreOther = false;
//            nativ().sendRecv(ignore, 1, MPI.BOOLEAN, other, 0, ignoreOther, 1, MPI.BOOLEAN, other, 0);

            if (ignore && ignoreOther) {
                ignores.set(i, true);
                input.subList(ranges.get(other).getKey(), ranges.get(other).getValue()).clear();
            }
        }

        for (int i = 0; i < queue.size(); i++) {
            int other = queue.get(i);
            if (ignores.get(i) || other == executors) {
                continue;
            }
            int otherPart = ranges.get(other).getKey();
            int otherEnd = ranges.get(other).getValue();
            int mePart = ranges.get(rank).getKey();
            int meEnd = ranges.get(rank).getValue();
            int its = Math.max(otherEnd - otherPart, meEnd - mePart);

            for (int j = 0; j < its; i++) {
                if (otherPart >= otherEnd || mePart >= meEnd) {
                    if (otherPart >= otherEnd) {
                        recv(input.get(mePart), other, 0);
                    } else if (mePart >= meEnd) {
                        send(input.get(otherPart), other, 0);
                    } else continue;
                } else {
                    sendRecv(input.get(otherPart), input.get(mePart), other, 0);
                }
                input.remove(otherPart);
                otherPart++;
                mePart++;
            }
        }

        for (IPartition partition : input) {
            partition.fit();
            output.add(partition);
        }
        input.clear();

    }

    public int[] listToArrayInt(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++)
            array[i] = list.get(i);
        return array;
    }

    public byte[] objToBytes(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        return bos.toByteArray();
    }

    private class MsgOpt {
        private final boolean sameProtocol;
        private final boolean sameStorage;

        public MsgOpt(boolean sameProtocol, boolean sameStorage) {
            this.sameProtocol = sameProtocol;
            this.sameStorage = sameStorage;
        }
    }

}
