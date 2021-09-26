package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.Logger;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.storage.IPartition;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

import static java.lang.Math.max;

public class IOModule extends Module {


    public IOModule(IExecutorData executorData, Logger logger) {
        super(executorData, logger);
    }


    public long partitionApproxSize() {
        logger.info("IO: calculating partition size");

        return this.getExecutorData().getPartitions().stream()
                .mapToLong(IPartition::bytes).sum();
    }

    public void textFile(String path, int minPartitions) {
        logger.info("IO: reading text file");

    }


    public String partitionFileName(String path, int index) throws IOException {
        Path pathz = Path.of(path);
        if (!Files.isDirectory(pathz)) {
            Files.createDirectories(pathz);
        }
        String strIndex = String.valueOf(index);
        int zeros = max(6 - strIndex.length(), 0);
        return path + "/part" + '0' * zeros + strIndex;
    }

    /* Reads text and binary files */
    public FileInputStream openFileRead(String path) throws IOException {
        logger.info("IO: opening file " + path);
        FileInputStream fileIS = new FileInputStream(path);
        logger.info("IO: file opening successful");
        return fileIS;
    }

    public FileOutputStream openFileWrite(String path) throws IOException {
        logger.info("IO: opening file " + path);
        Path pathz = Path.of(path);
        if (Files.exists(pathz)) {
            if (executorData.getProperties().ioOverwrite()) {
                logger.warn("IO: " + path + " already exists");
                Files.delete(pathz);
            } else throw new IOException(path + " already exists");
        }
        FileOutputStream fileOS = new FileOutputStream(path);
        logger.info("IO: file create successful");

        return fileOS;
    }

}
