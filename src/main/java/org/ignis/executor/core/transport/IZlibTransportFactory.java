package org.ignis.executor.core.transport;

import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TZlibTransport;

public class IZlibTransportFactory extends TZlibTransport.Factory {

    private int compression;

    public IZlibTransportFactory(int compression) {
        this.compression = compression;
    }

    @Override
    public TTransport getTransport(TTransport base) throws TTransportException {
        return new TZlibTransport(base, compression);
    }
}
