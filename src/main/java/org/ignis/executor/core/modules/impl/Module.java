package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.Logger;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IModule;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.rpc.IExecutorException;

import java.util.Arrays;
import java.util.Collection;

public abstract class Module implements IModule {

    IExecutorData executorData;
    private Logger logger;


    public Module(IExecutorData executorData, Logger logger) {
        this.executorData = executorData;
        this.logger = logger;
    }

    public IExecutorData getExecutorData() {
        return executorData;
    }


    public void packException(Exception ex) {
        String message = ex.getMessage();
        StackTraceElement[] stack = ex.getStackTrace();
        String cause = ex.getClass().getName() + ":" + message + "\n Caused by: \n" + Arrays.toString(stack);
        this.logger.error(cause);
//        throw new IExecutorException(message, cause);
    }

    public void useSource(String src) throws IExecutorException {
//        try {
//            this.executorData.loadLibrary(src).before(this.executorData.getContext());
//        } catch (Exception ex) {
//            this.packException(ex);
//        }
    }


    public void resizeMemoryPartition(IMemoryPartition part, int n) {
        Collection<Object> inner = part.getElements();
        IMemoryPartition newPart = new IMemoryPartition(n);
        for(Object obj : part.getElements()){
            newPart.getElements().add(obj);
        }
    }

//    public void exchange(){
//
//    }


}
