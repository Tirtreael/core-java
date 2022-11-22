package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ignis.executor.core.IExecutorData;
import org.ignis.mpi.Mpi;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

class ModuleTest {

    private static final Logger LOGGER = LogManager.getLogger();
    private final IExecutorData iExecutorData = new IExecutorData();
    private final String library = "build/libs/core-jara-testFunctions.jar";

    private final GeneralModule generalModule = new GeneralModule(iExecutorData);


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
        int n = elems.size() / this.generalModule.getExecutorData().getContext().executors();
        int rank = this.generalModule.getExecutorData().getContext().executorId();
        return elems.subList(n * rank, n * (rank + 1));
    }
}