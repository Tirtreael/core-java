package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.io.IEnumTypes;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.transport.IZlibTransport;

import java.io.NotSerializableException;
import java.util.Iterator;
import java.util.List;

public abstract class IRawPartition implements IPartition {

    private final int headerSize = 0;
    private final TTransport transport;
    private IZlibTransport zlib;

    public int getHeaderSize() {
        return headerSize;
    }

    public TTransport getTransport() {
        return transport;
    }

    public IZlibTransport getZlib() {
        return zlib;
    }

    public void setZlib(IZlibTransport zlib) {
        this.zlib = zlib;
    }

    public void setElements(int elements) {
        this.elements = elements;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getCompression() {
        return compression;
    }

    public boolean isNativ() {
        return nativ;
    }

    public String getHeader() {
        return header;
    }

    public void setCompression(int compression) {
        this.compression = compression;
    }

    private int compression;
    private final boolean nativ;
    private int elements = 0;
    private byte type = 0x0;
    private String header;

    IRawPartition(int compression, boolean nativ) {
        this.compression = compression;
        this.nativ = nativ;
        this.clear();
        this.transport = null;
    }

    IRawPartition(TTransport transport, int compression, boolean nativ) {
        this.transport = transport;
        this.compression = compression;
        this.nativ = nativ;
        this.clear();
    }


    @Override
    public String getType() {
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

    public abstract IPartition clone();

    @Override
    public void copyFrom(IPartition source) {

    }

    @Override
    public void moveFrom(IPartition source) {
        this.copyFrom(source);
        source.clear();
    }

    @Override
    public int size() {
        return this.elements;
    }

    @Override
    public Iterator<Object> iterator() {
        return null;
    }

    public abstract byte[] toBytes();

    @Override
    public void clear() {
        this.elements = 0;
        this.type = IEnumTypes.I_VOID.id;
//        this.header = IEnumHeaders
    }

    public abstract void fit();

    public abstract String type();

    public void sync() throws TTransportException {
        if (this.getElements().size() > 0)
            this.zlib.flush();

    }

    public abstract TTransport readTransport();


    public Object readHeader(TTransport transport) throws TException, NotSerializableException {
        IObjectProtocol protocol = new IObjectProtocol(transport);
        boolean nativ = protocol.readSerialization();
        boolean compatible = this.nativ == nativ;
//        IHeader = IEnumHeaders


        return null;
    }

    public abstract void writeHeader();


}
