package org.ignis.executor.core.modules;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ignis.executor.core.IExecutorData;
import org.ignis.rpc.IExecutorException;

import java.util.Arrays;

public abstract class Module implements IModule {

    private final IExecutorData executorData;
    private static final Logger LOGGER = LogManager.getLogger();


    public Module(IExecutorData executorData) {
        this.executorData = executorData;
    }

    public IExecutorData getExecutorData() {
        return executorData;
    }


    public void packException(Exception ex) throws IExecutorException {
        String message = ex.getMessage();
        StackTraceElement[] stack = ex.getStackTrace();
        String cause = ex.getClass().getName() + ":" + message + "\n Caused by: \n" + Arrays.toString(stack);
        LOGGER.error(cause);
        throw new IExecutorException(message, cause);
    }

    public void useSource(String src) throws IExecutorException {
//        try {
//            this.executorData.loadLibrary(src).before(this.executorData.getContext());
//        } catch (Exception ex) {
//            this.packException(ex);
//        }
    }


}
