package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.ICacheContextModule;
import org.ignis.executor.core.modules.IModule;
import org.ignis.rpc.IExecutorException;


public class CacheContextModule extends Module implements IModule, ICacheContextModule {


    private static final Logger LOGGER = LogManager.getLogger();
    ICacheImpl impl;

    public CacheContextModule(IExecutorData executorData) {
        super(executorData, LOGGER);
        impl = new ICacheImpl(executorData);
        // ToDo
        /*try{
            this.executorData.reloadLibraries();
            this.impl.loadCacheFromDisk();
        }
        catch (Exception ex){
            LOGGER.error(ex.getMessage());
        }*/
    }


    @Override
    public long saveContext() throws IExecutorException, TException {
        try {
            return this.impl.saveContext();
        } catch (Exception ex) {
            this.packException(ex);
        }
        return -1;
    }

    @Override
    public void clearContext() throws IExecutorException, TException {
        try {
            this.impl.clearContext();
        } catch (Exception ex) {
            this.packException(ex);
        }
    }

    @Override
    public void loadContext(long id) throws IExecutorException, TException {
        try {
            this.impl.loadContext(id);
        } catch (Exception ex) {
            this.packException(ex);
        }
    }

    @Override
    public void loadContextAsVariable(long id, String name) throws IExecutorException, TException {
        try {
            this.impl.loadContextAsVariable(id, name);
        } catch (Exception ex) {
            this.packException(ex);
        }
    }

    @Override
    public void cache(long id, byte level) throws IExecutorException, TException {
        try {
            this.impl.cache(id, level);
        } catch (Exception ex) {
            this.packException(ex);
        }
    }

    @Override
    public void loadCache(long id) throws IExecutorException, TException {
        try {
            this.impl.loadCache(id);
        } catch (Exception ex) {
            this.packException(ex);
        }
    }
}
