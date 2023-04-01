/*
 * Copyright (C) 2018
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ignis.driver.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.driver.api.IDriverException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.impl.Module;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.ignis.rpc.IExecutorException;
import org.ignis.rpc.executor.ICacheContextModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author César Pomar
 */
public class IDriverContext extends Module implements ICacheContextModule.Iface {


    private static final Logger LOGGER = LogManager.getLogger();

    private long nextId = 0;
    private Map<Long, IPartitionGroup> context = new HashMap<>();
    private Map<Long, Supplier<IPartitionGroup>> data = new HashMap<>();


    public IDriverContext(IExecutorData executorData) {
        super(executorData, LOGGER);
    }

    private IPartitionGroup getContext(long id) {
        IPartitionGroup partitionGroup = this.context.get(id);
        if (partitionGroup == null) {
            throw new IllegalArgumentException("context " + id + " not found");
        }
        this.context.remove(id);
        return partitionGroup;
    }

    @Override
    public synchronized long saveContext() throws IExecutorException, TException {
        try {
            long id = this.nextId;
            this.nextId += 1;
            this.context.put(id, this.getExecutorData().getPartitionGroup());
            return id;
        } catch (Exception ex) {
            this.packException(ex);
        }
        return -1;
    }

    @Override
    public synchronized void clearContext() throws IExecutorException, TException {
        try {
            this.getExecutorData().deletePartitions();
            this.getExecutorData().clearVariables();
        } catch (Exception ex) {
            this.packException(ex);
        }
    }

    @Override
    public synchronized void loadContext(long id) throws IExecutorException, TException {
        try {
            this.getExecutorData().setPartitions(this.getContext(id));
        } catch (Exception ex) {
            this.packException(ex);
        }
    }

    @Override
    public void loadContextAsVariable(long id, String name) throws IExecutorException, TException {
        this.packException(new RuntimeException("Driver does not implement loadContextAsVariable"));
    }

    @Override
    public void cache(long id, byte level) throws IExecutorException, TException {
        this.packException(new RuntimeException("Driver does not implement cache"));
    }

    @Override
    public synchronized void loadCache(long id) throws IExecutorException, TException {
        try {
            IPartitionGroup partitionGroup = this.data.get(id).get();
            if (partitionGroup == null) {
                throw new IllegalArgumentException("data " + id + " not found");
            }
            this.getExecutorData().setPartitions(partitionGroup);
        } catch (Exception ex) {
            this.packException(ex);
        }
    }

    public long parallelize(List<Object> data, boolean nativ) {
        try {
            Supplier<IPartitionGroup> get = () -> {
                IPartitionGroup group = this.getExecutorData().getPartitionTools().newPartitionGroup();
                IPartition partition = new IMemoryPartition();
                partition.setElements(data);
                group.add(partition);

                return group;
            };

            synchronized (this) {
                long id = this.nextId;
                this.nextId += 1;
                this.data.put(id, get);
                return id;
            }
        } catch (Exception e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public synchronized List<Object> collect(long id) {
        try {
            IPartitionGroup group = this.getContext(id);
            if (this.getExecutorData().getPartitionTools().isMemory(group)) {
                IPartition result;
                if (group.size() > 1) {
                    result = new IMemoryPartition();
                    for (IPartition part : group) {
                        part.copyTo(result);
                    }
                } else result = group.get(0);
                return result.getElements();
            }

            List<Object> result = new ArrayList<>();
            for (IPartition part : group) {
                IReadIterator it = part.readIterator();
                while (it.hasNext()) {
                    result.add(it.next());
                }
            }
            return result;

        } catch (Exception ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public Object collect1(long id) {
        Object l = this.collect(id).get(0);
        if (l instanceof List<?> && ((List<?>) l).size() == 0) {
            throw new IDriverException("Empty collection");
        }
        return ((List<?>) l).get(0);
    }

}
