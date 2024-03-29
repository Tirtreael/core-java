package org.ignis.executor.core.storage;


import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.transport.IZlibTransport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class IMemoryPartition implements IPartition {

    public static final String TYPE = "Memory";

    private List<Object> elements;

    public IMemoryPartition() {
        elements = new ArrayList<>();
    }

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

    @Override
    public String getType() {
        return IMemoryPartition.TYPE;
    }

//    @Override
//    public long bytes() {
//        return this.toBytes().length;
//    }

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

    public void readAll(TTransport transport) throws TException, NotSerializableException {
        IZlibTransport trans = new IZlibTransport(transport);
        IObjectProtocol proto = new IObjectProtocol(trans);
        Object elems = proto.readObject();
        if (elems instanceof Collection)
            elements.addAll((Collection<?>) elems);
        else elements.add(elems);
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
        write(transport, IZlibTransport.defaultCompressionLevel, false);
    }

    public List<Object> getElements() {
        return this.elements;
    }

    public void setElements(List<Object> elements) {
        this.elements = elements;
    }

    @Override
    public IMemoryReadIterator readIterator() {
        return new IMemoryReadIterator(this.getElements());
    }

    @Override
    public IMemoryWriteIterator writeIterator() {
        return new IMemoryWriteIterator(this.getElements());
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
    public void moveFrom(IPartition source) throws TException {
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
    public byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(baos);
            for (Object element : this.elements) {
                out.writeObject(element);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    @Override
    public void clear() {
        this.elements.clear();
    }

    @Override
    public void fit() {
        ((ArrayList<Object>) this.elements).trimToSize();
    }

    @Override
    public String type() {
        return IMemoryPartition.TYPE;
    }


    public static class IMemoryReadIterator implements IReadIterator {

        List<Object> elements;
        int pos = 0;

        public IMemoryReadIterator(List<Object> elements) {
            this.elements = elements;
        }

        @Override
        public Object next() {
            int pos0 = this.pos;
            this.pos++;
            return this.elements.get(pos0);
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.elements.size();
        }
    }


    public static class IMemoryWriteIterator implements IWriteIterator {

        List<Object> elements;

        IMemoryWriteIterator(List<Object> elements) {
            this.elements = elements;
        }

        @Override
        public void write(Object obj) {
            this.elements.add(obj);
        }
    }
}
