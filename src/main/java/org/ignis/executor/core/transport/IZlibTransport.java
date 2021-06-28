package org.ignis.executor.core.transport;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TZlibTransport;

public class IZlibTransport extends TZlibTransport {

    private final boolean compBuffer = true;
    private int compressionLevel;
    //@Todo check if needed
    private boolean rInit = false;
    private boolean wInit = false;

    public IZlibTransport(TTransport transport) {
        super(transport);
    }

    public IZlibTransport(TTransport transport, int compressionLevel) {
        super(transport, compressionLevel);
    }

    public void reset() {
        this.rInit = false;
        this.wInit = false;
    }


}
