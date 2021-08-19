package org.ignis.executor.core.transport;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TZlibTransport;

public class IZlibTransport extends TZlibTransport {

    private final TTransport transport;

    private boolean compBuffer;
    private int defaultCompressionLevel;
    private int compressionLevel;
    private boolean rInit = false;
    private boolean wInit = false;

    public IZlibTransport(TTransport transport) {
        super(transport);
        this.transport = transport;
        this.defaultCompressionLevel = this.compressionLevel;
    }

    public IZlibTransport(TTransport transport, int compressionLevel) {
        super(transport, compressionLevel);
        this.transport = transport;
        this.defaultCompressionLevel = this.compressionLevel;
        this.compBuffer = true;
    }

    public void reset() {
        this.rInit = false;
        this.wInit = false;
        this.compressionLevel = this.defaultCompressionLevel;
    }

    @Override
    public void flush() throws TTransportException {
        super.flush();
        this.transport.flush();
    }

    @Override
    public int read(byte[] buf, int off, int len) throws TTransportException {
        if (!this.rInit) {
            //this.transport.readAll(buf, off, 1);
            this.rInit = true;
        }
        if (this.compressionLevel > 0) {
            return super.read(buf, off, len);
        } else {
            return this.transport.read(buf, off, len);
        }
    }
    
    @Override
    public void write(byte[] buf, int off, int len) throws TTransportException {
        if (!this.wInit) {
            this.wInit = true;
            this.transport.write(buf, off, len);
        }
        if (this.compressionLevel > 0) {
            super.write(buf, off, len);
            if(this.getBufferPosition() > getBuffer().length) {
                this.flush();
            }
        }
        else{
            this.transport.write(buf, off, len);
        }
    }
    
}
