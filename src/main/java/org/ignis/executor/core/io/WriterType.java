package org.ignis.executor.core.io;


import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;


public class WriterType {
    private final CheckedFunction<TProtocol, Object> write;


    public WriterType(CheckedFunction<TProtocol, Object> write) {
        this.write = write;
    }

    public CheckedFunction<TProtocol, Object> getWrite() {
        return write;
    }


    @FunctionalInterface
    public interface CheckedFunction<T1, T2> {
        void apply(T1 protocol, T2 object) throws TException;
    }


}
