package org.ignis.executor.core.protocol;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.core.io.INativeReader;
import org.ignis.executor.core.io.INativeWriter;
import org.ignis.executor.core.io.IReader;
import org.ignis.executor.core.io.IWriter;

import java.io.NotSerializableException;

public class IObjectProtocol extends TCompactProtocol {

    public static final byte IGNIS_PROTOCOL = 0;

    public static final byte JAVA_PROTOCOL = 4;


    public IObjectProtocol(TTransport transport, long stringLengthLimit, long containerLengthLimit) {
        super(transport, stringLengthLimit, containerLengthLimit);
    }

    public IObjectProtocol(TTransport transport) {
        super(transport);
    }


    public Object readObject() throws TException, NotSerializableException {
        boolean nativ = this.readSerialization();
        long elements = IReader.readSize(this);
        if (nativ) {
            return INativeReader.read(this, (int) elements);
        } else {
            return IReader.read(this);
        }
    }


    public void writeObject(Object obj, boolean nativ) throws TException {
        this.writeSerialization(nativ);
        if (nativ) {
            INativeWriter.write(this, obj);
        } else {
            IWriter.write(this, obj);
        }
    }


    public boolean readSerialization() throws TException, NotSerializableException {
        byte id = this.readByte();
        if (id == IGNIS_PROTOCOL)
            return false;
        else if (id == JAVA_PROTOCOL)
            return true;
        else throw new NotSerializableException("Serialization is not compatible with Java");
    }

    public void writeSerialization(boolean nativ) throws TException {
        if (nativ)
            this.writeByte(IObjectProtocol.JAVA_PROTOCOL);
        else this.writeByte(IObjectProtocol.IGNIS_PROTOCOL);
    }


}
