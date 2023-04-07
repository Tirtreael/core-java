package org.ignis.executor.api;

import org.ignis.executor.core.IPropertyParser;

import java.util.HashMap;
import java.util.Map;

public class IContext {

    private final IPropertyParser properties;
    private final Map<String, Object> variables;
//    private final mpi.Intracomm mpiGroup = MPI.COMM_WORLD;

    public IContext(IPropertyParser properties) {
        this.properties = properties;
        this.variables = new HashMap<>();
    }

    //    private MPIGroup = mpi.MPI.COMM_WORLD;

    public int cores() {
        return 1;
    }

    public int executors() {
        return 1; //MPI.COMM_WORLD.Size();
    }

    public int executorId() {
        return 0; //this.mpiGroup.Rank();
    }

    public int threadId() {
        return 0;
    }

    public IPropertyParser props() {
        return this.properties;
    }

    public Map<String, Object> vars() {
        return this.variables;
    }

    public IThreadContext newIThreadContext(int threadID) {
        return new IThreadContext(this, threadID);
    }

}
