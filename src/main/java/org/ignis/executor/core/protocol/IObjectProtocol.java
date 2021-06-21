package org.ignis.executor.core.protocol;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TTransport;

public class IObjectProtocol extends TCompactProtocol {
    /**
     * Create a TCompactProtocol.
     *
     * @param transport            the TTransport object to read from or write to.
     * @param stringLengthLimit    the maximum number of bytes to read for
     *                             variable-length fields.
     * @param containerLengthLimit the maximum number of elements to read
     */
    public IObjectProtocol(TTransport transport, long stringLengthLimit, long containerLengthLimit) {
        super(transport, stringLengthLimit, containerLengthLimit);
    }

    /**
     * Create a TCompactProtocol.
     *
     * @param transport the TTransport object to read from or write to.
     */
    public IObjectProtocol(TTransport transport) {
        super(transport);
    }


}
