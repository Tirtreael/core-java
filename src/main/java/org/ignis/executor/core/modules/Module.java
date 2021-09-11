package org.ignis.executor.core.modules;

import org.ignis.executor.core.IExecutorData;
import org.slf4j.Logger;

public class Module {
    private final IExecutorData executorData;
    private final Logger logger;


    public Module(IExecutorData executorData, Logger logger) {
        this.executorData = executorData;
        this.logger = logger;
    }

    public IExecutorData getExecutorData() {
        return executorData;
    }

    public Logger getLogger() {
        return logger;
    }

//    public void useSource(Runnable src){
//        src.run(this.executor_data.before(this.executor_data.getContext()));
//    }

}
