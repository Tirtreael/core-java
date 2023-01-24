package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.ICacheContextModule;
import org.ignis.executor.core.modules.IModule;
import org.ignis.rpc.IExecutorException;

// @TODO

public class CacheContextModule extends Module implements IModule, ICacheContextModule {


    private static final Logger LOGGER = LogManager.getLogger();
    public CacheContextModule(IExecutorData executorData) {
        super(executorData, LOGGER);
    }


    @Override
    public long saveContext() throws IExecutorException, TException {
        return 0;
    }

    @Override
    public void clearContext() throws IExecutorException, TException {

    }

    @Override
    public void loadContext(long id) throws IExecutorException, TException {

    }

    @Override
    public void loadContextAsVariable(long id, String name) throws IExecutorException, TException {

    }

    @Override
    public void cache(long id, byte level) throws IExecutorException, TException {

    }

    @Override
    public void loadCache(long id) throws IExecutorException, TException {

    }
}
