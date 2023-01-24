package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.Logger;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.ILibraryLoader;
import org.ignis.executor.core.modules.IModule;
import org.ignis.executor.core.storage.IMemoryPartition;
import org.ignis.executor.core.storage.IPartition;
import org.ignis.executor.core.storage.IPartitionGroup;
import org.ignis.mpi.Mpi;
import org.ignis.rpc.IExecutorException;
import org.ignis.rpc.ISource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class Module implements IModule {

    private final Logger logger;
    IExecutorData executorData;


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

    public void useSource(ISource src) throws IExecutorException {
        try {
            ILibraryLoader.loadISource(src).before(this.executorData.getContext());
        } catch (Exception ex) {
            this.packException(ex);
        }
    }


    public void resizeMemoryPartition(IMemoryPartition part, int n) {
        Collection<Object> inner = part.getElements();
        IMemoryPartition newPart = new IMemoryPartition(n);
        for (Object obj : part.getElements()) {
            newPart.getElements().add(obj);
        }
    }

    public void exchange(IPartitionGroup input, IPartitionGroup output) throws Mpi.MpiException {
        int executors = this.executorData.getMpi().executors();
        if (executors == 1) {
            for (IPartition part : input) {
                part.fit();
                output.add(part);
            }
            return;
        }

        String tp = this.executorData.getPropertyParser().exchangeType();
        boolean sync = false;
        if (tp.equals("sync"))
            sync = true;
        else if (tp.equals("async"))
            sync = false;
        else {
            this.logger.info("Base: detecting exchange type");
            List<Integer> data = new ArrayList<>();
            data.add(input.size());
            int i = 0;
            for (IPartition part : input) {
                if (part.isEmpty())
                    i++;
            }
            data.add(i);
//            this.executorData.getMpi().nativ().reduce(data, 2, MPI.INT, MPI.SUM, 0);
            if (this.executorData.getMpi().isRoot(0)) {
                int n = data.get(0);
                int nZero = data.get(1);
                sync = nZero < n / executors;
            }
//            this.executorData.getMpi().nativ().bcast(sync, 1, MPI.BOOLEAN, 0);
        }
        if (sync) {
            this.logger.info("Base: using synchronous exchange");
            this.executorData.getMpi().exchangeSync(input, output);
        } else {
            this.logger.info("Base: using asynchronous exchange");
            this.executorData.getMpi().exchangeASync(input, output);
        }
    }

//        if (this.propertyParser.transportCores() > 0 && cores > 1 && partType != IDiskPartition.TYPE) {
//
//        }
//
//
//        partsTargets.removeIf(Objects::isNull);
//        this.executorData.enableMPICores();
////        mpiCores = this.executorData.getMpiCores();
//        for (int i = 0; i < numPartitions; i++) {
//            int first = partsTargets.get(i).getKey();
//            int target = partsTargets.get(i).getValue();
//
//            executorData.getMpi().gather(input.get(first), target);
//
//            if (executorData.getMpi().isRoot(target)) {
//                input.get(p).fit();
//            } else {
//                input.get(p).reset();
//            }
//        }

}
