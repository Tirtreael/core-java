package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.core.IElements;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class CacheContextTest extends ModuleTest implements IElements {

    private static final Logger LOGGER = LogManager.getLogger();


    private final CacheContextModule cacheContextModule = new CacheContextModule(super.getExecutorData());


    public CacheContextTest() {
        Properties props = this.getExecutorData().getPropertyParser().getProperties();
        props.put("ignis.transport.compression", "0");
        props.put("ignis.partition.compression", "0");
        props.put("ignis.partition.serialization", "native");
        props.put("ignis.executor.directory", "./");
        props.put("ignis.executor.cores", "1");
        props.put("ignis.transport.cores", "0");
        props.put("ignis.modules.load.type", "false");
        props.put("ignis.modules.exchange.type", "sync");

        props.put("ignis.modules.sort.samples", "0.1");
        props.put("ignis.modules.sort.resampling", "false");
    }

    @Test
    void saveRecover() throws TException {
        List<Object> elems = (List<Object>) IElements.createInteger().get(0);
        List<Object> elems2 = (List<Object>) IElements.createInteger().get(0);
        this.loadToPartitions(elems, 20);

        long context = this.cacheContextModule.saveContext();
        IExecutorData executorData = this.getExecutorData();
        IPartitionGroup partitionGroup = this.getExecutorData().getPartitionGroup();
        assertEquals(this.getExecutorData(), this.cacheContextModule.getExecutorData());

        assertNotNull(this.cacheContextModule.getExecutorData().getPartitions());

        this.cacheContextModule.clearContext();
        assertNull(this.cacheContextModule.getExecutorData().getPartitions());
        assertNotNull(partitionGroup);
        assertNull(this.getExecutorData().getPartitions());

        this.loadToPartitions(elems2, 20);

        this.cacheContextModule.loadContext(context);
        assertNotNull(this.cacheContextModule.getExecutorData().getPartitions());
        assertEquals(partitionGroup, this.getExecutorData().getPartitionGroup());
    }


}
