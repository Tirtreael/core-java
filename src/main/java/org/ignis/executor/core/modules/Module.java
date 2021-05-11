package org.ignis.executor.core.modules;

import org.ignis.executor.core.IExecutorData;
import org.slf4j.Logger;

public class Module {
    IExecutorData executorData;
    Logger logger;


    public Module(IExecutorData executorData, Logger logger) {
        this.executorData = executorData;
        this.logger = logger;
    }

//    public void useSource(Runnable src){
//        src.run(this.executor_data.before(this.executor_data.getContext()));
//    }

}
