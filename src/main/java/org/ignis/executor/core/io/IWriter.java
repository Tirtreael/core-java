package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.json.JSONObject;

import java.nio.ByteBuffer;
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
            Map.entry(IType.I_PAIR_LIST.id(), new WriterType((protocol, obj) -> writePairList(protocol, (List<Map.Entry<?, ?>>) obj))),
            Map.entry(IType.I_JSON.id(), new WriterType((protocol, obj) -> writeJSON(protocol, (JSONObject) obj)))
    );

    static WriterType getWriterType(byte id) {
        return writers.get(id);
    }

    static void write(TProtocol protocol, Object obj) throws TException {
        writeType(protocol, IType.getId(obj));

        WriterType writerType = getWriterType(IType.getId(obj));
        writerType.getWrite().apply(protocol, obj);
    }

    static void writeSize(TProtocol protocol, long i64) throws TException {
        protocol.writeI64(i64);
    }

    static void writeType(TProtocol protocol, byte type) throws TException {
        protocol.writeByte(type);
    }

    static void writeI32(TProtocol protocol, Integer obj) throws TException {
        writeType(protocol, IType.getId(obj));
    }

    static <T> void writeList(TProtocol protocol, List<T> list) throws TException {
        long size = list.size();
        IType elemType = IType.I_VOID;
        WriterType wt = getWriterType(IType.I_VOID.id);

        writeSize(protocol, size);
        if (size > 0) {
            elemType = IType.getIType(list.get(0));
            wt = getWriterType(elemType.id);
        }
        writeType(protocol, elemType.id);
        for (T obj : list)
            wt.getWrite().apply(protocol, obj);
    }

    static <T> void writeSet(TProtocol protocol, Set<T> set) throws TException {
        long size = set.size();
        IType elemType = IType.I_VOID;
        if (size > 0) {
            elemType = IType.getIType(set.toArray()[0]);
        }
        WriterType wt = getWriterType(elemType.id);
        writeSize(protocol, size);
        writeType(protocol, elemType.id);
        for (T obj : set)
            wt.getWrite().apply(protocol, obj);
    }

    static <K, V> void writeMap(TProtocol protocol, Map<K, V> map) throws TException {
        long size = map.size();
        IType elemTypeKey = IType.I_VOID;
        IType elemTypeValue = IType.I_VOID;
        if (size > 0) {
            Map.Entry<K, V> entry = map.entrySet().iterator().next();
            elemTypeKey = IType.getIType(entry.getKey());
            elemTypeValue = IType.getIType(entry.getValue());
        }
        WriterType wtKey = getWriterType(elemTypeKey.id);
        WriterType wtValue = getWriterType(elemTypeValue.id);
        writeSize(protocol, size);
        writeType(protocol, elemTypeKey.id);
        writeType(protocol, elemTypeValue.id);
        for (Map.Entry<K, V> e : map.entrySet()) {
            wtKey.getWrite().apply(protocol, e.getKey());
            wtValue.getWrite().apply(protocol, e.getValue());
        }
    }

    static <K, V> void writePair(TProtocol protocol, Map.Entry<K, V> pair) throws TException {
        IType elemTypeKey = IType.getIType(pair.getKey());
        IType elemTypeValue = IType.getIType(pair.getValue());
        WriterType writerTypeKey = getWriterType(elemTypeKey.id);
        WriterType writerTypeValue = getWriterType(elemTypeValue.id);

        writeType(protocol, elemTypeKey.id);
        writeType(protocol, elemTypeValue.id);
        writerTypeKey.getWrite().apply(protocol, pair.getKey());
        writerTypeValue.getWrite().apply(protocol, pair.getValue());
    }

    static void writeBinary(TProtocol protocol, byte[] binary) throws TException {
        protocol.writeBinary(ByteBuffer.wrap(binary));
    }

    static void writePairList(TProtocol protocol, List<Map.Entry<?, ?>> pairList) throws TException {
        long size = pairList.size();
        IType elemTypeKey = IType.I_VOID;
        IType elemTypeValue = IType.I_VOID;
        WriterType wtKey = getWriterType(IType.I_VOID.id);
        WriterType wtValue = getWriterType(IType.I_VOID.id);

        writeSize(protocol, size);
        if (size > 0) {
            elemTypeKey = IType.getIType(pairList.get(0).getKey());
            elemTypeValue = IType.getIType(pairList.get(0).getValue());
            wtKey = getWriterType(elemTypeKey.id);
            wtValue = getWriterType(elemTypeValue.id);
        }
        writeType(protocol, elemTypeKey.id);
        writeType(protocol, elemTypeValue.id);
        for (Map.Entry<?, ?> pair : pairList) {
            wtKey.getWrite().apply(protocol, pair.getKey());
            wtValue.getWrite().apply(protocol, pair.getValue());
        }
    }

    static void writeJSON(TProtocol protocol, JSONObject obj) throws TException {
        write(protocol, obj.toMap());
    }

}
