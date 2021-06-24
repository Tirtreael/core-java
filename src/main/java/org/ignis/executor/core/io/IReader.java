package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IReader {
    byte readTypeAux(TProtocol protocol) throws TException;

    ReaderType<?>[] getReaders();

    ReaderType<?> getReaderType(int typeId);

    long readSizeAux(TProtocol protocol) throws TException;

    List<Object> readList(TProtocol protocol) throws Exception;

    Set<Object> readSet(TProtocol protocol) throws Exception;

    Map<Object, Object> readMap(TProtocol protocol) throws Exception;

    AbstractMap.SimpleEntry<Object, Object> readPair(TProtocol protocol) throws Exception;

    byte[] readBinary(TProtocol protocol) throws Exception;

    List<AbstractMap.SimpleEntry<Object, Object>> readPairList(TProtocol protocol) throws Exception;
}
