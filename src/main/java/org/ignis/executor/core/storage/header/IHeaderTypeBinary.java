package org.ignis.executor.core.storage.header;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.ignis.executor.core.io.IReader;
import org.ignis.executor.core.io.IWriter;
import org.ignis.executor.core.io.ReaderType;
import org.ignis.executor.core.io.WriterType;

public class IHeaderTypeBinary extends IHeader {

    protected IHeaderTypeBinary(byte id, Class<?> type) {
        super(id, type);
    }

    @Override
    public ContainedLongType read(TProtocol protocol, byte headerType) throws TException {
        return null;
    }

    @Override
    public void write(TProtocol protocol, int elems, byte... typeId) throws TException {

    }

    @Override
    public ReaderType[] getElemRead(byte typeId) {
        return new ReaderType[]{IReader.getReaderType(typeId)};
    }

    @Override
    public WriterType[] getElemWrite(Object obj) {
        return new WriterType[]{IWriter.getWriterType(obj)};
    }

}