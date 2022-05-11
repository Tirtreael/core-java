package org.ignis.executor.api;

import mpi.Intracomm;
import mpi.MPI;
import mpi.MPIException;
import org.ignis.executor.core.IPropertyParser;

import java.util.Map;

// @Todo
public class IContext {

    private final IPropertyParser properties;
    private Map<String, Object> variables;
    private final mpi.Intracomm mpiGroup = MPI.COMM_WORLD;

    public IContext(IPropertyParser properties) {
        this.properties = properties;
    }

    //    private MPIGroup = mpi.MPI.COMM_WORLD;

    public int cores() {
        return 1;
    }

    public int executors() throws MPIException {
        return MPI.COMM_WORLD.getSize();
    }

    public int executorId() {
//        return this.mpiGroup.getRank();
        return 0;
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

    public Intracomm getMPIGroup(){
        return this.mpiGroup;
    }

}
