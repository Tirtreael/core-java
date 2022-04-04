package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;
import org.ignis.executor.core.io.IEnumTypes;
import org.ignis.executor.core.io.IReader;
import org.ignis.executor.core.io.ReaderType;
import org.ignis.executor.core.io.WriterType;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.storage.header.IEnumHeaders;
import org.ignis.executor.core.storage.header.IHeader;
import org.ignis.executor.core.transport.IZlibTransport;

import java.io.NotSerializableException;
import java.util.Iterator;
import java.util.List;

public abstract class IRawPartition implements IPartition {

    private final int headerSize = 0;
    private final TTransport transport;
    private IZlibTransport zlib;
    private int compression;
    private final boolean nativ;
    private int elements = 0;
    private byte type = 0x0;
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

    public void setType(byte type) {
        this.type = type;
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
        IZlibTransport zlib_in = new IZlibTransport(transport);
        int currentElements = this.elements;
        boolean compatible = this.readHeader(transport);
        this.sync();

        if(compatible){
            int bb = 1;
            int tambuf = 256;
            byte[] buf = new byte[tambuf];
            while(bb>0){
                bb = zlib_in.read(buf,0,tambuf);
                this.zlib.write(buf, 0, bb);
            }
        }
        else{
            IWriteIterator writeIterator = this.writeIterator();
        }

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
        if(this.headerSize == 0){
            this.writeHeader();
        }
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
            if(this.type !=readedTypeId && compatible)
                throw new IllegalArgumentException("Ignis serialization does not support heterogeneous basic types");
        else{
            this.header = header;
            this.type = readedTypeId;
        }
        this.elements += readedElements;

        return compatible;
    }

    public abstract void writeHeader();

    private static class IRawReadIterator implements IReadIterator{
        private final TProtocol protocol;
        private final IRawPartition partition;
        private ReaderType read ;
        private int pos;
        private boolean rInit = false;

        private IRawReadIterator(TProtocol protocol, IRawPartition partition, ReaderType read) {
            this.protocol = protocol;
            this.partition = partition;
            this.read = read = partition.header.getElemRead(partition.type)[0];
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
                    this.partition.type = 0x0;
                    this.write = this.partition.header.getElemWrite(obj)[0];
                } else {
                    IHeader header = IEnumHeaders.getInstance().getType(obj);
                    this.write = this.partition.header.getElemWrite(obj)[0];
                    if (this.partition.elements > 0 && this.partition.type != IEnumTypes.getInstance().getId(obj)) {
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
