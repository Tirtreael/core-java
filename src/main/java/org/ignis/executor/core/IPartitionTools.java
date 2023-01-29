package org.ignis.executor.core;

import org.ignis.executor.api.IContext;
import org.ignis.executor.core.storage.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IPartitionTools {

    private final IPropertyParser properties;
    private final IContext context;
    private final int partition_id_gen;


    public IPartitionTools(IPropertyParser properties, IContext context) {
        this.properties = properties;
        this.context = context;
        this.partition_id_gen = 0;
    }


    public IPartition newPartition() {
        return this.newPartition("Memory");
    }

    public IPartition newPartition(int size) {
        return this.newPartition("", size);
    }

    public IPartition newPartition(String partitionType) {
        if (partitionType == null)
            partitionType = this.properties.partitionType();
        if (partitionType.equals(IMemoryPartition.TYPE))
            return this.newMemoryPartition();
//        else if(partitionType.equals(IMemoryPartition.TYPE))
//            return this.newMemoryPartition();
//        else if(partitionType.equals(IMemoryPartition.TYPE))
//            return this.newMemoryPartition();
        else throw new IllegalArgumentException("unkown partition type: " + partitionType);
    }

    public IPartition newPartition(String partitionType, int size) {
        if (partitionType.equals(IMemoryPartition.TYPE))
            return this.newMemoryPartition(size);
//        else if(partitionType.equals(IMemoryPartition.TYPE))
//            return this.newMemoryPartition(size);
//        else if(partitionType.equals(IMemoryPartition.TYPE))
//            return this.newMemoryPartition(size);
        else throw new IllegalArgumentException("unkown partition type: " + partitionType);
    }

    public IPartition newPartition(IPartition partition) {
        return newPartition(partition.getType(), partition.size());
    }

    public IPartitionGroup newPartitionGroup() {
        return new IPartitionGroup();
    }

    public IPartitionGroup newPartitionGroup(int nPartitions) {
        IPartitionGroup group = new IPartitionGroup(nPartitions);
        for (int i = 0; i < nPartitions; i++) {
            group.add(this.newPartition());
        }
        return group;
    }

    public IPartitionGroup newPartitionGroup(IPartitionGroup partitionGroup) {
        IPartitionGroup group = new IPartitionGroup();
        for (IPartition partition : partitionGroup) {
            group.add(this.newPartition(partition));
        }
        return group;
    }


    public IPartition newMemoryPartition() {
        return new IMemoryPartition();
    }

    public IMemoryPartition newMemoryPartition(int elems) {
        return new IMemoryPartition(elems);
    }


    public boolean isMemory(IPartition partition) {
        return IMemoryPartition.TYPE.equals(partition.getType());
    }

    public boolean isRawMemory(IPartition partition) {
        return IRawMemoryPartition.TYPE.equals(partition.getType());
    }

    public boolean isDisk(IPartition partition) {
        return IDiskPartition.TYPE.equals(partition.getType());
    }

    public boolean isMemory(IPartitionGroup partitionGroup) {
        return IMemoryPartition.TYPE.equals(partitionGroup.get(0).getType());
    }

    public boolean isRawMemory(IPartitionGroup partitionGroup) {
        return IRawMemoryPartition.TYPE.equals(partitionGroup.get(0).getType());
    }

    public boolean isDisk(IPartitionGroup partitionGroup) {
        return IDiskPartition.TYPE.equals(partitionGroup.get(0).getType());
    }

    public void createDirectoryIfNotExists(String path) {
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
