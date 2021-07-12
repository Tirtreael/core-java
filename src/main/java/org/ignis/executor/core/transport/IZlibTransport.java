package org.ignis.executor.core.transport;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TZlibTransport;

public class IZlibTransport extends TZlibTransport {

    private final TTransport transport;

    private final boolean compBuffer = true;
    private int defaultCompressionLevel;
    private int compressionLevel;
    private boolean rInit = false;
    private boolean wInit = false;

    public IZlibTransport(TTransport transport) {
        super(transport, 6);
        this.transport = transport;
        this.defaultCompressionLevel = this.compressionLevel;
    }

    public IZlibTransport(TTransport transport, int compressionLevel) {
        super(transport, compressionLevel);
        this.transport = transport;
        this.defaultCompressionLevel = this.compressionLevel;
    }

    public void reset() {
        this.rInit = false;
        this.wInit = false;
        this.defaultCompressionLevel = this.compressionLevel;
    }


}
