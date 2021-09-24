package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;

import java.io.IOException;
import java.io.NotSerializableException;
import java.util.List;

public interface IPartition /*extends Iterable<Object>, Serializable */ {

    String getTYPE();

    void read(TTransport transport) throws TException, NotSerializableException;

    void write(TTransport transport, int compression, boolean nativ) throws TException;

    void write(TTransport transport, int compression) throws TException;

    void write(TTransport transport) throws TException;

    List<Object> getElements();

    void readIterator(IPartition partition);

    void writeIterator(IPartition partition);

    IPartition clone();

    void copyFrom(IPartition source);

    default void copyTo(IPartition target) {
        target.copyFrom(this);
    }

    void moveFrom(IPartition source);

    default void moveTo(IPartition source, IPartition target) {
        target.copyFrom(source);
    }

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    byte[] toBytes() throws IOException;

    void clear();

    void fit();

    String type();


}
