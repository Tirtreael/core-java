package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IIOModule;
import org.ignis.executor.core.storage.IDiskPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.ignis.mpi.Mpi;
import org.ignis.rpc.IExecutorException;
import org.ignis.rpc.ISource;
import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public class IOModule extends Module implements IIOModule {

    private static final Logger LOGGER = LogManager.getLogger();

    public IOModule(IExecutorData executorData) {
        super(executorData, LOGGER);
    }

    @Override
    public void loadClass(ISource src) {
        try {
            this.useSource(src);
        } catch (IExecutorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadLibrary(String path) {
        try {
            this.executorData.loadLibraryFunctions(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long partitionCount() throws TException {
        return this.executorData.getPartitions().size();
    }

    @Override
    public List<Long> countByPartition() throws TException {
        List<Long> sizes = new ArrayList<>(this.executorData.getPartitions().size());
        for (IPartition part : this.executorData.getPartitionGroup()) {
            sizes.add((long) part.size());
        }
        return sizes;
    }

    @Override
    public long partitionApproxSize() {
        LOGGER.info("IO: calculating partition size");
        return this.getExecutorData().getPartitionGroup().stream()
                .mapToLong(IPartition::bytes).sum();
    }

    @Override
    public void plainFile(String path, byte delim) throws IExecutorException, TException {

    }

    @Override
    public void plainFile3(String path, long minPartitions, byte delim) throws IExecutorException, TException {

    }

    @Override
    public void textFile(String path) throws TException {
        try {
            textFile(path, 1, "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void textFile2(String path, long minPartitions) throws TException {
        try {
            textFile(path, (int) minPartitions, "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // @ToDo with IDiskPartition
    @Override
    public void partitionObjectFile(String path, long first, long partitions) throws TException {
        LOGGER.info("IO: reading partitions object file");
        IPartitionGroup partitionGroup = this.executorData.getPartitionTools().newPartitionGroup((int) partitions);
        this.executorData.setPartitions(partitionGroup);

        for (int i = 0; i < partitions; i++) {
            try {
                String fileName = this.partitionFileName(path, (int) (first + i));
                FileInputStream fileIS = new FileInputStream(this.partitionFileName(path, (int) (first + i)));
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
        LOGGER.info("IO: reading partitions text file");
        IPartitionGroup group = this.executorData.getPartitionTools().newPartitionGroup();
        this.executorData.setPartitions(group);

        for (int i = 0; i < partitions; i++) {
            try (FileInputStream fileIS = new FileInputStream(this.partitionFileName(path, (int) (first + i)));
                 ObjectInputStream ois = new ObjectInputStream(fileIS)
            ) {
                IPartition partition = this.executorData.getPartitionTools().newPartition();
//                IWriteIterator writeIterator = partition.writeIterator();
                Object obj = ois.readObject();
                List<Object> objectArrayList = (List<Object>) obj;
                partition.setElements(objectArrayList);
//                for(Object elem : objectArrayList)
//                writeIterator.write();
//                    obj = ois.readObject();
//                }
                group.add(partition);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void partitionJsonFile4a(String path, long first, long partitions, boolean objectMapping) throws TException {
        LOGGER.info("IO: reading partitions json file");
        IPartitionGroup group = this.executorData.getPartitionTools().newPartitionGroup();
        this.executorData.setPartitions(group);

        for (int i = 0; i < partitions; i++) {
            IPartition partition = this.executorData.getPartitionTools().newPartition();
            try {
                String fileName = this.partitionFileName(path, (int) (first + i)) + ".json";
                FileInputStream fileIS = new FileInputStream(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(fileIS, StandardCharsets.UTF_8));
                IWriteIterator writeIterator = partition.writeIterator();
                JSONTokener tokener = new JSONTokener(br);
                JSONArray object = new JSONArray(tokener);
                for (var v : object) {
                    writeIterator.write(v);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            group.add(partition);
        }

    }

    @Override
    public void partitionJsonFile4b(String path, long first, long partitions, ISource src) throws TException {

    }

    @Override
    public void saveAsObjectFile(String path, byte compression, long first) throws TException {

    }

    public void textFile(String path, int minPartitions, String delim) throws IOException {
        LOGGER.info("IO: reading text file");

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

            LOGGER.info("IO: file has " + size + " Bytes");

            if (executorId > 0) {
                long pos;
                if (exChunkInit > 0)
                    pos = exChunkInit - 1;
                else pos = exChunkInit;
                br.skip(pos);
                exChunkInit = pos + br.readLine().length();
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
                if(bb == null)
                    break;
                if (bb.charAt(bb.length() - 1) == delim.charAt(0))
                    writeIterator.write(new String(bb.substring(0, bb.length() - 1).getBytes(StandardCharsets.UTF_8)));
                else writeIterator.write(new String(bb.getBytes(StandardCharsets.UTF_8)));
                elements += 1;
                filePos += bb.length();
            }
            exChunkEnd = fileIS.getChannel().position();

            LOGGER.info("IO: created " + partitionGroup.size() + " partitions, " + elements
                    + " lines and " + (exChunkEnd - exChunkInit) + "Bytes read ");

        } catch (TException | Mpi.MpiException e) {
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
        return path + "/part" + "0".repeat(zeros) + index;
    }

    /* Reads text and binary files */
    public FileInputStream openFileRead(String path) throws IOException {
        LOGGER.info("IO: opening file " + path);
        FileInputStream fileIS = new FileInputStream(path);
        LOGGER.info("IO: file opening successful");
        return fileIS;
    }

    public FileOutputStream openFileWrite(String path) throws IOException {
        LOGGER.info("IO: opening file " + path);
        Path pathz = Path.of(path);
        if (Files.exists(pathz)) {
            if (executorData.getPropertyParser().ioOverwrite()) {
                LOGGER.warn("IO: " + path + " already exists");
                Files.delete(pathz);
            } else throw new IOException(path + " already exists");
        }
        FileOutputStream fileOS = new FileOutputStream(path);
        LOGGER.info("IO: file create successful");

        return fileOS;
    }

    @Override
    public void partitions() {
// ToDo
    }

    @Override
    public void saveAsObjectFile(String path, String compression, int first) {
        LOGGER.info("IO: saving as text file");
        IPartitionGroup group = this.executorData.getAndDeletePartitions();
        for (int i = 0; i < group.size(); i++) {
            try {
                String fileName = this.partitionFileName(path, (int) (first + i));
                FileOutputStream fileOS = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fileOS);
                LOGGER.info("IO: saving text file " + fileName);
                oos.writeObject(group.get(i).getElements());
//                for(Object elem : group.get(i)){
//                    oos.writeObject(elem);
//                }
                oos.flush();
                oos.close();
                fileOS.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        group.clear();
    }

    @Override
    public void saveAsTextFile(String path, long first) {
        LOGGER.info("IO: saving as text file");
        IPartitionGroup group = this.executorData.getAndDeletePartitions();
        for (IPartition objects : group) {
            try {
                FileWriter file = new FileWriter(path);
                BufferedWriter bw = new BufferedWriter(file);
//                FileOutputStream fileOS = new FileOutputStream(fileName);
//                ObjectOutputStream oos = new ObjectOutputStream(fileOS);
                LOGGER.info("IO: saving text file " + path);
//                oos.writeObject(group.get(i).getElements());
                for (Object elem : objects) {
                    bw.write(elem + "\n");
                }
                bw.flush();
                bw.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        group.clear();
    }

    @Override
    public void saveAsJsonFile(String path, long first, boolean pretty) {
        LOGGER.info("IO: saving as json file");
        IPartitionGroup group = this.executorData.getAndDeletePartitions();

        for (int i = 0; i < group.size(); i++) {
            try {
                String fileName = this.partitionFileName(path, (int) (first + i));
                FileOutputStream fileOS = new FileOutputStream(fileName + ".json");
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOS, StandardCharsets.UTF_8));
                LOGGER.info("IO: saving json file " + fileName);
                JSONArray jsonArray = new JSONArray(group.get(i).getElements());
//                JSONObject jsonObject = new JSONObject(group.get(i).getElements());
                bw.write(jsonArray.toString());
                System.out.println(jsonArray);
                bw.flush();
                bw.close();
                fileOS.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        group.clear();
    }

}
