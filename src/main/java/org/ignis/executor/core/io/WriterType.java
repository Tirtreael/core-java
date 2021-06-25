package org.ignis.executor.core.io;


import org.apache.thrift.protocol.TProtocol;

import java.util.function.BiConsumer;

public record WriterType(BiConsumer<TProtocol, Object> write) {


    public BiConsumer<TProtocol, Object> getWrite() {
        return write;
    }
}
