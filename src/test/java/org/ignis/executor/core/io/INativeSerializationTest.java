package org.ignis.executor.core.io;

import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.ignis.executor.core.transport.IZlibTransport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class INativeSerializationTest {

    @Test
    void serialDeserial() {
        TMemoryBuffer memoryBuffer = new TMemoryBuffer(4096);
        TTransport zlib = new IZlibTransport(memoryBuffer);
        IObjectProtocol proto = new IObjectProtocol(zlib);

        String str = "Jacob";
        String str2;

        INativeWriter.write(proto, str);
        str2 = (String) INativeReader.read(proto);

        assertEquals(str, str2);
    }
}