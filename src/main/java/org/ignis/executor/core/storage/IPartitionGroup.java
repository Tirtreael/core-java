package org.ignis.executor.core.storage;

import java.util.ArrayList;

public class IPartitionGroup extends ArrayList<IPartition> {

    private final boolean cache = false;


    public Object shallowCopy() {
        return this.clone();
    }

    public Object deepCopy() {
        IPartitionGroup copy = new IPartitionGroup();
        for (IPartition partition : this) {
            copy.add(partition.clone());
        }
        return copy;
    }

}
