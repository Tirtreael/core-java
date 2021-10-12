package org.ignis.executor.core.storage.headerType;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.ignis.executor.core.io.*;

public class IHeaderTypeList implements IHeaderType {

    @Override
    public ContainedLongType read(TProtocol protocol, byte headerType) throws TException {
        if (headerType == IEnumTypes.I_VOID.id)
            headerType = IReader.readType(protocol);
        long elems = IReader.readSize(protocol);
        return new ContainedLongType(elems, IReader.readType(protocol));
    }

    @Override
    public void write(TProtocol protocol, int elems, byte... typeId) throws TException {
        IWriter.writeType(protocol, IEnumTypes.I_LIST.id);
        IWriter.writeSize(protocol, elems);
        IWriter.writeType(protocol, typeId[0]);
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