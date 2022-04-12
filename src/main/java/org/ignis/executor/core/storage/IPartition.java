package org.ignis.executor.core.storage;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.ignis.executor.api.IReadIterator;
import org.ignis.executor.api.IWriteIterator;

import java.io.IOException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.instrument.Instrumentation;
import java.util.Iterator;
import java.util.List;

public interface IPartition extends Iterable<Object>, Serializable {

    String getType();

    void read(TTransport transport) throws TException, NotSerializableException;

    void write(TTransport transport, int compression, boolean nativ) throws TException;

    void write(TTransport transport, int compression) throws TException;

    void write(TTransport transport) throws TException;

    List<Object> getElements();

    void setElements(List<Object> elements);

    IReadIterator readIterator() throws TException;

    IWriteIterator writeIterator() throws TException;

    IPartition clone();

    void copyFrom(IPartition source);

    default void copyTo(IPartition target) {
        target.copyFrom(this);
    }

    void moveFrom(IPartition source) throws TException;

    default void moveTo(IPartition source, IPartition target) {
        target.copyFrom(source);
    }

    int size();

    default boolean isEmpty() {
        return size() == 0;
    }

    default long bytes() {
        if (this.size() == 0)
            return 0;
        else return 0;
//        else return IMemoryPartition.ObjectSizeFetcher.getObjectSize(this.getElements().get(0)) * this.size();
    }

    Iterator<Object> iterator();


    class ObjectSizeFetcher {
        private static Instrumentation instrumentation;

        public static void premain(String args, Instrumentation inst) {
            instrumentation = inst;
        }

        public static long getObjectSize(Object o) {
            return instrumentation.getObjectSize(o);
        }
    }

    byte[] toBytes() throws IOException;

    void clear() throws TException;

    void fit();

    String type();


}
