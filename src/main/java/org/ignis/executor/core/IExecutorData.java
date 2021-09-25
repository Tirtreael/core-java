package org.ignis.executor.core;

//import mpi.MPI;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.core.storage.IPartition;

import java.util.List;
import java.util.Map;


public class IExecutorData {

    private IContext context;
    private IPropertyParser properties;
    private ILibraryLoader libraryLoader;
    private IPartitionTools partitionTools;
    private IMPI mpi;
    private List<IPartition> partitions;
    private Map<Object, Object> variables;

    public IExecutorData() {
        this.properties = new IPropertyParser();
        this.libraryLoader = new ILibraryLoader(this.properties);
    }

    public Object getVariable(Object key) {
        return variables.get(key);
    }

    public void setVariable(Object key, Object value) {
        variables.put(key, value);
    }

    public boolean hasVariable(Object key) {
        return variables.containsKey(key);
    }

    public void removeVariable(Object key) {
        variables.remove(key);
    }

    public void clearVariables() {
        variables.clear();
    }

    public IContext getContext() {
        return context;
    }

    public IPropertyParser getProperties() {
        return properties;
    }

    public IPartitionTools getPartitionTools() {
        return partitionTools;
    }

    public IMPI getMpi() {
        return mpi;
    }

    public List<IPartition> getPartitions() {
        return partitions;
    }

    public List<IPartition> getAndDeletePartitions() {
        return partitions;
    }

    public boolean hasPartitions() {
        return !partitions.isEmpty();
    }

    public List<IPartition> setPartitions(List<IPartition> partitions) {
        List<IPartition> old = this.partitions;
        this.partitions = partitions;
        return old;
    }

    public void deletePartitions() {
        partitions = null;
    }

    public String infoDirectory() {
        String info = this.properties.executorDirectory() + "/info";
//        this.partitionTools.createDirectoryIfNotExists(info);
        return info;
    }

    public Map<String, IFunction> loadLibrary(String src) throws ClassNotFoundException {
        try {
            return this.libraryLoader.loadLibrary(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
