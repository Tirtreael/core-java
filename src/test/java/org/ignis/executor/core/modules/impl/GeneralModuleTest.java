package org.ignis.executor.core.modules.impl;

import mpi.MPIException;
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
import org.ignis.executor.core.ILibraryLoader;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.ignis.rpc.ISource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GeneralModuleTest extends ModuleTest implements IElements {

    private static final Logger LOGGER = LogManager.getLogger();

    static {
        try {
//            mpi.MPI.Init(new String[]{});
        } catch (MPIException e) {
            e.printStackTrace();
        }
    }

    private final GeneralModule generalModule = new GeneralModule(new IExecutorData());

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
    @Timeout(60000)
    void map(String partitionType) {
        IFunction function = ILibraryLoader.loadFunction("org.ignis.executor.api.functions.MapFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = (List<Object>) IElements.createInteger().get(0);
        try {
//            IPartitionGroup group = new IPartitionGroup();
//            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 1000);
            this.generalModule.map(function);
            List<Object> result = this.getFromPartitions();

            assertEquals(elems.size(), result.size());
            for (int i = 0; i < elems.size(); i++) {
                assertEquals(function.call(elems.get(i), generalModule.getExecutorData().getContext()), result.get(i));
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void filter(String partitionType) {

        ISource iSource = this.generalModule.getExecutorData().getLibraryLoader().createSource("org.ignis.executor.api.functions.FilterFunction");
//        IFunction function = this.generalModule.getExecutorData().getLibraryLoader().loadFunction("org.ignis.executor.api.functions.FilterFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = (List<Object>) IElements.createInteger().get(0);
        try {
//            IPartitionGroup group = new IPartitionGroup();
//            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 20);
            this.generalModule.filter(iSource);
            List<Object> result = this.getFromPartitions();

            int j = 0;
            for (Object elem : elems) {
                if (((Integer) elem) > 50) {
                    assertEquals(elem, result.get(j));
                    j++;
                }
            }
            assertEquals(result.size(), j);

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void flatMap(String partitionType) {
        IFunction function = ILibraryLoader.loadFunction("org.ignis.executor.api.functions.FlatMapFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = (List<Object>) IElements.createInteger().get(0);
        try {
//            IPartitionGroup group = new IPartitionGroup();
//            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 20);
            this.generalModule.flatmap(function);
            List<Object> result = this.getFromPartitions();

            for (int i = 0; i < elems.size(); i++) {
                assertEquals(elems.get(i), result.get(2 * i));
                assertEquals(elems.get(i), result.get(2 * i + 1));
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void keyBy(String partitionType) {
        IFunction function = ILibraryLoader.loadFunction("org.ignis.executor.api.functions.KeyByFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = (List<Object>) IElements.createInteger().get(0);
        try {
//            IPartitionGroup group = new IPartitionGroup();
//            group.add(new IMemoryPartition());
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
        IFunction function = ILibraryLoader.loadFunction("org.ignis.executor.api.functions.MapPartitionsFunction");
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = (List<Object>) IElements.createInteger().get(0);
        try {
            /* ARRANGE */
            int partitions = 20;
            this.loadToPartitions(elems, partitions);

            /* ACT */
            this.generalModule.mapPartitions(function);

            /* CHECK */
            List<Object> result = this.getFromPartitions();
            assertEquals(partitions, result.size());
            for (Object oIterator : result) {
                int elements = 0;
                for (IMemoryPartition.IMemoryReadIterator it = (IMemoryPartition.IMemoryReadIterator) oIterator; it.hasNext(); ) {
                    Object o = it.next();
                    elements++;
                }
                // Check there are at least the integer division in each partition
                assertTrue(elems.size() / partitions - 1 <= elements);
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void mapPartitionsWithIndex(String partitionType) {
        IFunction2 function = ILibraryLoader.loadFunction("org.ignis.executor.api.functions.MapPartitionsWithIndexFunction", IFunction2.class);
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = (List<Object>) IElements.createInteger().get(0);
        try {
            /* ARRANGE */
            int partitions = 20;
            this.loadToPartitions(elems, partitions);

            /* ACT */
            this.generalModule.mapPartitionsWithIndex(function, true);

            /* CHECK */
            List<Object> result = this.getFromPartitions();
            assertEquals(partitions, result.size());
            int total = 0;
            for (Object oIterator : result) {
                int elements = 0;
                for (IMemoryPartition.IMemoryReadIterator it = (IMemoryPartition.IMemoryReadIterator) oIterator; it.hasNext(); ) {
                    Object o = it.next();
                    elements++;
                }
                total += elements;
                // Check there are at least the integer division in each partition
                assertTrue(elems.size() / partitions - 1 == elements || elems.size() / partitions == elements);
            }
            assertEquals(elems.size(), total);

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void mapExecutor(String partitionType) {
        IFunction function = ILibraryLoader.loadFunction("org.ignis.executor.api.functions.MapExecutorFunction", IFunction.class);
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = (List<Object>) IElements.createInteger().get(0);
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
        IFunction function = ILibraryLoader.loadFunction("org.ignis.executor.api.functions.MapExecutorToStringFunction", IFunction.class);
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        List<Object> elems = (List<Object>) IElements.createInteger().get(0);
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

    @Disabled
    @ParameterizedTest
    @ValueSource(strings = "Memory")
    void groupBy(String partitionType) {
        IFunction function = ILibraryLoader.loadFunction(
                "org.ignis.executor.api.functions.GroupByIntStringFunction", IFunction.class);
        this.generalModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

        try {
            int np = this.generalModule.getExecutorData().getContext().executors();
            List<Object> elems = IElements.createString(100 * 2 * np, 0);
            List<Object> localElems = super.rankVector(elems);
            this.loadToPartitions(elems, 2);
            this.generalModule.groupBy(function, 1);
            List<Object> result = this.getFromPartitions();

            Map<Integer, Integer> counts = new HashMap<>();
            for (Object obj : elems) {
                int len = ((String) obj).length();
                if (counts.containsKey(len)) {
                    counts.put(len, counts.get(len) + 1);
                } else {
                    counts.put(len, 1);
                }
            }
            this.loadToPartitions(result, 1);
            generalModule.getExecutorData().getMpi().gather(generalModule.getExecutorData().getPartitions().get(0), 0);
            List<Object> result2 = this.getFromPartitions();

            if (getExecutorData().getMpi().isRoot(0)) {
                for (Object obj : result2) {
                    Pair<Integer, ArrayList<String>> item = (Pair<Integer, ArrayList<String>>) obj;
                    assertEquals(counts.get(item.getKey()), item.getValue().size());
                }
            }

        } catch (TException | MPIException | IOException e) {
            e.printStackTrace();
        }
    }


}