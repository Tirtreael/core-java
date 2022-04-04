package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.io.*;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.storage.header.IEnumHeaders;
import org.ignis.executor.core.storage.header.IHeader;
import org.ignis.executor.core.transport.IZlibTransport;

import java.io.NotSerializableException;
import java.util.Iterator;
import java.util.List;

public abstract class IRawPartition implements IPartition {

    public static final int HEADER = 30;
    private final int headerSize = 0;
    private final TTransport transport;
    private IZlibTransport zlib;
    private int compression;
    private final boolean nativ;
    private int elements = 0;
    private byte nestedType = 0x0;
    private IHeader header;

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

    public void setNestedType(byte nestedType) {
        this.nestedType = nestedType;
    }

    public void setHeader(IHeader header) {
        this.header = header;
    }

    public int getCompression() {
        return compression;
    }

    public boolean isNativ() {
        return nativ;
    }

    public IHeader getHeader() {
        return header;
    }

    public void setCompression(int compression) {
        this.compression = compression;
    }


    IRawPartition(int compression, boolean nativ) throws TException {
        this.compression = compression;
        this.nativ = nativ;
        this.clear();
        this.transport = null;
    }

    IRawPartition(TTransport transport, int compression, boolean nativ) throws TException {
        this.transport = transport;
        this.compression = compression;
        this.nativ = nativ;
        this.clear();
    }

    @Override
    public void read(TTransport transport) throws TException, NotSerializableException {
        IZlibTransport zlib_in = new IZlibTransport(transport);
        int currentElements = this.elements;
        boolean compatible = this.readHeader(transport);
        this.sync();

        if(compatible){
            writeBuf(zlib_in, this.zlib);
        }
        else{
            IWriteIterator writeIterator = this.writeIterator();
            IObjectProtocol protoBuffer = new IObjectProtocol(zlib_in);
            int elementsTmp = currentElements;
            currentElements = this.elements;
            this.elements = elementsTmp;
            while(this.elements < currentElements){
                writeIterator.write((this.header.getElemRead(nestedType)[0]).getRead().read(protoBuffer));
            }

        }

    }

    @Override
    public void write(TTransport transport, int compression, boolean nativ) throws TException {
        this.sync();
        TTransport transportSrc = this.readTransport();
        if(this.nativ == nativ){
            if(this.compression == compression){
                writeBuf(transportSrc, transport);
                transport.flush();
            }
            else{
                IZlibTransport zlib_in = new IZlibTransport(transportSrc);
                IZlibTransport zlib_out = new IZlibTransport(transport, compression);
                writeBuf(zlib_in, zlib_out);
                zlib_out.flush();
            }
        }
        else{
            IZlibTransport zlib_out = new IZlibTransport(transport, compression);
            IObjectProtocol proto_out = new IObjectProtocol(zlib_out);
            proto_out.writeSerialization(nativ);
            if(this.elements > 0){
                if(!nativ){
                    IReadIterator readIterator = this.readIterator();
                    Object first = readIterator.next();
                    WriterType writer = IWriter.getWriterType(this.nestedType);
                    //writer = IWriter.getWriterType(first);
                    IHeader header2 = IEnumHeaders.getInstance().getHeaderTypeById(this.nestedType, false);
                    WriterType writerType = header2.getElemWrite(first)[0];
                    header2.write(proto_out, this.elements);
                    writerType.getWrite().write(proto_out, readIterator.next());
                    while(iterator().hasNext())
                        writerType.getWrite().write(proto_out, readIterator.next());
                }
                else {
                    IHeader header2 = IEnumHeaders.headerNative;
                    WriterType writerType = IEnumHeaders.headerNative.getWrite();
                    header2.write(proto_out, this.elements);
                    IReadIterator readIterator = this.readIterator();
                    while(readIterator.hasNext()){
                        writerType.getWrite().write(proto_out, readIterator.next());
                    }
                }
            }
            else{
                header = IEnumHeaders.getInstance().getHeaderTypeById(this.nestedType, this.nativ);
                header.write(proto_out, 0, this.nestedType);
            }
            zlib_out.flush();
        }
    }

    private void writeBuf(TTransport zlib_in, TTransport zlib_out) throws TTransportException {
        int bb = 1;
        int tambuf = 256;
        byte[] buf = new byte[tambuf];
        while(bb>0) {
            bb = zlib_in.read(buf, 0, tambuf);
            zlib_out.write(buf, 0, bb);
        }
    }

    @Override
    public void write(TTransport transport, int compression) throws TException {
        this.write(transport, compression, false);
    }

    @Override
    public void write(TTransport transport) throws TException {
        this.write(transport, 0, false);
    }

    @Override
    public List<Object> getElements() {
        return null;
    }

    @Override
    public IReadIterator readIterator() throws TException {
        this.sync();
        IZlibTransport zlibIt = new IZlibTransport(this.readTransport());
        IObjectProtocol proto = new IObjectProtocol(zlibIt);
        try {
            proto.readSerialization();
        } catch (TException | NotSerializableException e) {
            e.printStackTrace();
        }
        this.header.read(proto, nestedType);
        return new IRawReadIterator(proto, this);
    }

    @Override
    public IWriteIterator writeIterator() throws TException {
        if(this.headerSize == 0){
            this.writeHeader();
        }
        return null;
    }

    public abstract IPartition clone();

    @Override
    public void copyFrom(IPartition source) {
        //if(source.type() == )
    }

    @Override
    public void moveFrom(IPartition source) throws TException {
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

    public abstract long bytes();

    @Override
    public void clear() throws TException {
        this.elements = 0;
        this.nestedType = IEnumTypes.I_VOID.id;
        this.header = IEnumHeaders.getInstance().getHeaderTypeById(this.nestedType, this.nativ);
    }

    public abstract void fit();

    public abstract String type();

    public void sync() throws TException {
        if (this.getElements().size() > 0)
            this.zlib.flush();

    }

    public abstract TTransport readTransport();


    public boolean readHeader(TTransport transport) throws TException, NotSerializableException {
        IObjectProtocol protocol = new IObjectProtocol(transport);
        boolean nativ = protocol.readSerialization();
        boolean compatible = this.nativ == nativ;
        byte typeId = 0x0;
        IHeader header;
        if(!nativ)
            typeId = IReader.readType(protocol);
        header = IEnumHeaders.getInstance().getHeaderTypeById(typeId, nativ);
        IHeader.ContainedLongType containedLongType = header.read(protocol, typeId);
        long readedElements = containedLongType.noElems;
        byte readedTypeId = containedLongType.typeID[0];

        if(this.elements > 0)
            if(this.nestedType !=readedTypeId && compatible)
                throw new IllegalArgumentException("Ignis serialization does not support heterogeneous basic types");
        else{
            this.header = header;
            this.nestedType = readedTypeId;
        }
        this.elements += readedElements;

        return compatible;
    }

    public abstract void writeHeader() throws TException;

    public byte getNestedType() {
        return nestedType;
    }

    private static class IRawReadIterator implements IReadIterator{
        private final TProtocol protocol;
        private final IRawPartition partition;
        private ReaderType read ;
        private int pos;
        private boolean rInit = false;

        private IRawReadIterator(TProtocol protocol, IRawPartition partition) {
            this.protocol = protocol;
            this.partition = partition;
            this.read = partition.header.getElemRead(partition.nestedType)[0];
        }

        @Override
        public Object next() {
            try {
                this.pos += 1;
                return this.read.getRead().read(protocol);
            } catch (TException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.partition.elements;
        }
    }

    private static class IRawWriteIterator implements IWriteIterator{

        private final TProtocol protocol;
        private final IRawPartition partition;
        private WriterType write;
        private boolean wInit = false;

        public IRawWriteIterator(TProtocol protocol, IRawPartition partition) {
            this.protocol = protocol;
            this.partition = partition;
        }

        public void fastWrite(Object obj) {
            this.partition.elements += 1;
            this.partition.getElements().add(obj);
        }

        @Override
        public void write(Object obj) {
            if(!this.wInit) {
                if (this.partition.nativ) {
                    this.partition.nestedType = 0x0;
                    this.write = this.partition.header.getElemWrite(obj)[0];
                } else {
                    IHeader header = IEnumHeaders.getInstance().getType(obj);
                    this.write = this.partition.header.getElemWrite(obj)[0];
                    if (this.partition.elements > 0 && this.partition.nestedType != IEnumTypes.getInstance().getId(obj)) {
                        throw new IllegalArgumentException("Ignis serialization does not support heterogeneous basic types");
                    }
                    this.partition.header = header;
                }
                this.wInit = true;
            }
            this.fastWrite(obj);
        }
    }


}
