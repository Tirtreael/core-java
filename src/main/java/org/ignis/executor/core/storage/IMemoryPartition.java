package org.ignis.executor.core.storage;


import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.transport.IZlibTransport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
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
        /*if (elems.getClass() == elements.get(0).getClass()) {
            elements.add(elems);
        } else {
            elements.add(elems);
        }*/
    }

    @Override
    public void write(TTransport transport, int compression, boolean nativ) throws TException {
        IZlibTransport trans = new IZlibTransport(transport, compression);
        IObjectProtocol proto = new IObjectProtocol(trans);
        proto.writeObject(elements, nativ, true);
    }

    @Override
    public void write(TTransport transport, int compression) throws TException {
        write(transport, compression, false);

    }

    @Override
    public void write(TTransport transport) throws TException {
        write(transport, 0, false);
    }

    @Override
    public List<Object> getElements() {
        return this.elements;
    }

    //@Todo
    @Override
    public void readIterator(IPartition partition) {

    }

    //@Todo
    @Override
    public void writeIterator(IPartition partition) {

    }

    @Override
    public void copyFrom(IPartition source) {
        if (source instanceof IMemoryPartition &&
                ((IMemoryPartition) source).elements.getClass() == this.elements.getClass()) {
            this.elements.addAll(((IMemoryPartition) source).elements);
        } else {
            elements.addAll(source.getElements());
        }
    }

    @Override
    public void moveFrom(IPartition source) {
        if (source instanceof IMemoryPartition && elements.size() == 0 &&
                ((IMemoryPartition) source).elements.getClass() == this.elements.getClass()) {
            List<Object> elementsTmp = this.elements;
            this.elements = ((IMemoryPartition) source).elements;
            ((IMemoryPartition) source).elements = elementsTmp;
        } else {
            this.copyFrom(source);
            source.clear();
        }
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
        this.elements.clear();
    }

    //@Todo
    @Override
    public void fit() {

    }

    @Override
    public String type() {
        return IMemoryPartition.TYPE;
    }


}
