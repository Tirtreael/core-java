package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.IElements;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IOModuleTest implements IElements {
    private static final Logger LOGGER = LogManager.getLogger();
    private final IOModule ioModule = new IOModule(new IExecutorData());

    public IOModuleTest() {
        Properties props = this.ioModule.getExecutorData().getPropertyParser().getProperties();
        props.setProperty("ignis.partition.minimal", "10MB");
        props.setProperty("ignis.partition.type", "Memory");
        props.setProperty("ignis.modules.io.overwrite", "true");
        props.put("ignis.modules.load.type", "false");
    }

    @BeforeEach
    void setUp() {

    }

    void loadToPartitions(List<Object> elems, int partitions) throws TException {
        IPartitionGroup group = this.ioModule.getExecutorData().getPartitionTools().newPartitionGroup(partitions);
        this.ioModule.getExecutorData().setPartitions(group);
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
        IPartitionGroup group = this.ioModule.getExecutorData().getPartitionGroup();
        for (IPartition objects : group) {
            IReadIterator readIterator = objects.readIterator();
            while (readIterator.hasNext()) {
                elems.add(readIterator.next());
            }
        }
        return elems;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadClass() {
    }

    @Test
    void loadLibrary() {
    }

    @Test
    void partitionCount() {
    }

    @Test
    void countByPartition() {
    }

    @Test
    void partitionApproxSize() {
    }

    @Test
    void textFile() {
        String partitionType = "Memory";
        String pathIn = "/home/miguelr/Downloads/ignis-downloads/ignis-deploy/text.txt";
        String pathOut = "/home/miguelr/Downloads/ignis-downloads/ignis-deploy/outText.txt";
        try {
            this.ioModule.textFile(pathIn);
            this.ioModule.saveAsTextFile(pathOut, 0);
            assertEquals(-1L, Files.mismatch(Path.of(pathIn), Path.of(pathOut)));
        } catch (TException | IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void textFile2() {
    }

    @Test
    void partitionObjectFile() {
    }

    @Test
    void partitionObjectFile4() {
    }

    @Test
    void partitionTextFile() {
    }

    @Test
    void partitionJsonFile4a() {
    }

    @Test
    void partitionJsonFile4b() {
    }

    @Test
    void saveAsObjectFile() {
    }

    @Test
    void saveAsTextFile() {
    }

    @Disabled
    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList", "createJson"})
    void saveAsTextFile(List<Object> elems) {
        String partitionType = "Memory";
        String path = "test-data/ignisIOTestText" + elems.get(0).getClass().getName();
        this.ioModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

//        List<Object> elems = (List<Object>) IElements.createMap().get(0);
        try {
//            IPartitionGroup group = new IPartitionGroup();
//            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 2);
            this.ioModule.saveAsTextFile(path, 0);
            this.ioModule.partitionTextFile(path, 0, 2);
            List<Object> result = this.getFromPartitions();

//            assertEquals(elems.get(0), result.get(0));
            assertEquals(elems.size(), result.size());
            for (int i = 0; i < elems.size(); i++) {
                assertEquals(elems.get(i), result.get(i));
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }


    //    @Disabled
    @ParameterizedTest
    @MethodSource({"createBoolean", "createByte", "createShort", "createInteger", "createLong", "createDouble",
            "createString", "createList", "createSet", "createMap", "createPair", "createBinary",
            "createPairList"/*, "createJson"*/})
    void saveAsJsonFile(List<Object> elems) {
        String partitionType = "Memory";
        String path = "test-data/Downloads/ignisIOTestJson" + elems.get(0).getClass().getName();
        this.ioModule.getExecutorData().getPropertyParser().getProperties().put("ignis.partition.type", partitionType);

//        List<Object> elems = (List<Object>) IElements.createMap().get(0);
        try {
//            IPartitionGroup group = new IPartitionGroup();
//            group.add(new IMemoryPartition());
            this.loadToPartitions(elems, 3);
            this.ioModule.saveAsJsonFile(path, 0, true);
            this.ioModule.partitionJsonFile4a(path, 0, 3, true);
            List<Object> result = this.getFromPartitions();

//            assertEquals(elems, result);
            assertEquals(elems.size(), result.size());
            for (int i = 0; i < elems.size(); i++) {
                assertEquals(elems.get(i), result.get(i));
            }

        } catch (TException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTextFile() {
    }

    @Test
    void partitionFileName() {
    }

    @Test
    void openFileRead() {
    }

    @Test
    void openFileWrite() {
    }

    @Test
    void partitions() {
    }

    @Test
    void testSaveAsObjectFile() {
    }

    @Test
    void testSaveAsTextFile() {
    }

    @Test
    void testSaveAsJsonFile() {
    }

    @Test
    void packException() {
    }
}