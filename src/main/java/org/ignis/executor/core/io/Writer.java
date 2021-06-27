package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Writer {

    public final Map<Type, WriterType> writers = Map.ofEntries(
            Map.entry(IType.I_VOID.type(), new WriterType((protocol, obj) -> {
            })),
            Map.entry(IType.I_BOOL.type(), new WriterType((protocol, obj) -> this.writeBoolean(protocol, (boolean) obj))),
            Map.entry(IType.I_I08.type(), new WriterType((protocol, obj) -> this.writeByte(protocol, (byte) obj))),
            Map.entry(IType.I_I16.type(), new WriterType((protocol, obj) -> this.writeShort(protocol, (short) obj))),
            Map.entry(IType.I_I32.type(), new WriterType((protocol, obj) -> this.writeInt(protocol, (int) obj))),
            Map.entry(IType.I_I64.type(), new WriterType((protocol, obj) -> this.writeLong(protocol, (long) obj))),
            Map.entry(IType.I_DOUBLE.type(), new WriterType((protocol, obj) -> this.writeDouble(protocol, (double) obj))),
            Map.entry(IType.I_STRING.type(), new WriterType((protocol, obj) -> this.writeString(protocol, (String) obj))),
            Map.entry(IType.I_LIST.type(), new WriterType((protocol, obj) -> this.writeList(protocol, (List<?>) obj))),
            Map.entry(IType.I_SET.type(), new WriterType((protocol, obj) -> this.writeSet(protocol, (Set<?>) obj))),
            Map.entry(IType.I_MAP.type(), new WriterType((protocol, obj) -> this.writeMap(protocol, (Map<?, ?>) obj))),
            Map.entry(IType.I_PAIR.type(), new WriterType((protocol, obj) -> this.writePair(protocol, (Map.Entry<?, ?>) obj))),
            Map.entry(IType.I_BINARY.type(), new WriterType((protocol, obj) -> this.writeBinary(protocol, (byte[]) obj)))
            /*,
            Map.entry(IType.I_PAIR_LIST.type(), new WriterType((protocol, obj) -> {
                try {
                    this.writePairList(protocol, (List<Map.Entry<?,?>>) obj);
                } catch (TException e) {
                    e.printStackTrace();
                }
            })),
            Map.entry(IType.I_JSON.type(), new WriterType((protocol, obj) -> {
                try {
                    this.writeJSON(protocol, (JSONObject) obj);
                } catch (TException e) {
                    e.printStackTrace();
                }
            }))*/
    );


    public WriterType getWriterType(Type type) {
        return this.writers.get(type);
    }

    public void writeSize(TProtocol protocol, long i64) throws TException {
        protocol.writeI64(i64);
    }

    public void writeType(TProtocol protocol, byte type) throws TException {
        protocol.writeByte(type);
    }


    public void writeBoolean(TProtocol protocol, boolean obj) throws TException {
        protocol.writeBool(obj);
    }

    public void writeByte(TProtocol protocol, byte obj) throws TException {
        protocol.writeByte(obj);
    }

    public void writeShort(TProtocol protocol, short obj) throws TException {
        protocol.writeI16(obj);
    }

    public void writeInt(TProtocol protocol, int obj) throws TException {
        protocol.writeI32(obj);
    }

    public void writeLong(TProtocol protocol, long obj) throws TException {
        protocol.writeI64(obj);
    }

    public void writeDouble(TProtocol protocol, double obj) throws TException {
        protocol.writeDouble(obj);
    }

    public void writeString(TProtocol protocol, String obj) throws TException {
        protocol.writeString(obj);
    }

    public <T> void writeList(TProtocol protocol, List<T> list) throws TException {
        long size = list.size();
        Type elemType;
        if (size == 0) {
            elemType = IType.I_VOID.type();
        } else {
            elemType = list.get(0).getClass();
        }
        WriterType wt = getWriterType(elemType);
        this.writeSize(protocol, size);
        this.writeType(protocol, IType.types.get(elemType).id());
        for (T obj : list)
            wt.getWrite().apply(protocol, obj);
//        protocol.writeListEnd();
    }

    public <T> void writeSet(TProtocol protocol, Set<T> set) throws TException {
        long size = set.size();
        Type elemType;
        if (size == 0) {
            elemType = IType.I_VOID.type();
        } else {
            elemType = set.toArray()[0].getClass();
        }
        WriterType wt = getWriterType(elemType);
        this.writeSize(protocol, size);
        this.writeType(protocol, IType.types.get(elemType).id());
        for (T obj : set)
            wt.getWrite().apply(protocol, obj);
//        protocol.writeSetEnd();
    }

    public <K, V> void writeMap(TProtocol protocol, Map<K, V> map) throws TException {
        long size = map.size();
        Type elemTypeKey;
        Type elemTypeValue;
        if (size == 0) {
            elemTypeKey = IType.I_VOID.type();
            elemTypeValue = IType.I_VOID.type();
        } else {
            Map.Entry<K, V> entry = (Map.Entry<K, V>) map.entrySet().toArray()[0];
            elemTypeKey = entry.getKey().getClass();
            elemTypeValue = entry.getValue().getClass();
        }
        WriterType wtKey = getWriterType(elemTypeKey);
        WriterType wtValue = getWriterType(elemTypeValue);
        this.writeSize(protocol, size);
        this.writeType(protocol, IType.types.get(elemTypeKey).id());
        this.writeType(protocol, IType.types.get(elemTypeValue).id());
        for (Map.Entry<K, V> e : map.entrySet()) {
            wtKey.getWrite().apply(protocol, e.getKey());
            wtValue.getWrite().apply(protocol, e.getValue());
        }
    }

    public <K, V> void writePair(TProtocol protocol, AbstractMap.Entry<K, V> pair) throws TException {
        Type elemTypeKey = pair.getKey().getClass();
        Type elemTypeValue = pair.getValue().getClass();
        WriterType writerTypeKey = getWriterType(elemTypeKey);
        WriterType writerTypeValue = getWriterType(elemTypeValue);

        this.writeType(protocol, IType.types.get(elemTypeKey).id());
        this.writeType(protocol, IType.types.get(elemTypeValue).id());
        writerTypeKey.getWrite().apply(protocol, pair.getKey());
        writerTypeValue.getWrite().apply(protocol, pair.getValue());
    }

    public void writeBinary(TProtocol protocol, byte[] binary) throws TException {
        protocol.writeBinary(ByteBuffer.wrap(binary));
    }

    public <K, V> void writePairList(TProtocol protocol, List<Map.Entry<K, V>> pairList) throws TException {

    }


    public void writeJSON(TProtocol protocol, JSONObject obj) throws TException {

    }

}
