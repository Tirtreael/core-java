package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IExecutorServerModule;

import java.util.Map;
import java.util.Properties;


public class IExecutorServerModuleImpl extends Module implements IExecutorServerModule {

    private static final Logger LOGGER = LogManager.getLogger();
    private TServer server;
    private TMultiplexedProcessor processor;


    public IExecutorServerModuleImpl(IExecutorData executorData) {
        super(executorData, LOGGER);
        this.server = null;
        this.processor = null;
    }


    public void serve(String name, int port, int compression) throws TTransportException {
        if (this.server != null) {
            this.processor = new TMultiplexedProcessor();
            TServerTransport serverTransport = new TServerSocket(port);
            this.server = new IThreadedServer(new TServer.Args(serverTransport));

            this.processor.registerProcessor(name, new org.ignis.rpc.executor.IExecutorServerModule.Processor<>(this));
            LOGGER.info("ServerModule: java executor started");
            this.server.serve();
            LOGGER.info("ServerModule: java executor stopped");
            this.server.stop();
        }
    }

    public void start(Properties properties) {
        //this.getExecutorData().
    }

    @Override
    public void start(Map<String, String> properties) throws TException {
        try {
//            this.executorData.getContext().props()
//            for (Map.Entry<String, String> entry : properties.entrySet()) {
//
//            }

//            MPI.Init()
//            LOGGER.info("ServerModule: Mpi started");

            this.createServices(this.processor);
            LOGGER.info("ServerModule: java executor ready");
        } catch (Exception e) {
            this.packException(e);
        }
    }

    public void stop() {
        try {
//            MPI.Finalize();
            if (this.server != null) {
                TServer aux = this.server;
                this.server = null;
                aux.stop();
            }
        } catch (Exception e) {
            this.packException(e);
        }

    }

    public boolean test() {
        LOGGER.info("ServerModule: test ok");
        return true;
    }

    public void createServices(TProcessor processor) {
    }

    @Override
    public void packException(Exception ex) {

    }
}
