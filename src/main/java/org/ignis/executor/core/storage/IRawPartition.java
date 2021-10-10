package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.io.IEnumTypes;
import org.ignis.executor.core.transport.IZlibTransport;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.Iterator;
import java.util.List;

public class IRawPartition implements IPartition {

    private final int headerSize = 0;
    private TTransport transport;
    private IZlibTransport zlib;
    private int compression;
    private boolean nativ;
    private int elements = 0;
    private byte type;
    private String header;


    IRawPartition(TTransport transport, int compression, boolean nativ) {

    }


    @Override
    public String getTYPE() {
        return null;
    }

    @Override
    public void read(TTransport transport) throws TException, NotSerializableException {

    }

    @Override
    public void write(TTransport transport, int compression, boolean nativ) throws TException {

    }

    @Override
    public void write(TTransport transport, int compression) throws TException {

    }

    @Override
    public void write(TTransport transport) throws TException {

    }

    @Override
    public List<Object> getElements() {
        return null;
    }

    @Override
    public IReadIterator readIterator() {
        return null;
    }

    @Override
    public IWriteIterator writeIterator() {
        return null;
    }

    @Override
    public IPartition clone() {
        return null;
    }

    @Override
    public void copyFrom(IPartition source) {

    }

    @Override
    public void moveFrom(IPartition source) {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator<Object> iterator() {
        return null;
    }

    @Override
    public byte[] toBytes() throws IOException {
        return new byte[0];
    }

    @Override
    public void clear() {
        this.elements = 0;
        this.type = IEnumTypes.I_VOID.id;
//        this.header = IHeader
    }

    @Override
    public void fit() {

    }

    @Override
    public String type() {
        return null;
    }


    class IHeaderAbs {
//        types = Map
    }
}
