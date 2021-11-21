package org.ignis.executor.core.storage.header;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.ignis.executor.core.io.ReaderType;
import org.ignis.executor.core.io.WriterType;

public abstract class IHeader {

    public final byte id;
    public final Class<?> type;

    protected IHeader(byte id, Class<?> type) {
        this.id = id;
        this.type = type;
    }

    abstract ContainedLongType read(TProtocol protocol, byte headerType) throws TException;

    abstract void write(TProtocol protocol, int elems, byte... typeId) throws TException;

    abstract ReaderType[] getElemRead(byte typeId);

    abstract WriterType[] getElemWrite(Object obj);

    static class ContainedLongType {
        public long elems;
        public byte[] typeID;

        public ContainedLongType(long elems, byte... typeID) {
            this.elems = elems;
            this.typeID = typeID;
        }
    }

}
