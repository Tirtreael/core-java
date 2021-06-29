package org.ignis.executor.core.storage;


import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.transport.IZlibTransport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IMemoryPartition implements IPartition {

    public static final String TYPE = "Memory";

    public List<Object> elements;

    public IMemoryPartition(int nElements) {
        elements = new ArrayList<>(nElements);
    }

    public Iterator<Object> iterator() {
        return elements.iterator();
    }

    public IMemoryPartition clone() {
        IMemoryPartition newPartition = new IMemoryPartition(this.elements.size());
        this.copyTo(newPartition);
        return newPartition;
    }

    //@Todo check
    public void read(TTransport transport) throws TException, NotSerializableException {
        IZlibTransport trans = new IZlibTransport(transport);
        IObjectProtocol proto = new IObjectProtocol(trans);
        Object elems = proto.readObject();
        elements.add(elems);
    }

    @Override
    public void write(TTransport transport, int compression) {

    }

    @Override
    public void write(TTransport transport) {

    }

    @Override
    public void readIterator(IPartition partition) {

    }

    @Override
    public void writeIterator(IPartition partition) {

    }

    @Override
    public void copyFrom(IPartition source) {
        if (source instanceof IMemoryPartition &&
                ((IMemoryPartition) source).elements.getClass() == this.elements.getClass()) {
            this.elements.addAll(((IMemoryPartition) source).elements);
        } else {

        }
    }

    @Override
    public void moveFrom(IPartition source) {

    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);

        for (Object element : this.elements) {
            out.writeObject(element);
        }
        return baos.toByteArray();
    }

    @Override
    public void clear() {

    }

    @Override
    public void fit() {

    }

    @Override
    public Type type() {
        return null;
    }

    public void write(TTransport transport, int compression, boolean nativ) throws TException {
        IZlibTransport trans = new IZlibTransport(transport, compression);
        IObjectProtocol proto = new IObjectProtocol(transport);
        proto.writeObject(elements, nativ);
        trans.flush();
    }


}
