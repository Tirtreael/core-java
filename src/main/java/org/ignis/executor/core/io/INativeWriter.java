package org.ignis.executor.core.io;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public interface INativeWriter {

    WriterType write = new WriterType(INativeWriter::write);

    static void write(TProtocol protocol, Object obj) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        byte[] data = new byte[4096];
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            data = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        try {
            protocol.getTransport().write(data);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

}
