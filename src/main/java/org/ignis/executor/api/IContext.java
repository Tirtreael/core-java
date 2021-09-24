package org.ignis.executor.api;

import org.ignis.executor.core.IPropertyParser;

// @Todo
public class IContext {

    private IPropertyParser properties = new IPropertyParser();
//    private Object variables;
//    private MPIGroup = mpi.MPI.COMM_WORLD;

    public int cores() {
        return 1;
    }

//    public int execturos(){
//        return this.mpiGroup.getSize();
//    }

//    public int execturoId(){
//        return this.mpiGroup.getRank();
//    }

//    public int threadId(){
//        return 0;
//    }

    public IPropertyParser props() {
        return this.properties;
    }

    public Object vars() {
        return 1;
    }

//    public MPIGroup mpiGroup(){
//        return this.mpiGroup;
//    }

}
