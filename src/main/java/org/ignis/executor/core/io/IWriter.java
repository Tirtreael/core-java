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
            Map.entry(IEnumTypes.I_VOID.id, new WriterType((protocol, obj) -> {
            })),
            Map.entry(IEnumTypes.I_BOOL.id, new WriterType((protocol, obj) -> protocol.writeBool((boolean) obj))),
            Map.entry(IEnumTypes.I_I08.id, new WriterType((protocol, obj) -> protocol.writeByte((byte) obj))),
            Map.entry(IEnumTypes.I_I16.id, new WriterType((protocol, obj) -> protocol.writeI16((short) obj))),
            Map.entry(IEnumTypes.I_I32.id, new WriterType((protocol, obj) -> protocol.writeI32((int) obj))),
            Map.entry(IEnumTypes.I_I64.id, new WriterType((protocol, obj) -> protocol.writeI64((long) obj))),
            Map.entry(IEnumTypes.I_DOUBLE.id, new WriterType((protocol, obj) -> protocol.writeDouble((double) obj))),
            Map.entry(IEnumTypes.I_STRING.id, new WriterType((protocol, obj) -> protocol.writeString((String) obj))),
            Map.entry(IEnumTypes.I_LIST.id, new WriterType((protocol, obj) -> writeList(protocol, (List<?>) obj))),
            Map.entry(IEnumTypes.I_SET.id, new WriterType((protocol, obj) -> writeSet(protocol, (Set<?>) obj))),
            Map.entry(IEnumTypes.I_MAP.id, new WriterType((protocol, obj) -> writeMap(protocol, (Map<?, ?>) obj))),
            Map.entry(IEnumTypes.I_PAIR.id, new WriterType((protocol, obj) -> writePair(protocol, (Map.Entry<?, ?>) obj))),
            Map.entry(IEnumTypes.I_BINARY.id, new WriterType((protocol, obj) -> writeBinary(protocol, (byte[]) obj))),
            Map.entry(IEnumTypes.I_PAIR_LIST.id, new WriterType((protocol, obj) -> writePairList(protocol, (List<Map.Entry<?, ?>>) obj))),
            Map.entry(IEnumTypes.I_JSON.id, new WriterType((protocol, obj) -> writeJSON(protocol, (JSONObject) obj)))
    );

    static WriterType getWriterType(byte id) {
        return writers.get(id);
    }

    static WriterType getWriterType(Object obj) {
        return writers.get(IEnumTypes.getInstance().getId(obj));
    }

    static void write(TProtocol protocol, Object obj) throws TException {
        writeType(protocol, IEnumTypes.getInstance().getId(obj));

        WriterType writerType = getWriterType(IEnumTypes.getInstance().getId(obj));
        writerType.getWrite().write(protocol, obj);
    }

    static void writeSize(TProtocol protocol, long i64) throws TException {
        protocol.writeI64(i64);
    }

    static void writeType(TProtocol protocol, byte type) throws TException {
        protocol.writeByte(type);
    }

    static void writeI32(TProtocol protocol, Integer obj) throws TException {
        writeType(protocol, IEnumTypes.getInstance().getId(obj));
    }

    static <T> void writeList(TProtocol protocol, List<T> list) throws TException {
        long size = list.size();
        IType elemType = IEnumTypes.I_VOID;
        WriterType wt = getWriterType(IEnumTypes.I_VOID.id);

        writeSize(protocol, size);
        if (size > 0) {
            elemType = IEnumTypes.getInstance().getType(list.get(0));
            wt = getWriterType(elemType.id);
        }
        writeType(protocol, elemType.id);
        for (T obj : list)
            wt.getWrite().write(protocol, obj);
    }

    static <T> void writeSet(TProtocol protocol, Set<T> set) throws TException {
        long size = set.size();
        IType elemType = IEnumTypes.I_VOID;
        if (size > 0) {
            elemType = IEnumTypes.getInstance().getType(set.toArray()[0]);
        }
        WriterType wt = getWriterType(elemType.id);
        writeSize(protocol, size);
        writeType(protocol, elemType.id);
        for (T obj : set)
            wt.getWrite().write(protocol, obj);
    }

    static <K, V> void writeMap(TProtocol protocol, Map<K, V> map) throws TException {
        long size = map.size();
        IType elemTypeKey = IEnumTypes.I_VOID;
        IType elemTypeValue = IEnumTypes.I_VOID;
        if (size > 0) {
            Map.Entry<K, V> entry = map.entrySet().iterator().next();
            elemTypeKey = IEnumTypes.getInstance().getType(entry.getKey());
            elemTypeValue = IEnumTypes.getInstance().getType(entry.getValue());
        }
        WriterType wtKey = getWriterType(elemTypeKey.id);
        WriterType wtValue = getWriterType(elemTypeValue.id);
        writeSize(protocol, size);
        writeType(protocol, elemTypeKey.id);
        writeType(protocol, elemTypeValue.id);
        for (Map.Entry<K, V> e : map.entrySet()) {
            wtKey.getWrite().write(protocol, e.getKey());
            wtValue.getWrite().write(protocol, e.getValue());
        }
    }

    static <K, V> void writePair(TProtocol protocol, Map.Entry<K, V> pair) throws TException {
        IType elemTypeKey = IEnumTypes.getInstance().getType(pair.getKey());
        IType elemTypeValue = IEnumTypes.getInstance().getType(pair.getValue());
        WriterType writerTypeKey = getWriterType(elemTypeKey.id);
        WriterType writerTypeValue = getWriterType(elemTypeValue.id);

        writeType(protocol, elemTypeKey.id);
        writeType(protocol, elemTypeValue.id);
        writerTypeKey.getWrite().write(protocol, pair.getKey());
        writerTypeValue.getWrite().write(protocol, pair.getValue());
    }

    static void writeBinary(TProtocol protocol, byte[] binary) throws TException {
        protocol.writeBinary(ByteBuffer.wrap(binary));
    }

    static void writePairList(TProtocol protocol, List<Map.Entry<?, ?>> pairList) throws TException {
        long size = pairList.size();
        IType elemTypeKey = IEnumTypes.I_VOID;
        IType elemTypeValue = IEnumTypes.I_VOID;
        WriterType wtKey = getWriterType(IEnumTypes.I_VOID.id);
        WriterType wtValue = getWriterType(IEnumTypes.I_VOID.id);

        writeSize(protocol, size);
        if (size > 0) {
            elemTypeKey = IEnumTypes.getInstance().getType(pairList.get(0).getKey());
            elemTypeValue = IEnumTypes.getInstance().getType(pairList.get(0).getValue());
            wtKey = getWriterType(elemTypeKey.id);
            wtValue = getWriterType(elemTypeValue.id);
        }
        writeType(protocol, elemTypeKey.id);
        writeType(protocol, elemTypeValue.id);
        for (Map.Entry<?, ?> pair : pairList) {
            wtKey.getWrite().write(protocol, pair.getKey());
            wtValue.getWrite().write(protocol, pair.getValue());
        }
    }

    static void writeJSON(TProtocol protocol, JSONObject obj) throws TException {
        write(protocol, obj.toMap());
    }

}
