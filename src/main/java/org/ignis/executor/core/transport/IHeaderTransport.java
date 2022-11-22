package org.ignis.executor.core.transport;

import org.apache.thrift.TConfiguration;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;


public class IHeaderTransport extends TTransport {

    private final TTransport transport;
    private String header;
    private int position = 0;


    public IHeaderTransport(TTransport transport, String header) {
        this.transport = transport;
        this.header = header;
    }

    @Override
    public boolean isOpen() {
        return true;
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
    public int read(byte[] buf, int off, int len) throws TTransportException {
        if (header.isEmpty())
            return transport.readAll(buf, off, len);
        int bytes = Integer.min(header.length() - position, len);
        header += transport.readAll(buf, off, bytes);
        position += bytes;
        if (position == header.length())
            header = "";
        return bytes;
    }

    @Override
    public void write(byte[] buf, int off, int len) throws TTransportException {
        transport.write(buf, off, len);
    }

    @Override
    public TConfiguration getConfiguration() {
        return null;
    }

    @Override
    public void updateKnownMessageSize(long size) throws TTransportException {

    }

    @Override
    public void checkReadBytesAvailable(long numBytes) throws TTransportException {

    }
}
