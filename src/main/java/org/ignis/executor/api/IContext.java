package org.ignis.executor.api;

import mpi.Intracomm;
import mpi.MPI;
import org.ignis.executor.core.IPropertyParser;

import java.util.Map;

// @Todo
public class IContext {

    private IPropertyParser properties;
    private Map<String, Object> variables;
    private mpi.Intracomm mpiGroup = MPI.COMM_WORLD;

    public IContext(IPropertyParser properties) {
        this.properties = properties;
    }

    //    private MPIGroup = mpi.MPI.COMM_WORLD;

    public int cores() {
        return 1;
    }

    public int executors(){
//        return this.mpiGroup.getSize();
        return 0;
    }

    public int executorId(){
//        return this.mpiGroup.getRank();
        return 0;
    }

    public int threadId(){
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
