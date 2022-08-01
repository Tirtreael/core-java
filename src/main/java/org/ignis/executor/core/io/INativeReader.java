package org.ignis.executor.core.io;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.core.transport.StreamTransport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public interface INativeReader {

    ReaderType read = new ReaderType(INativeReader::read);

    static List<Object> read(TProtocol protocol, long elements) {
        List<Object> list = new ArrayList<>((int) elements);
        try {
            ObjectInputStream in = new ObjectInputStream(new StreamTransport(protocol.getTransport()));
            long i = 0;
            while (i < elements) {
                list.add(in.readObject());
                i++;
            }
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    static Object read(TProtocol protocol) {
        int size = 4096;
        byte[] data = new byte[size];
        try {
            protocol.getTransport().read(data, 0, size);
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream in;
            in = new ObjectInputStream(bis);
            Object obj = in.readObject();
            in.close();
            return obj;
        } catch (IOException | ClassNotFoundException | TTransportException e) {
            e.printStackTrace();
        }
        return null;
    }
}
