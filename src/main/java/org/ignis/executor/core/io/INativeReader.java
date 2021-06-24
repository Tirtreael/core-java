package org.ignis.executor.core.io;

import org.apache.thrift.protocol.TProtocol;


public class INativeReader {

    public byte[] read(TProtocol protocol) {
        return protocol.getTransport().getBuffer();
    }
}
