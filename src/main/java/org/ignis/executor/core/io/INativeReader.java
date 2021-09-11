package org.ignis.executor.core.io;

import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.core.protocol.IObjectProtocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public interface INativeReader {

    static Object read(IObjectProtocol protocol) {
        int size = 4096;
        byte[] data = new byte[size];
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ObjectInputStream in = null;
        try {
            protocol.getTransport().read(data, 0, size);
            in = new ObjectInputStream(bis);
            return in.readObject();
        } catch (IOException | ClassNotFoundException | TTransportException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
