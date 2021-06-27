package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;


public class ReaderType {

    private final CheckedFunction<Object> read;

    public ReaderType(CheckedFunction<Object> read) {
        this.read = read;
    }

    public CheckedFunction<Object> getRead() {
        return read;
    }

    @FunctionalInterface
    public interface CheckedFunction<R> {
        R apply(TProtocol protocol) throws TException;
    }


}
