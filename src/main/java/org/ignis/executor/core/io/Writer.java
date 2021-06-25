package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TSet;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Writer implements IWriter {

    public final Map<Type, WriterType> writers = Map.ofEntries(
            Map.entry(IType.I_VOID.type(), new WriterType((protocol, obj) -> {
            })),
            Map.entry(IType.I_BOOL.type(), new WriterType((protocol, obj) -> {
                try {
                    this.writeBoolean(protocol, (boolean) obj);
                } catch (TException e) {
                    e.printStackTrace();
                }
            }))
    );

//            new WriterType((protocol, obj) -> {
//                try {
//                    this.writeBoolean(protocol, (boolean) obj);
//                } catch (TException e) {
//                    e.printStackTrace();
//                }
//            }),
//            new WriterType((protocol, obj) -> {
//                try {
//                    this.writeByte(protocol, (byte) obj);
//                } catch (TException e) {
//                    e.printStackTrace();
//                }
//            };
    /*final WriterType<Byte> I_I08 = new WriterType<>((TProtocol protocol, Byte obj) -> {
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
    final WriterType<List<?>> I_LIST = new WriterType<Type>((TProtocol protocol, List<Type> obj) -> {
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
    final WriterType<List<AbstractMap.SimpleEntry<?, ?>>> I_PAIR_LIST = new WriterType<>((TProtocol protocol, List<AbstractMap.SimpleEntry<?, ?>> obj) -> {
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
    });*/
//    }


    public void writeTypeAux(TProtocol protocol, long s) throws TException {
        protocol.writeI64(s);
    }

    public WriterType getWriterType(Type type) {
        return this.writers.get(type);
    }
/*
    void writeType(TProtocol protocol, Type type) throws TException {
        protocol.writeByte();
    }*/

    public void writeSizeAux(TProtocol protocol, long i64) throws TException {
        protocol.writeI64(i64);
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
        int size = list.size();
        TList tList = new TList(IType.I_LIST.id(), size);
//        this.writeSizeAux(protocol, size);
        protocol.writeListBegin(tList);
        WriterType wt;
        if (size == 0)
            wt = getWriterType(IType.I_VOID.type());
        else
            wt = getWriterType(list.get(0).getClass());
        for (T obj : list)
            wt.getWrite().accept(protocol, obj);
        protocol.writeListEnd();
    }

    public void writeSet(TProtocol protocol, Set<Object> set) throws TException {
        int size = set.size();
        TSet tSet = new TSet(IType.I_SET.id(), size);
//        this.writeSizeAux(protocol, size);
        protocol.writeSetBegin(tSet);
        WriterType wt;
        if (size == 0)
            wt = getWriterType(IType.I_VOID.type());
        else
            wt = getWriterType(set.toArray()[0].getClass());
        for (Object obj : set)
            wt.getWrite().accept(protocol, obj);
        protocol.writeSetEnd();
    }
/*
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

    public <K, V> void writePair(TProtocol protocol, AbstractMap.SimpleEntry<K, V> pair) throws TException {
        WriterType<K> writerTypeKey;
        WriterType<V> writerTypeValue;
        writerTypeKey = getWriterType(pair.getKey());
        writerTypeValue = getWriterType(pair.getValue());

        this.writeType(protocol, pair.getClass());
        writerTypeKey.getWrite().accept(protocol, pair.getKey());
        writerTypeValue.getWrite().accept(protocol, pair.getValue());
    }

    public void writeBinary(TProtocol protocol, byte[] binary) throws TException {
        long size = binary.length;
        this.writeSizeAux(protocol, size);
        protocol.writeBinary(ByteBuffer.wrap(binary));
    }

    public void writePairList(TProtocol protocol, List<AbstractMap.SimpleEntry<?, ?>> pairList) throws TException {
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

    public void writeJSON(TProtocol protocol, JSONObject obj) throws TException {

    }
*/
}
