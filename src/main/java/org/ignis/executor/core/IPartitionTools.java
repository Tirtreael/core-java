package org.ignis.executor.core;

import org.ignis.executor.api.IContext;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;

public class IPartitionTools {

    private IPropertyParser properties;
    private IContext context;
    private int partition_id_gen;


    public IPartitionTools(IPropertyParser properties, IContext context) {
        this.properties = properties;
        this.context = context;
        this.partition_id_gen = 0;
    }


    public IPartition newPartition() {
        return this.newPartition("");
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
        return newPartition(partition.getTYPE(), partition.size());
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
        IPartitionGroup group = new IPartitionGroup(partitionGroup);
        for (IPartition ignored : partitionGroup) {
            group.add(this.newPartition());
        }
        return group;
    }


    public IPartition newMemoryPartition() {
        return new IMemoryPartition();
    }

    public IPartition newMemoryPartition(int elems) {
        return new IMemoryPartition(elems);
    }

    public boolean isMemory(IPartitionGroup partitionGroup){
        return partitionGroup.size() > 0 && partitionGroup.get(0) instanceof IMemoryPartition;
    }

    public boolean isMemory(IPartition partition){
        return IMemoryPartition.TYPE.equals(partition.getTYPE());
    }




}