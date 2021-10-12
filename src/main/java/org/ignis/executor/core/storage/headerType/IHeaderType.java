package org.ignis.executor.core.storage.headerType;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.ignis.executor.core.io.ReaderType;
import org.ignis.executor.core.io.WriterType;

public interface IHeaderType {
    ContainedLongType read(TProtocol protocol, byte headerType) throws TException;

    void write(TProtocol protocol, int elems, byte... typeId) throws TException;

    ReaderType[] getElemRead(byte typeId);

    WriterType[] getElemWrite(Object obj);

    class ContainedLongType {
        public long elems;
        public byte[] typeID;

        public ContainedLongType(long elems, byte... typeID) {
            this.elems = elems;
            this.typeID = typeID;
        }
    }

}
