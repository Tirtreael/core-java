package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;

public interface IReader {
    byte readType(TProtocol protocol) throws TException;

}
