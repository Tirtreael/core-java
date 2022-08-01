package org.ignis.executor.core.transport;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TZlibTransport;

public class IZlibTransport extends TZlibTransport {

    public static final int defaultCompressionLevel = 6;

    private final TTransport transport;
    private final boolean compBuffer;
    private int compressionLevel;
    private boolean rInit;
    private boolean wInit;

    public IZlibTransport(TTransport transport) {
        this(transport, defaultCompressionLevel);
        rInit = false;
        wInit = false;
    }

    public IZlibTransport(TTransport transport, int compressionLevel) {
        super(transport, compressionLevel);
        this.transport = transport;
        this.compressionLevel = compressionLevel;
        this.compBuffer = true;
        rInit = false;
        wInit = false;
    }

    public void reset() {
        this.rInit = false;
        this.wInit = false;
//        this.compressionLevel = this.defaultCompressionLevel;
    }

    @Override
    public void flush() throws TTransportException {
        super.flush();
        this.transport.flush();
    }

    @Override
    public int read(byte[] buf, int off, int len) throws TTransportException {
        if (!this.rInit) {
            this.transport.read(buf, off, 1);
            this.compressionLevel = buf[0];
            this.rInit = transport.peek();
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
            byte[] compBuffer = new byte[]{(byte) compressionLevel};
            this.transport.write(compBuffer, off, 1);
            this.flush();
        }
        if (this.compressionLevel > 0) {
            super.write(buf, off, len);
            this.flush();
        } else {
            this.transport.write(buf, off, len);
            this.flush();
        }
    }

}
