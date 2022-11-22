package org.ignis.executor.core.modules.impl;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class IThreadedServer extends TServer {

    org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Module.class);

    private boolean stop;
    private final List<TTransport> clients;

    public IThreadedServer(AbstractServerArgs args) {
        super(args);
        this.stop = false;
        this.clients = new ArrayList<>();
    }

    @Override
    public void serve() {
        this.stop = false;
        try {
            this.serverTransport_.listen();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        try {
            while (!this.stop) {
                try {
                    TTransport client = this.serverTransport_.accept();
                    this.clients.add(client);
                    if (client == null) {
                        continue;
                    }
                    Thread t = new Thread(() -> {
                        try {
                            this.handle(client);
                        } catch (TTransportException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    t.setDaemon(true);
                    t.start();
                } catch (TTransportException e) {
                    e.printStackTrace();
                }
            }

        } finally {
            this.stop();
            this.serverTransport_.close();
        }
    }

    private void handle(TTransport client) throws TTransportException {
        TTransport trans = this.inputTransportFactory_.getTransport(client);
        TProtocol prot = this.inputProtocolFactory_.getProtocol(trans);

        try {
            while (!this.stop) {
                super.processorFactory_.getProcessor(trans).process(prot, prot);
            }
        } catch (Exception e) {
            if (!this.stop) {
                LOGGER.info(e.getMessage(), e);
            }
        }
        trans.close();
    }

    @Override
    public void stop() {
        for (TTransport client : this.clients) {
            this.stop = true;
            client.close();
        }
    }

}
