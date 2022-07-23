package org.ignis.executor.core.protocol;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.core.io.INativeReader;
import org.ignis.executor.core.io.INativeWriter;
import org.ignis.executor.core.io.IReader;
import org.ignis.executor.core.io.IWriter;

import java.io.NotSerializableException;
import java.util.Collection;
import java.util.List;

//@Todo: add all methods
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
        if (nativ) {
            boolean header = readBool();
            if (header) {
                long elements = IReader.readSize(this);
                return INativeReader.read(this, elements);
            } else {
                return INativeReader.read(this);
            }
        } else {
            return IReader.read(this);
        }
    }

    public void writeObject(Object obj, boolean nativ, boolean listHeader) throws TException {
        this.writeSerialization(nativ);
        if (nativ) {
            this.writeBool(obj instanceof Collection && listHeader);
            if (obj instanceof Collection && listHeader) {
                IWriter.writeSize(this, ((Collection<?>) obj).size());
//                for (Object element : (Collection<?>) obj) {
                INativeWriter.write(this, obj);
//                }
            } else {
                INativeWriter.write(this, List.of(obj));
            }
        } else {
            IWriter.write(this, obj);
        }
    }


    public boolean readSerialization() throws TException, NotSerializableException {
//        this.readByte();
        byte id = this.readByte();
        if (id == IGNIS_PROTOCOL)
            return false;
        else if (id == JAVA_PROTOCOL)
            return true;
        else throw new NotSerializableException("Serialization " + id + " is not compatible with Java");
    }

    public void writeSerialization(boolean nativ) throws TException {
        if (nativ)
            this.writeByte(IObjectProtocol.JAVA_PROTOCOL);
        else this.writeByte(IObjectProtocol.IGNIS_PROTOCOL);
    }


}
