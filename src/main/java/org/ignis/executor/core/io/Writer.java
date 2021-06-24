package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.*;


public class Writer implements IWriter {

    final WriterType<Void> I_VOID = new WriterType<>((TProtocol protocol, Void obj) -> {
    });

    final WriterType<Boolean> I_BOOL = new WriterType<>((TProtocol protocol, Boolean obj) -> {
        try {
            this.writeBoolean(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<Byte> I_I08 = new WriterType<>((TProtocol protocol, Byte obj) -> {
        try {
            this.writeByte(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<Short> I_I16 = new WriterType<>((TProtocol protocol, Short obj) -> {
        try {
            this.writeShort(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<Integer> I_I32 = new WriterType<>((TProtocol protocol, Integer obj) -> {
        try {
            this.writeInt(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<Long> I_I64 = new WriterType<>((TProtocol protocol, Long obj) -> {
        try {
            this.writeLong(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<Double> I_DOUBLE = new WriterType<>((TProtocol protocol, Double obj) -> {
        try {
            this.writeDouble(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<String> I_STRING = new WriterType<>((TProtocol protocol, String obj) -> {
        try {
            this.writeString(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<List<?>> I_LIST = new WriterType<>((TProtocol protocol, List<?> obj) -> {
        try {
            this.writeList(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<Set<?>> I_SET = new WriterType<>((TProtocol protocol, Set<?> obj) -> {
        try {
            this.writeSet(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<Map<?, ?>> I_MAP = new WriterType<>((TProtocol protocol, Map<?, ?> obj) -> {
        try {
            this.writeMap(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<AbstractMap.SimpleEntry<?, ?>> I_PAIR = new WriterType<>((TProtocol protocol, AbstractMap.SimpleEntry<?, ?> obj) -> {
        try {
            this.writePair(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<byte[]> I_BINARY = new WriterType<>((TProtocol protocol, byte[] obj) -> {
        try {
            this.writeBinary(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<List> I_PAIR_LIST = new WriterType<>((TProtocol protocol, List obj) -> {
        try {
            this.writePairList(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
    final WriterType<JSONObject> I_JSON = new WriterType<>((TProtocol protocol, JSONObject obj) -> {
        try {
            this.writeJSON(protocol, obj);
        } catch (TException e) {
            e.printStackTrace();
        }
    });
//    }


    public void writeTypeAux(TProtocol protocol, long s) throws TException {
        protocol.writeI64(s);
    }

    public <T> WriterType getWriterType(Object obj) {
        Class<?> clazz = obj.getClass();
        if (clazz.equals(ITypes.TYPES[ITypes.I_VOID])) {
            return this.I_VOID;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_BOOL])) {
            return this.I_BOOL;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_I08])) {
            return this.I_I08;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_I16])) {
            return this.I_I16;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_I32])) {
            return this.I_I32;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_I64])) {
            return this.I_I64;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_DOUBLE])) {
            return this.I_DOUBLE;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_STRING])) {
            return this.I_STRING;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_LIST])) {
            return this.I_LIST;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_SET])) {
            return this.I_SET;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_MAP])) {
            return this.I_MAP;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_PAIR])) {
            return this.I_PAIR;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_BINARY])) {
            return this.I_BINARY;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_PAIR_LIST])) {
            return this.I_PAIR_LIST;
        }
        if (clazz.equals(ITypes.TYPES[ITypes.I_JSON])) {
            return this.I_JSON;
        }
        return this.I_VOID;
    }

    void writeType(TProtocol protocol, Class<?> clazz) throws TException {
        protocol.writeByte(ITypes.getTypeIndex(clazz));
    }

    public void writeSizeAux(TProtocol protocol, long i64) throws TException {
        protocol.writeI64(i64);
    }

    @Override
    public void writeBoolean(TProtocol protocol, boolean obj) throws TException {
        protocol.writeBool(obj);
    }

    @Override
    public void writeByte(TProtocol protocol, byte obj) throws TException {
        protocol.writeByte(obj);
    }

    @Override
    public void writeShort(TProtocol protocol, short obj) throws TException {
        protocol.writeI16(obj);
    }

    @Override
    public void writeInt(TProtocol protocol, int obj) throws TException {
        protocol.writeI32(obj);
    }

    @Override
    public void writeLong(TProtocol protocol, long obj) throws TException {
        protocol.writeI64(obj);
    }

    @Override
    public void writeDouble(TProtocol protocol, double obj) throws TException {
        protocol.writeDouble(obj);
    }

    @Override
    public void writeString(TProtocol protocol, String obj) throws TException {
        protocol.writeString(obj);
    }

    public <T> void writeList(TProtocol protocol, List<T> list) throws TException {
        long size = list.size();
        this.writeSizeAux(protocol, size);
        WriterType<T> wt;
        if (size == 0)
            wt = getWriterType(null);
        else wt = getWriterType(list.get(0));

        this.writeType(protocol, list.getClass());
        for (T obj : list)
            wt.getWrite().accept(protocol, obj);
    }

    @Override
    public <T> void writeSet(TProtocol protocol, Set<T> set) throws TException {
        long size = set.size();
        this.writeSizeAux(protocol, size);
        WriterType<T> wt;
        if (size == 0)
            wt = getWriterType(null);
        else wt = getWriterType(set.stream().findAny().get());

        this.writeType(protocol, set.getClass());
        for (T obj : set)
            wt.getWrite().accept(protocol, obj);
    }

    public <K, V> void writeMap(TProtocol protocol, Map<K, V> map) throws TException {
        long size = map.size();
        this.writeSizeAux(protocol, size);
        WriterType<K> writerTypeKey;
        WriterType<V> writerTypeValue;
        if (size == 0) {
            writerTypeKey = getWriterType(null);
            writerTypeValue = getWriterType(null);
        } else {
            Map.Entry<K, V> entry = map.entrySet().stream().findAny().get();
            writerTypeKey = getWriterType(entry.getKey());
            writerTypeValue = getWriterType(entry.getValue());
        }

        this.writeType(protocol, map.getClass());
        for (Map.Entry<K, V> e : map.entrySet()) {
            writerTypeKey.getWrite().accept(protocol, e.getKey());
            writerTypeValue.getWrite().accept(protocol, e.getValue());
        }
    }

    @Override
    public <K, V> void writePair(TProtocol protocol, AbstractMap.SimpleEntry<K, V> pair) throws TException {
        WriterType<K> writerTypeKey;
        WriterType<V> writerTypeValue;
        writerTypeKey = getWriterType(pair.getKey());
        writerTypeValue = getWriterType(pair.getValue());

        this.writeType(protocol, pair.getClass());
        writerTypeKey.getWrite().accept(protocol, pair.getKey());
        writerTypeValue.getWrite().accept(protocol, pair.getValue());
    }

    @Override
    public void writeBinary(TProtocol protocol, byte[] binary) throws TException {
        long size = binary.length;
        this.writeSizeAux(protocol, size);
        protocol.writeBinary(ByteBuffer.wrap(binary));
    }

    @Override
    public <K,V,T extends Map.Entry<K,V>> void writePairList(TProtocol protocol, List<T> pairList) throws TException {
        long size = pairList.size();
        this.writeSizeAux(protocol, size);
        WriterType<K> writerTypeKey;
        WriterType<V> writerTypeValue;
        if (size == 0) {
            writerTypeKey = getWriterType(null);
            writerTypeValue = getWriterType(null);
        } else {
            Map.Entry<K, V> entry = pairList.get(0);
            writerTypeKey = getWriterType(entry.getKey());
            writerTypeValue = getWriterType(entry.getValue());
        }

        this.writeType(protocol, pairList.getClass());
        for (Map.Entry<K, V> e : pairList) {
            writerTypeKey.getWrite().accept(protocol, e.getKey());
            writerTypeValue.getWrite().accept(protocol, e.getValue());
        }
    }

    @Override
    public void writeJSON(TProtocol protocol, JSONObject obj) throws TException {

    }

}
