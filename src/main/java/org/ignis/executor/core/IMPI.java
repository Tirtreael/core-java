package org.ignis.executor.core;

import mpi.Comm;
import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TMemoryBuffer;
import org.ignis.executor.api.IContext;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;

import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class IMPI {

    private MPI mpi;
    private IPropertyParser propertyParser;
    private IPartitionTools partitionTools;
    private IContext context;

    public IMPI(IPropertyParser propertyParser, IPartitionTools partitionTools, IContext context) {
        this.propertyParser = propertyParser;
        this.partitionTools = partitionTools;
        this.context = context;
    }

    public void gather(IPartition part, int root) {
        if (this.executors() == 1) {
        } else this.gatherImpl(this.nativ(), part, root, true);
    }

    public void bcast(IPartition partition, int root) throws TException, NotSerializableException {
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
        boolean driver = group.getRank() == 0;
        boolean exec0 = group.getRank() == 1;
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
        group.allReduce(MPI.REPLACE, maxPartition, MPI.INT, MPI.MAX);
        group.bcast(protocol, 1, MPI.BYTE, 0);
        if (maxPartition == 0) {
            return;
        }
        group.allReduce(MPI.REPLACE, storageLength, MPI.INT, MPI.MAX);

        storage.add((byte) (storageLength - storage.size()));
        List<Byte> storageV;
        if (driver)
            storageV = new ArrayList<>(storageLength * group.getSize());
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

    public void send(IPartition partition, Object dest, String tag) {

    }

    public void recvGroup(Intracomm group, IPartition partition, Object source, String tag, MsgOpt opt) {

    }

    public void recv(Intracomm group, IPartition partition, Object source, String tag) {

    }

    public void sendRecv(Object sendP, Object recvP, Object other, String tag) {

    }

    public void barrier() {

    }

    public boolean isRoot(int root) {
        return this.rank() == root;
    }

    public int rank() {
        return this.context.executorId();
    }

    public int executors() {
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

    public void gatherImpl(Comm group, IPartition partition, int root, boolean sameProtocol) {

    }

    public void sendRecv(IPartition part, Object source, Object dest, String tag) {

    }

    public void sendRecvGroup(IPartition part, Object source, Object dest, String tag, MsgOpt opt) {

    }

    public void sendRecvImpl(IPartition part, Object source, Object dest, String tag, boolean sameProtocol) {

    }

    public void exchangeASync(Object input, Object outPut) {

    }

    private class MsgOpt {
        private boolean sameProtocol;
        private boolean sameStorage;

        public MsgOpt(boolean sameProtocol, boolean sameStorage) {
            this.sameProtocol = sameProtocol;
            this.sameStorage = sameStorage;
        }
    }


}
