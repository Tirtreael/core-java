package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.core.IElements;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneralModuleTest {

    private GeneralModule generalModule = new GeneralModule(new IExecutorData());;
    private static final Logger LOGGER = LogManager.getLogger();

    public GeneralModuleTest() {
        Properties props = this.generalModule.getExecutorData().getPropertyParser().getProperties();
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

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    void loadToPartitions(List<Object> elems, IPartitionGroup partitionGroup) throws TException {
        IPartitionGroup group = this.generalModule.getExecutorData().getPartitionTools().newPartitionGroup(partitionGroup);
        this.generalModule.getExecutorData().setPartitions(group);
        int partitionSize = elems.size() / partitionGroup.size();
        for(int p=0; p < partitionGroup.size(); p++){
            IWriteIterator writeIterator = group.get(p).writeIterator();
            int i = partitionSize * p;
            while( i < partitionSize * (p+1) && i < elems.size()){
                writeIterator.write(elems.get(i));
                i += 1;
            }
        }
    }

    List<Object> getFromPartitions() throws TException {
        List<Object> elems = new ArrayList<>();
        IPartitionGroup group = this.generalModule.getExecutorData().getPartitionGroup();
        for (IPartition objects : group) {
            IReadIterator readIterator = objects.readIterator();
            while (readIterator.hasNext()) {
                elems.add(readIterator.next());
            }
        }
        return elems;
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void map(String partitionType) throws ClassNotFoundException {
        IFunction function = this.generalModule.getExecutorData().loadLibraryFunctions("artifacts/IFunctionExample.jar").get("org.ignis.executor.api.functions.IFunctionExample");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);
        
        List<Object> elems = IElements.createInteger().collect(Collectors.toList());
        try {
            IPartitionGroup group = new IPartitionGroup();
            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, group);
            this.generalModule.map(function);
            List<Object> result = this.getFromPartitions();

            for(int i=0; i < elems.size(); i++){
                assertEquals(function.call(elems.get(i), this.generalModule.executorData.getContext()), result.get(i));
                System.out.println(result.get(i));
            }

        } catch (TException e) {
            e.printStackTrace();
        }


    }
}