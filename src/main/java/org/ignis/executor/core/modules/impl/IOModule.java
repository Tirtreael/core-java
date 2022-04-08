package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IIOModule;
import org.ignis.executor.core.storage.IDiskPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.ignis.rpc.ISource;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.max;

public class IOModule extends Module implements IIOModule {

    private final Logger logger;

    public IOModule(IExecutorData executorData, Logger logger) {
        super(executorData, logger);
        this.logger = logger;
    }

    //@ToDo
    @Override
    public void loadClass(ISource src) throws TException {
//        this.useSource(src);
    }

    public void loadLibrary(String path){
        try{
            this.executorData.loadLibraryFunctions(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long partitionCount() throws TException {
        return this.executorData.getPartitionGroup().size();
    }

    @Override
    public List<Long> countByPartition() throws TException {
        List<Long> sizes = new ArrayList<>(this.executorData.getPartitions().size());
        for(IPartition part : this.executorData.getPartitionGroup()){
            sizes.add((long) part.size());
        }
        return sizes;
    }

    public long partitionApproxSize() {
        logger.info("IO: calculating partition size");
        return this.getExecutorData().getPartitionGroup().stream()
                .mapToLong(IPartition::bytes).sum();
    }

    @Override
    public void textFile(String path) throws TException {
        try {
            textFile(path, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void textFile2(String path, long minPartitions) throws TException {
        try {
            textFile(path, (int) minPartitions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @ToDo with IDiskPartition
    @Override
    public void partitionObjectFile(String path, long first, long partitions) throws TException {
        logger.info("IO: reading partitions object file");
        IPartitionGroup partitionGroup = this.executorData.getPartitionTools().newPartitionGroup((int) partitions);
        this.executorData.setPartitions(partitionGroup);

        for(int i=0;i<partitions; i++){
            try {
                String fileName = this.partitionFileName(path, (int) (first+i));
                FileInputStream fileIS = new FileInputStream(this.partitionFileName(path, (int) (first+i)));
                BufferedReader br = new BufferedReader(new InputStreamReader(fileIS, StandardCharsets.UTF_8));
                IPartition partition = new IDiskPartition(fileName, 0, false, true, true);
                partition.copyTo(partition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void partitionObjectFile4(String path, long first, long partitions, ISource src) throws TException {
        this.partitionObjectFile(path, first, partitions);
    }

    @Override
    public void partitionTextFile(String path, long first, long partitions) throws TException {
        logger.info("IO: reading partitions text file");
        IPartitionGroup group = this.executorData.getPartitionTools().newPartitionGroup();
        this.executorData.setPartitions(group);

        for(int i=0;i<partitions; i++){
            try (FileInputStream fileIS = new FileInputStream(this.partitionFileName(path, (int) (first+i)));
                 BufferedReader br = new BufferedReader(new InputStreamReader(fileIS, StandardCharsets.UTF_8))
            ){
                IPartition partition = this.executorData.getPartitionTools().newPartition();
                IWriteIterator writeIterator = partition.writeIterator();
                writeIterator.write(br.lines().map(String::trim));
                group.add(partition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void partitionJsonFile4a(String path, long first, long partitions, boolean objectMapping) throws TException {
        logger.info("IO: reading partitions json file");
        IPartitionGroup group = this.executorData.getPartitionTools().newPartitionGroup();
        this.executorData.setPartitions(group);

        for(int i=0;i<partitions; i++){
            try {
                String fileName = this.partitionFileName(path, (int) (first+i))+".json";
                FileInputStream fileIS = new FileInputStream(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(fileIS, StandardCharsets.UTF_8));
                IPartition partition = this.executorData.getPartitionTools().newPartition();
                IWriteIterator writeIterator = partition.writeIterator();
                if(objectMapping){

                }
                else{
                    JSONObject json = new JSONObject(br);
                    Iterator<String> keys = json.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        JSONObject value = json.getJSONObject(key);
                        String component = value.getString("component");
                        writeIterator.write(component);
                    }
                }

                group.add(partition);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void partitionJsonFile4b(String path, long first, long partitions, ISource src) throws TException {

    }

    @Override
    public void saveAsObjectFile(String path, byte compression, long first) throws TException {

    }

    @Override
    public void saveAsTextFile(String path, long first) throws TException {

    }

    @Override
    public void saveAsJsonFile(String path, long first, boolean pretty) throws TException {

    }

    public void textFile(String path, int minPartitions) throws IOException {
        logger.info("IO: reading text file");

        try (
                FileInputStream fileIS = new FileInputStream(path);
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(fileIS, StandardCharsets.UTF_8))
        ) {
            long size = Files.size(Path.of(path));
            int executorId = this.executorData.getContext().executorId();
            int executors = this.executorData.getContext().executors();
            long exChunk = (int) (size / executors);
            long exChunkInit = executorId * exChunk;
            long exChunkEnd = exChunkInit + exChunk;
            long minPartitionsSize = this.executorData.getPropertyParser().partitionMinimal();
            minPartitions = (int) Math.ceil(minPartitions / executors);

            logger.info("IO: file has " + size + " Bytes");

            if (executorId > 0) {
                if (exChunkInit > 0)
                    br.skip(exChunkInit - 1);
                else br.skip(exChunkInit);
                if (executorId == executors - 1)
                    exChunkEnd = (int) size;
            }

            if ((exChunk / minPartitionsSize) < minPartitions) {
                minPartitionsSize = exChunk / minPartitions;
            }

            IPartitionGroup partitionGroup = this.executorData.getPartitionTools().newPartitionGroup();
            this.executorData.setPartitions(partitionGroup);
            IPartition partition = this.executorData.getPartitionTools().newPartition();
            IWriteIterator writeIterator = partition.writeIterator();
            partitionGroup.add(partition);
            long partitionInit = exChunkInit;
            long filePos = exChunkInit;
            long elements = 0;
            while (filePos < exChunkEnd) {
                if ((filePos - partitionInit) > minPartitionsSize) {
                    partition = this.executorData.getPartitionTools().newPartition();
                    writeIterator = partition.writeIterator();
                    partitionGroup.add(partition);
                    partitionInit = filePos;
                }

                String bb = br.readLine();
                writeIterator.write(new String(bb.getBytes(StandardCharsets.UTF_8)));
                elements += 1;
                filePos += bb.length();
            }
            exChunkEnd = fileIS.getChannel().position();

            logger.info("IO: created " + partitionGroup.size() + " partitions, " + elements
                    + " lines and " + (exChunkEnd - exChunkInit) + "Bytes read ");

        } catch (TException e) {
            e.printStackTrace();
        }
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
            if (executorData.getPropertyParser().ioOverwrite()) {
                logger.warn("IO: " + path + " already exists");
                Files.delete(pathz);
            } else throw new IOException(path + " already exists");
        }
        FileOutputStream fileOS = new FileOutputStream(path);
        logger.info("IO: file create successful");

        return fileOS;
    }

    @Override
    public void partitions() {

    }

    @Override
    public void saveAsObjectFile(String path, String compression, int first) {

    }

    @Override
    public void saveAsTextFile(String path, int first) {

    }

    @Override
    public void saveAsJsonFile(String path, int first, boolean pretty) {

    }

    @Override
    public void packException(Exception ex) {

    }
}
