package org.ignis.executor.core.modules.impl;

import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IExecutorServerModule;
import org.ignis.executor.core.modules.Module;
import org.slf4j.Logger;

import java.util.Properties;


public class IExecutorServerModuleImpl extends Module implements IExecutorServerModule {

    private TServer server;
    private TProcessor processor;


    public IExecutorServerModuleImpl(IExecutorData executorData, Logger logger) {
        super(executorData, logger);
    }


    public void serve(String name, int port, int compression) throws TTransportException {
        if(this.server!=null){

//            this.processor = new TMultiplexedProcessor();
//            this.server = new IThreadedServer(this.processor, new TServerSocket(port), TCompactProtocol(),
//                    IZlibTransportFactory(compression),
//                    new TCompactProtocol());
//
//            self.__processor.registerProcessor(name, IExecutorServerModuleProcessor(self))
//            logger.info("ServerModule: python executor started")
//            self.__server.serve()
//            logger.info("ServerModule: python executor stopped")
//            self.__server.stop()
        }

    }

    public void start(Properties properties) {

    }

    public void stop() {

    }

    public boolean test() {
        return false;
    }
}
