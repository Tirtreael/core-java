package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;


public class ReaderType {

    public final Function<Object> read;

    public ReaderType(Function<Object> read) {
        this.read = read;
    }

    public Function<Object> getRead() {
        return read;
    }

    public interface Function<R> {
        R read(TProtocol protocol) throws TException;
    }


}
