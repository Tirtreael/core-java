package org.ignis.executor.core.storage.header;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.ignis.executor.core.io.ReaderType;
import org.ignis.executor.core.io.WriterType;

public abstract class IHeader {

    public final byte id;
    public final Class<?> type;
    private WriterType write;

    public IHeader(byte id, Class<?> type) {
        this.id = id;
        this.type = type;
    }

    public WriterType getWrite() {
        return write;
    }

    protected IHeader(byte id, Class<?> type, WriterType write) {
        this.id = id;
        this.type = type;
        this.write = write;
    }

    public abstract ContainedLongType read(TProtocol protocol, byte headerType) throws TException;

    public abstract void write(TProtocol protocol, int elems, byte... typeId) throws TException;

    public abstract ReaderType[] getElemRead(byte typeId);

    public abstract WriterType[] getElemWrite(Object obj);

    public static class ContainedLongType {
        public long noElems;
        public byte[] typeID;

        public ContainedLongType(long noElems, byte... typeID) {
            this.noElems = noElems;
            this.typeID = typeID;
        }
    }

}
