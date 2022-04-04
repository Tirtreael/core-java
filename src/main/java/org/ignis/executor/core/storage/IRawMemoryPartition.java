package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.core.protocol.IObjectProtocol;

// @ToDo Add IMemoryBuffer
public class IRawMemoryPartition extends IRawPartition{

    public static final String TYPE = "Memory";

    IRawMemoryPartition(long bytes, int compression, boolean nativ) throws TException {
        super(new TMemoryBuffer((int) bytes + IRawPartition.HEADER), compression, nativ);
        this.clear();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public IPartition clone() {
        try {
            IPartition newPartition = new IRawMemoryPartition(this.bytes(), this.getCompression(), this.isNativ());
            this.copyTo(newPartition);
            return newPartition;
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void copyTo(IPartition target) {
        super.copyTo(target);
    }

    @Override
    public void moveTo(IPartition source, IPartition target) {
        super.moveTo(source, target);
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public long bytes() {
        int size = this.getTransport().getBuffer().length - IRawPartition.HEADER + this.getHeaderSize();
        return Math.max(size, 0);
    }

    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public void clear() throws TException {
        super.clear();
        try {
            this.sync();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fit() {
        // Add IMemoryBuffer
    }

    @Override
    public String type() {
        return TYPE;
    }

    public byte[] getBuffer(){
        return this.getTransport().getBuffer();
    }

    @Override
    public void sync() throws TException {
        super.sync();
        this.writeHeader();
    }

    @Override
    public TTransport readTransport() {
//        int init = IRawPartition.HEADER - this.getHeaderSize();
//        TMemoryBuffer rBuffer = TMemoryBuffer(this.getTransport().getBuffer().length);
//        rbuffer.
        return null; // rbuffer;
    }

    @Override
    public void writeHeader() throws TException {
        IObjectProtocol proto = new IObjectProtocol(this.getZlib());
        this.getZlib().reset();
        //int writePosition = this.getTransport().writeEnd();
        this.getTransport().flush();

        proto.writeSerialization(this.isNativ());
        this.getHeader().write(proto, this.getElements().size(), this.getNestedType());
        this.getZlib().flush();
        // Align header to the left
//        this.getHeaderSize() = this.getTransport().writeEnd();
//        header = this.getTransport().readAll(buf,0,this.getHeaderSize())
//        this.getTransport().setReadBuffer(0);
//        this.getTransport().setWriteBuffer(IRawPartition.HEADER - this.getHeaderSize());
//        this.getTransport().write(header);
//        this.getTransport().setWriteBuffer(Math.max(writePosition, IRawPartition.HEADER));
    }
}
