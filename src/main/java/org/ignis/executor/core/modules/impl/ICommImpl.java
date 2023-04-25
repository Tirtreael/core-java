package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TByteBuffer;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.ICommModule;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.ignis.executor.core.transport.IZlibTransport;
import org.ignis.rpc.IExecutorException;
import org.ignis.rpc.ISource;

import java.io.NotSerializableException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

// ToDo
public class ICommImpl extends Module implements ICommModule {

    private static final Logger LOGGER = LogManager.getLogger();

    public ICommImpl(IExecutorData executorData) {
        super(executorData, LOGGER);
    }

    @Override
    public String openGroup() throws IExecutorException, TException {
        return "";
    }

    @Override
    public void closeGroup() throws IExecutorException, TException {

    }

    @Override
    public void joinToGroup(String id, boolean leader) throws IExecutorException, TException {

    }

    @Override
    public void joinToGroupName(String id, boolean leader, String name) throws IExecutorException, TException {

    }

    @Override
    public boolean hasGroup(String name) throws IExecutorException, TException {
        return false;
    }

    @Override
    public void destroyGroup(String name) throws IExecutorException, TException {

    }

    @Override
    public void destroyGroups() throws IExecutorException, TException {

    }

    @Override
    public byte getProtocol() throws IExecutorException, TException {
        return 0;
    }

    @Override
    public List<ByteBuffer> getPartitions(byte protocol) throws IExecutorException, TException {
        return getPartitions2(protocol, 1);
    }

    @Override
    public List<ByteBuffer> getPartitions2(byte protocol, long minPartitions) throws IExecutorException, TException {
        List<ByteBuffer> partitions = new ArrayList<>();
        IPartitionGroup group = this.executorData.getPartitionGroup();
        int cmp = this.executorData.getPropertyParser().msgCompression();
        boolean nativ = this.getProtocol() == protocol && this.executorData.getPropertyParser().nativeSerialization();
        int capacity = (int) (group.size() > minPartitions ? group.size() : minPartitions);
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect((int) (group.size() > minPartitions ? group.size() : minPartitions));
        try (TByteBuffer buffer = new TByteBuffer(byteBuffer)) {
            if (group.size() > minPartitions) {
                for (IPartition part : group) {
                    buffer.flush();
                    part.write(buffer, cmp, nativ);
                    partitions.add(buffer.getByteBuffer());
                }
            } else if (group.size() == 1 && this.executorData.getPartitionTools().isMemory(group)) {
                IPartition men = group.get(0);
                IZlibTransport zlib = new IZlibTransport(buffer, cmp);
                IObjectProtocol proto = new IObjectProtocol(zlib);
                int partitionElems = (int) (men.size() / minPartitions);
                int remainder = (int) (men.size() % minPartitions);
                int offset = 0;
                for (int p = 0; p < minPartitions; p++) {
                    int size = partitionElems + (p < remainder ? 1 : 0);
                    proto.writeObject(men.getElements().subList(offset, offset + size), nativ, false);
                    offset += size;
                    zlib.flush();
                    zlib.reset();
                    partitions.add(buffer.getByteBuffer());
                    buffer.flush();
                }
            } else if (group.size() > 0) {
                int elements = 0;
                for (IPartition part : group) {
                    elements += part.size();
                }
                IPartition part = new IMemoryPartition(1024 * 1024);
                int partitionElems = (int) (elements / minPartitions);
                int remainder = (int) (elements % minPartitions);
                int i = 0;
                int ew = 0;
                int er = 0;
                IReadIterator it = group.get(0).readIterator();
                for (int p = 0; p < minPartitions; p++) {
                    part.clear();
                    IWriteIterator writer = part.writeIterator();
                    ew = partitionElems;
                    if (p < remainder) ew++;
                    while (ew > 0) {
                        if (er == 0) {
                            er = group.get(i).size();
                            it = group.get(i).readIterator();
                            i++;
                        }
                        int n = Math.min(ew, er);
                        for (int j = 0; j < n; j++)
                            writer.write(it.next());
                        ew -= n;
                        er -= n;
                    }
                    part.write(buffer, cmp, nativ);
                    partitions.add(buffer.getByteBuffer());
                    buffer.flush();
                }
            } else {
                IPartition part = new IMemoryPartition();
                part.write(buffer, cmp, nativ);
                for (int i = 0; i < minPartitions; i++) {
                    partitions.add(buffer.getByteBuffer());
                }
            }
            return partitions;
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPartitions(List<ByteBuffer> partitions) throws IExecutorException, TException {
        IPartitionGroup group = this.executorData.getPartitionTools().newPartitionGroup(partitions.size());
        for (int i = 0; i < partitions.size(); i++) {
            try {
                group.get(i).read(new TByteBuffer(partitions.get(i)));
            } catch (NotSerializableException e) {
                throw new RuntimeException(e);
            }
        }
        this.executorData.setPartitions(group);
    }

    @Override
    public void setPartitions2(List<ByteBuffer> partitions, ISource src) throws IExecutorException, TException {

    }

    @Override
    public void driverGather(String group, ISource src) throws IExecutorException, TException {

    }

    @Override
    public void driverGather0(String group, ISource src) throws IExecutorException, TException {

    }

    @Override
    public void driverScatter(String group, long partitions) throws IExecutorException, TException {

    }

    @Override
    public void driverScatter3(String group, long partitions, ISource src) throws IExecutorException, TException {

    }

    @Override
    public void importData(String group, boolean source, long threads) throws IExecutorException, TException {

    }

    @Override
    public void importData4(String group, boolean source, long threads, ISource src) throws IExecutorException, TException {

    }
}
