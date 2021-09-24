package org.ignis.executor.core.storage;

import java.util.ArrayList;
import java.util.Collection;

public class IPartitionGroup extends ArrayList<IPartition> {

    private final boolean cache = false;

    public IPartitionGroup(int initialCapacity) {
        super(initialCapacity);
    }

    public IPartitionGroup() {
        super();
    }

    public IPartitionGroup(Collection<? extends IPartition> c) {
        super(c);
    }

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
