package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.ignis.executor.core.protocol.IObjectProtocol;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IReader {
    byte readTypeAux(IObjectProtocol protocol) throws TException;

    ReaderType<?>[] getReaders();

    ReaderType<?> getReaderType(int typeId);

    long readSizeAux(IObjectProtocol protocol) throws TException;

    List<Object> readList(IObjectProtocol protocol) throws Exception;

    Set<Object> readSet(IObjectProtocol protocol) throws Exception;

    Map<Object, Object> readMap(IObjectProtocol protocol) throws Exception;

    AbstractMap.SimpleEntry<Object, Object> readPair(IObjectProtocol protocol) throws Exception;

    byte[] readBinary(IObjectProtocol protocol) throws Exception;

    List<AbstractMap.SimpleEntry<Object, Object>> readPairList(IObjectProtocol protocol) throws Exception;
}
