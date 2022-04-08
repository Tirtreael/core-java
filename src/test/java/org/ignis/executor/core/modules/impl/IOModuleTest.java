package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

class IOModuleTest {

    private ModuleTest moduleTest;
    private IOModule iioModule;
    private static final Logger LOGGER = LogManager.getLogger();

    @BeforeEach
    void setUp() {
        this.moduleTest = new ModuleTest();
        this.iioModule = new IOModule(this.moduleTest.getExecutorData(), LOGGER);
        Properties props = this.moduleTest.getExecutorData().getPropertyParser().getProperties();
        props.setProperty("ignis.partition.minimal", "10MB");
        props.setProperty("ignis.partition.type", "Memory");
        props.setProperty("ignis.modules.io.overwrite", "true");

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
        String path = "./testfile.txt";
        

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

    @Test
    void saveAsJsonFile() {
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