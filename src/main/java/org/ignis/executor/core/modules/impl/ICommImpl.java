package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.ICommModule;
import org.ignis.rpc.IExecutorException;
import org.ignis.rpc.ISource;

import java.nio.ByteBuffer;
import java.util.List;

// ToDo
public class ICommImpl extends Module implements ICommModule {

    private static final Logger LOGGER = LogManager.getLogger();
    public ICommImpl(IExecutorData executorData) {
        super(executorData, LOGGER);
    }

    @Override
    public String openGroup() throws IExecutorException, TException {
        return null;
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
        return null;
    }

    @Override
    public List<ByteBuffer> getPartitions2(byte protocol, long minPartitions) throws IExecutorException, TException {
        return null;
    }

    @Override
    public void setPartitions(List<ByteBuffer> partitions) throws IExecutorException, TException {

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
