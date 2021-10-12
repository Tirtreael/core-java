package org.ignis.executor.core.io;


import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;


public class WriterType {
    private final Function<TProtocol, Object> write;


    public WriterType(Function<TProtocol, Object> write) {
        this.write = write;
    }

    public Function<TProtocol, Object> getWrite() {
        return write;
    }


    public interface Function<T1, T2> {
        void write(T1 protocol, T2 object) throws TException;
    }


}
