package org.ignis.executor.core.transport;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


public class IHeaderTransport extends TTransport {

    private final TTransport transport;
    private final String header;
    private final int position = 0;


    public IHeaderTransport(TTransport transport, String header) {
        this.transport = transport;
        this.header = header;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void open() throws TTransportException {
        this.transport.open();
    }

    @Override
    public void close() {
        this.transport.close();
    }

    @Override
    //@Todo check
    public int read(byte[] buf, int off, int len) throws TTransportException {
        return transport.readAll(buf, off, len);
    }

    @Override
    public void write(byte[] buf, int off, int len) throws TTransportException {
        transport.readAll(buf, off, len);
    }
}
