package org.ignis.executor.core.io;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;

public interface INativeReader {

    static byte[] read(TProtocol protocol, int size) {
        byte[] data = new byte[size];
        try {
            protocol.getTransport().readAll(data, 0, size);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return data;
    }
}
