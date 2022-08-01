package org.ignis.executor.core.transport;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.io.InputStream;

public class StreamTransport extends InputStream {

    final TTransport transport;

    public StreamTransport(TTransport transport) {
        this.transport = transport;
    }

    @Override
    public int read() throws IOException {
        byte[] bytes = new byte[1];
        try {
            transport.read(bytes, 0, 1);
        } catch (TTransportException e) {
            throw new IOException(e);
        }
        return bytes[0];
    }

    @Override
    public int read(byte[] b) throws IOException {
        try {
            return transport.read(b, 0, b.length);
        } catch (TTransportException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        try {
            return transport.read(b, off, len);
        } catch (TTransportException e) {
            throw new IOException(e);
        }
    }
}
