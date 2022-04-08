package org.ignis.executor.core.modules.impl;

import org.ignis.executor.core.IExecutorData;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class ModuleTest {

    private IExecutorData iExecutorData = new IExecutorData();
    private String library = "artifacts/FunctionExample.jar";

    public ModuleTest(){
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
}