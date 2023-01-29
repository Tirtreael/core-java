package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.ignis.mpi.Mpi;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

class ModuleTest {

    private static final Logger LOGGER = LogManager.getLogger();
    private final IExecutorData iExecutorData = new IExecutorData();
    private final String library = "build/libs/core-jara-testFunctions.jar";

//    private final GeneralModule generalModule = new GeneralModule(iExecutorData);


    void loadToPartitions(List<Object> elems, int partitions) throws TException {
        IPartitionGroup group = this.getExecutorData().getPartitionTools().newPartitionGroup(partitions);
        this.getExecutorData().setPartitions(group);
        int partitionSize = (int) Math.ceil((double) elems.size() / group.size());
        for (int p = 0; p < group.size(); p++) {
            IWriteIterator writeIterator = group.get(p).writeIterator();
            int i = partitionSize * p;
            while (i < partitionSize * (p + 1) && i < elems.size()) {
                writeIterator.write(elems.get(i));
                i += 1;
            }
        }
    }

    List<Object> getFromPartitions() throws TException {
        List<Object> elems = new ArrayList<>();
        IPartitionGroup group = this.getExecutorData().getPartitionGroup();
        for (IPartition objects : group) {
            IReadIterator readIterator = objects.readIterator();
            while (readIterator.hasNext()) {
                elems.add(readIterator.next());
            }
        }
        return elems;
    }

    public ModuleTest() {
        Properties props = this.iExecutorData.getContext().props().getProperties();
        props.put("ignis.transport.compression", "0");
        props.put("ignis.partition.compression", "0");
        props.put("ignis.partition.serialization", "native");
        props.put("ignis.executor.directory", "./");
        props.put("ignis.executor.cores", "1");
        props.put("ignis.transport.cores", "0");
        props.put("ignis.modules.load.type", "false");
        props.put("ignis.modules.exchange.type", "sync");
    }

    IExecutorData getExecutorData() {
        return iExecutorData;
    }

    @Test
    void packException() {
    }

    @Test
    void useSource() {
    }

    @Test
    void resizeMemoryPartition() {
    }

    public List<Object> rankVector(List<Object> elems) throws Mpi.MpiException {
        int n = elems.size() / this.getExecutorData().getContext().executors();
        int rank = this.getExecutorData().getContext().executorId();
        return elems.subList(n * rank, n * (rank + 1));
    }
}