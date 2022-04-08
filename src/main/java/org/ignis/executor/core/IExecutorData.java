package org.ignis.executor.core;

// @ToDo loadLibrary, loadParameters, reloadLibraries

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;

import java.util.List;
import java.util.Map;


public class IExecutorData {

    private IContext context;
    private IPropertyParser propertyParser;
    private ILibraryLoader libraryLoader;
    private IPartitionTools partitionTools;
    private IMPI mpi;
    private IPartitionGroup partitions;
    private Map<Object, Object> variables;

    public ILibraryLoader getLibraryLoader() {
        return libraryLoader;
    }

    public Map<Object, Object> getVariables() {
        return variables;
    }

    public IExecutorData() {
        this.propertyParser = new IPropertyParser();
        this.context = new IContext(this.propertyParser);
        this.libraryLoader = new ILibraryLoader(this.propertyParser);
        this.partitionTools = new IPartitionTools(this.propertyParser, this.context);
    }

    public Object getVariable(String key) {
        return variables.get(key);
    }

    public void setVariable(String key, Object value) {
        variables.put(key, value);
    }

    public boolean hasVariable(String key) {
        return variables.containsKey(key);
    }

    public void removeVariable(String key) {
        variables.remove(key);
    }

    public void clearVariables() {
        variables.clear();
    }

    public IContext getContext() {
        return context;
    }

    public IPropertyParser getPropertyParser() {
        return propertyParser;
    }

    public IPartitionTools getPartitionTools() {
        return partitionTools;
    }

    public IMPI getMpi() {
        return mpi;
    }

    public List<IPartition> getPartitions() {
        return this.partitions;
    }

    public IPartitionGroup getPartitionGroup() {
        IPartitionGroup group = new IPartitionGroup(this.partitions);
        if(group.size() > 0 && this.propertyParser.loadType()){
            IPartition partition = group.get(0);
            if(this.partitionTools.isMemory(partition)){
                this.context.vars().put("STORAGE_CLASS", partition.getClass());
            }
        }
        return group;
    }

    public IPartitionGroup getAndDeletePartitions() {
        IPartitionGroup group = this.getPartitionGroup();
        this.deletePartitions();
        if(group.isCache())
            group.shallowCopy();
        return group;
    }

    public boolean hasPartitions() {
        return !partitions.isEmpty();
    }

    public IPartitionGroup setPartitions(IPartitionGroup partitions) {
        IPartitionGroup old = this.partitions;
        this.partitions = partitions;
        return old;
    }

    public void deletePartitions() {
        partitions = null;
    }

    public String infoDirectory() {
        String info = this.propertyParser.executorDirectory() + "/info";
        this.partitionTools.createDirectoryIfNotExists(info);
        return info;
    }

    public Map<String, IFunction> loadLibraryFunctions(String src) throws ClassNotFoundException {
        try {
            return this.libraryLoader.loadLibrary(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
