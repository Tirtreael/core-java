package org.ignis.executor.core.modules.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IExecutorServerModule;
import org.ignis.executor.core.transport.IZlibTransportFactory;
import org.ignis.mpi.Mpi;
import org.ignis.rpc.IExecutorException;

import java.util.Map;


public class IExecutorServerModuleImpl extends Module implements IExecutorServerModule {

    private static final Logger LOGGER = LogManager.getLogger();
    private IThreadedServer server;
    private TMultiplexedProcessor processor;


    public IExecutorServerModuleImpl(IExecutorData executorData) {
        super(executorData, LOGGER);
        this.server = null;
        this.processor = null;
    }


    public void serve(String name, int port, int compression) throws TTransportException {
        if (this.server == null) {
            this.processor = new TMultiplexedProcessor();
            TServerSocket serverTransport = new TServerSocket(port);
            TServer.Args args = new IThreadedServer.Args(serverTransport)
                    .processor(this.processor)
                    .transportFactory(new IZlibTransportFactory(compression))
                    .protocolFactory(new TCompactProtocol.Factory());

//            this.server = new IThreadedServer(new IThreadedServer.Args(serverTransport).processor(new org.ignis.rpc.executor.IExecutorServerModule.Processor<>(this)));
            this.server = new IThreadedServer(args);

            this.processor.registerProcessor(name, new org.ignis.rpc.executor.IExecutorServerModule.Processor<>(this));
            LOGGER.info("ServerModule: java executor started");
            this.server.serve();
            LOGGER.info("ServerModule: java executor stopped");
            this.server.stop();
        }
    }

    @Override
    public void start(Map<String, String> properties, Map<String, String> env) throws IExecutorException, TException {
        try {
            this.executorData.getContext().props().getProperties().putAll(properties);

//            Mpi.MPI_Init();
//            MPI.Init();
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
}
