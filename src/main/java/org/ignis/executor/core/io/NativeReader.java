package org.ignis.executor.core.io;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class NativeReader {

    private final TTransport transport;

    public NativeReader(TTransport transport) {
        this.transport = transport;
    }

    public byte[] read(int size) {
        byte[] data = new byte[size];
        try {
            this.transport.readAll(data, 0, size);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return data;
    }
}
