package org.ignis.executor.core.io;


import org.apache.thrift.protocol.TProtocol;

import java.util.function.BiConsumer;

public class WriterType<T> {

    public final BiConsumer<TProtocol, T> write;

    public WriterType (BiConsumer<TProtocol, T> write) {
        this.write = write;
    }


    public BiConsumer<TProtocol, T> getWrite() {
        return write;
    }
}
