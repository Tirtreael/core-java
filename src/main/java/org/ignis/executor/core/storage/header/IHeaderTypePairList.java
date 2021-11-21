package org.ignis.executor.core.storage.header;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.ignis.executor.core.io.*;

public class IHeaderTypePairList extends IHeader {

    protected IHeaderTypePairList(byte id, Class<?> type) {
        super(id, type);
    }

    @Override
    public ContainedLongType read(TProtocol protocol, byte headerType) throws TException {
        if (headerType == IEnumTypes.I_VOID.id)
            headerType = IReader.readType(protocol);
        long elems = IReader.readSize(protocol);
        return new ContainedLongType(elems, IReader.readType(protocol), IReader.readType(protocol));
    }

    @Override
    public void write(TProtocol protocol, int elems, byte... typeId) throws TException {
        IWriter.writeType(protocol, IEnumTypes.I_PAIR_LIST.id);
        IWriter.writeSize(protocol, elems);
        IWriter.writeType(protocol, typeId[0]);
        IWriter.writeType(protocol, typeId[1]);
    }


    public ReaderType[] getElemRead(byte typeId) {
        ReaderType r1 = IReader.getReaderType(typeId);
        ReaderType r2 = IReader.getReaderType(typeId);
        return new ReaderType[]{r1, r2};
    }

    @Override
    public WriterType[] getElemWrite(Object obj) {
        WriterType w1 = IWriter.getWriterType(obj);
        WriterType w2 = IWriter.getWriterType(obj);
        return new WriterType[]{w1, w2};
    }


}