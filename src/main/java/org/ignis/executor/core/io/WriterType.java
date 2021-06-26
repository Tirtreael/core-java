package org.ignis.executor.core.io;


import org.apache.thrift.protocol.TProtocol;

import java.util.function.BiConsumer;

public class WriterType {
    private final BiConsumer<TProtocol, Object> write;

    public WriterType(BiConsumer<TProtocol, Object> write) {
        this.write = write;
    }


    public BiConsumer<TProtocol, Object> getWrite() {
        return write;
    }

    public BiConsumer<TProtocol, Object> write() {
        return write;
    }


}
