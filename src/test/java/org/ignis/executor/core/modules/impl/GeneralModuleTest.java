package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.api.Pair;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.api.function.IFunction2;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneralModuleTest {

    private final GeneralModule generalModule = new GeneralModule(new IExecutorData());
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

    void loadToPartitions(List<Object> elems, int partitions) throws TException {
        IPartitionGroup group = this.generalModule.getExecutorData().getPartitionTools().newPartitionGroup(partitions);
        this.generalModule.getExecutorData().setPartitions(group);
        int partitionSize = elems.size() / group.size();
        for(int p=0; p < group.size(); p++){
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
    void map(String partitionType) {
        IFunction function = this.generalModule.getExecutorData().getLibraryLoader().loadFunction("org.ignis.executor.api.functions.MapFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);
        
        List<Object> elems = IElements.createInteger(100 * 2, 0);
        try {
            IPartitionGroup group = new IPartitionGroup();
            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 20);
            this.generalModule.map(function);
            List<Object> result = this.getFromPartitions();

            assertEquals(elems.size(), result.size());
            for(int i=0; i < elems.size(); i++){
                assertEquals(function.call(elems.get(i), generalModule.getExecutorData().getContext()), result.get(i));
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void filter(String partitionType) {
        IFunction function = this.generalModule.getExecutorData().getLibraryLoader().loadFunction("org.ignis.executor.api.functions.FilterFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = IElements.createInteger(100 * 2, 0);
        try {
            IPartitionGroup group = new IPartitionGroup();
            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 20);
            this.generalModule.filter(function);
            List<Object> result = this.getFromPartitions();

            for(int i=0, j=0; i < elems.size(); i++){
                if(((Integer) elems.get(i))>50) {
                    assertEquals(elems.get(i), result.get(j));
                    j++;
                }
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void flatMap(String partitionType) {
        IFunction function = this.generalModule.getExecutorData().getLibraryLoader().loadFunction("org.ignis.executor.api.functions.FlatMapFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = IElements.createInteger(100 * 2, 0);
        try {
            IPartitionGroup group = new IPartitionGroup();
            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 20);
            this.generalModule.flatmap(function);
            List<Object> result = this.getFromPartitions();

            for(int i=0; i < elems.size(); i++){
                assertEquals(elems.get(i), result.get(2*i));
                assertEquals(elems.get(i), result.get(2*i+1));
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void keyBy(String partitionType) {
        IFunction function = this.generalModule.getExecutorData().getLibraryLoader().loadFunction("org.ignis.executor.api.functions.KeyByFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = IElements.createInteger(100 * 2, 0);
        try {
            IPartitionGroup group = new IPartitionGroup();
            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 20);
            this.generalModule.keyBy(function);
            List<Object> result = this.getFromPartitions();

            for (int i = 0; i < elems.size(); i++) {
                assertEquals(function.call(elems.get(i), generalModule.getExecutorData().getContext()), ((Pair<Object, Object>) result.get(i)).getKey());
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void mapPartitions(String partitionType) {
        IFunction function = this.generalModule.getExecutorData().getLibraryLoader().loadFunction("org.ignis.executor.api.functions.MapPartitionsFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = IElements.createInteger(100 * 20, 0);
        try {
//            IPartitionGroup group = new IPartitionGroup();
//            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 20);
            this.generalModule.mapPartitions(function);
            List<Object> result =  this.getFromPartitions();

            IReadIterator readIterator = new IMemoryPartition.IMemoryReadIterator(elems);
            for(IPartition part : this.generalModule.getExecutorData().getPartitions()) {
                for (int i = 0; i < part.size(); i++) {
                    assertEquals(readIterator.next(), ((IReadIterator) result.get(i)).next());
                }
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void mapPartitionsWithIndex(String partitionType) {
        IFunction2 function = this.generalModule.getExecutorData().getLibraryLoader().loadFunction("org.ignis.executor.api.functions.MapPartitionsWithIndexFunction", IFunction2.class);
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = IElements.createInteger(100 * 20, 0);
        try {
//            IPartitionGroup group = new IPartitionGroup();
//            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 20);
            this.generalModule.mapPartitionsWithIndex(function, true);
            List<Object> result =  this.getFromPartitions();

            IReadIterator readIterator = new IMemoryPartition.IMemoryReadIterator(elems);
            for (IPartition part : this.generalModule.getExecutorData().getPartitions()) {
                for (int i = 0; i < part.size(); i++) {
                    assertEquals(readIterator.next(), ((IReadIterator) result.get(i)).next());
                }
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void mapExecutor(String partitionType) {
        IFunction function = this.generalModule.getExecutorData().getLibraryLoader().loadFunction("org.ignis.executor.api.functions.MapExecutorFunction", IFunction.class);
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = IElements.createInteger(100 * 20, 0);
        try {
            this.loadToPartitions(elems, 20);
            this.generalModule.mapExecutor(function);
            List<Object> result = this.getFromPartitions();

            for (int i = 0; i < elems.size(); i++) {
                assertEquals((int) elems.get(i) + 1, result.get(i));
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void mapExecutorTo(String partitionType) {
        IFunction function = this.generalModule.getExecutorData().getLibraryLoader().loadFunction("org.ignis.executor.api.functions.MapExecutorToStringFunction", IFunction.class);
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = IElements.createInteger(100 * 2, 0);
        try {
            this.loadToPartitions(elems, 20);
            this.generalModule.mapExecutorTo(function);
            List<Object> result = this.getFromPartitions();

            for (int i = 0; i < elems.size(); i++) {
                assertEquals(elems.get(i).toString(), result.get(i));
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }
}