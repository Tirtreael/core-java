package org.ignis.executor.core.storage.header;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.ignis.executor.core.io.*;

public class IHeaderTypeNative extends IHeader {

    protected IHeaderTypeNative(byte id, Class<?> type) {
        super(id, type);
    }

    @Override
    public ContainedLongType read(TProtocol protocol, byte headerType) throws TException {
        protocol.readBool();
        return new ContainedLongType(IReader.readSize(protocol), (byte) 0x0);
    }

    @Override
    public void write(TProtocol protocol, int elems, byte... typeId) throws TException {
        protocol.writeBool(true);
        IWriter.writeSize(protocol, elems);
    }

    @Override
    public ReaderType[] getElemRead(byte typeId) {
        return new ReaderType[]{INativeReader.read};
    }

    @Override
    public WriterType[] getElemWrite(Object obj) {
        return new WriterType[]{INativeWriter.write};
    }


}