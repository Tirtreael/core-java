package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TField;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TStruct;
import org.ignis.executor.core.protocol.IObjectProtocol;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface IWriter {

    Map<Byte, WriterType> writers = Map.ofEntries(
            Map.entry(IType.I_VOID.id(), new WriterType((protocol, obj) -> {
            })),
            Map.entry(IType.I_BOOL.id(), new WriterType((protocol, obj) -> protocol.writeBool((boolean) obj))),
            Map.entry(IType.I_I08.id(), new WriterType((protocol, obj) -> protocol.writeByte((byte) obj))),
            Map.entry(IType.I_I16.id(), new WriterType((protocol, obj) -> protocol.writeI16((short) obj))),
            Map.entry(IType.I_I32.id(), new WriterType((protocol, obj) -> protocol.writeI32((int) obj))),
            Map.entry(IType.I_I64.id(), new WriterType((protocol, obj) -> protocol.writeI64((long) obj))),
            Map.entry(IType.I_DOUBLE.id(), new WriterType((protocol, obj) -> protocol.writeDouble((double) obj))),
            Map.entry(IType.I_STRING.id(), new WriterType((protocol, obj) -> protocol.writeString((String) obj))),
            Map.entry(IType.I_LIST.id(), new WriterType((protocol, obj) -> writeList(protocol, (List<?>) obj))),
            Map.entry(IType.I_SET.id(), new WriterType((protocol, obj) -> writeSet(protocol, (Set<?>) obj))),
            Map.entry(IType.I_MAP.id(), new WriterType((protocol, obj) -> writeMap(protocol, (Map<?, ?>) obj))),
            Map.entry(IType.I_PAIR.id(), new WriterType((protocol, obj) -> writePair(protocol, (Map.Entry<?, ?>) obj))),
            Map.entry(IType.I_BINARY.id(), new WriterType((protocol, obj) -> writeBinary(protocol, (byte[]) obj))),
            Map.entry(IType.I_PAIR_LIST.id(), new WriterType((protocol, obj) -> writePairList(protocol, (List<Map.Entry<Object, Object>>) obj))),
            Map.entry(IType.I_JSON.id(), new WriterType((protocol, obj) -> writeJSON(protocol, (JSONObject) obj)))
    );


    static WriterType getWriterType(Class<?> clazz) {
        return writers.get(IType.getIdClazz(clazz));
    }

    static void write(TProtocol protocol, Object obj) throws TException {
        writeType(protocol, IType.getId(obj));

        WriterType writerType = getWriterType(obj.getClass());
        writerType.getWrite().apply(protocol, obj);
    }

    static void writeSize(TProtocol protocol, long i64) throws TException {
        protocol.writeI64(i64);
    }

    static void writeType(TProtocol protocol, byte type) throws TException {
        protocol.writeByte(type);
    }

    static void writeI32(TProtocol protocol, Integer obj) throws TException {
        writeType(protocol, IType.getIdClazz(obj.getClass()));
    }

    static <T> void writeList(TProtocol protocol, List<T> list) throws TException {
        long size = list.size();
        Class<?> elemType;
        if (size == 0) {
            elemType = IType.I_VOID.type();
        } else {
            elemType = list.get(0).getClass();
        }
        WriterType wt = getWriterType(elemType);
        writeSize(protocol, size);
        writeType(protocol, IType.getIdClazz(elemType));
        for (T obj : list)
            wt.getWrite().apply(protocol, obj);
    }

    static <T> void writeSet(TProtocol protocol, Set<T> set) throws TException {
        long size = set.size();
        Class<?> elemType;
        if (size == 0) {
            elemType = IType.I_VOID.type();
        } else {
            elemType = set.toArray()[0].getClass();
        }
        WriterType wt = getWriterType(elemType);
        writeSize(protocol, size);
        writeType(protocol, IType.getId(elemType));
        for (T obj : set)
            wt.getWrite().apply(protocol, obj);
    }

    static <K, V> void writeMap(TProtocol protocol, Map<K, V> map) throws TException {
        long size = map.size();
        Class<?> elemTypeKey;
        Class<?> elemTypeValue;
        if (size == 0) {
            elemTypeKey = IType.I_VOID.type();
            elemTypeValue = IType.I_VOID.type();
        } else {
            Map.Entry<K, V> entry = map.entrySet().iterator().next();
            elemTypeKey = entry.getKey().getClass();
            elemTypeValue = entry.getValue().getClass();
        }
        WriterType wtKey = getWriterType(elemTypeKey);
        WriterType wtValue = getWriterType(elemTypeValue);
        writeSize(protocol, size);
        writeType(protocol, IType.getId(elemTypeKey));
        writeType(protocol, IType.getId(elemTypeValue));
        for (Map.Entry<K, V> e : map.entrySet()) {
            wtKey.getWrite().apply(protocol, e.getKey());
            wtValue.getWrite().apply(protocol, e.getValue());
        }
    }

    static <K, V> void writePair(TProtocol protocol, AbstractMap.Entry<K, V> pair) throws TException {
        Class<?> elemTypeKey = pair.getKey().getClass();
        Class<?> elemTypeValue = pair.getValue().getClass();
        WriterType writerTypeKey = getWriterType(elemTypeKey);
        WriterType writerTypeValue = getWriterType(elemTypeValue);

        writeType(protocol, IType.getId(elemTypeKey));
        writeType(protocol, IType.getId(elemTypeValue));
        writerTypeKey.getWrite().apply(protocol, pair.getKey());
        writerTypeValue.getWrite().apply(protocol, pair.getValue());
    }

    static void writeBinary(TProtocol protocol, byte[] binary) throws TException {
        protocol.writeBinary(ByteBuffer.wrap(binary));
    }

    static void writePairList(TProtocol protocol, List<Map.Entry<Object, Object>> pairList) throws TException {
        long size = pairList.size();
        Class<?> elemTypeKey = IType.I_VOID.type();
        Class<?> elemTypeValue = IType.I_VOID.type();
        if (size != 0) {
            Map.Entry<Object, Object> pair1 = pairList.get(0);
            elemTypeKey = pair1.getKey().getClass();
            elemTypeValue = pair1.getValue().getClass();
        }
        WriterType writerTypeKey = getWriterType(elemTypeKey);
        WriterType writerTypeValue = getWriterType(elemTypeValue);
        writeSize(protocol, size);
        writeType(protocol, IType.I_PAIR_LIST.id());
        for (Map.Entry<Object, Object> pair : pairList) {
            writerTypeKey.getWrite().apply(protocol, pair.getKey());
            writerTypeValue.getWrite().apply(protocol, pair.getValue());
        }
    }

    static void writeJSON(TProtocol protocol, JSONObject obj) throws TException {

    }

}
