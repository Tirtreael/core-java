package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TMap;
import org.apache.thrift.protocol.TSet;
import org.ignis.executor.core.protocol.IObjectProtocol;

import java.util.*;

public class Reader implements IReader {

    private IObjectProtocol protocol;
    private final ReaderType<?>[] readers = {
            new ReaderType<>(void.class, () -> null),
            new ReaderType<>(boolean.class, () -> protocol.readBool()),
            new ReaderType<>(byte.class, () -> protocol.readByte()),
            new ReaderType<>(short.class, () -> protocol.readI16()),
            new ReaderType<>(int.class, () -> protocol.readI32()),
            new ReaderType<>(long.class, () -> protocol.readI64()),
            new ReaderType<>(double.class, () -> protocol.readDouble()),
            new ReaderType<>(String.class, () -> protocol.readString()),
            new ReaderType<>(List.class, () -> this.readList(protocol)),
            new ReaderType<>(Set.class, () -> this.readSet(protocol)),
            new ReaderType<>(Map.class, () -> this.readMap(protocol)),
            new ReaderType<>(AbstractMap.SimpleEntry.class, () -> this.readPair(protocol)),
            new ReaderType<>(byte[].class, () -> this.readBinary(protocol)),
            new ReaderType<>(List.class, () -> this.readPairList(protocol)),
//            new ReaderType<>(JSONObject.class, () -> protocol.readByte())
    };

//    private final Callable<Boolean> readBool = () -> protocol.readBool();
//    private final Callable<Byte> readByte = () -> protocol.readByte();
//    private final Callable<Short> readShort = () -> protocol.readI16();
//    private final Callable<Integer> readInteger = () -> protocol.readI32();
//    private final Callable<Long> readLong = () -> protocol.readI64();
//    private final Callable<Double> readDouble = () -> protocol.readDouble();
//    private final Callable<String> readString = () -> protocol.readString();

    public Reader(IObjectProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public byte readTypeAux(IObjectProtocol protocol) throws TException {
        return protocol.readByte();
    }

    @Override
    public ReaderType<?>[] getReaders() {
        return readers;
    }

    @Override
    public ReaderType<?> getReaderType(int typeId) {
        return readers[typeId];
    }

    @Override
    public long readSizeAux(IObjectProtocol protocol) throws TException {
        return protocol.readI64();
    }


    @Override
    public List<Object> readList(IObjectProtocol protocol) throws Exception {
        TList tList = protocol.readListBegin();
        long size = tList.size;
        byte elemType = tList.elemType;
        List<Object> obj = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            obj.add(this.readers[elemType].getRead().call());
        }
        return obj;
    }

    @Override
    public Set<Object> readSet(IObjectProtocol protocol) throws Exception {
        TSet tSet = protocol.readSetBegin();
        long size = tSet.size;
        byte elemType = tSet.elemType;
        Set<Object> obj = new HashSet<>();
        for (int i = 0; i < size; i++) {
            obj.add(this.readers[elemType].getRead().call());
        }
        return obj;
    }

    @Override
    public Map<Object, Object> readMap(IObjectProtocol protocol) throws Exception {
        TMap tMap = protocol.readMapBegin();
        long size = tMap.size;
        byte keyType = tMap.keyType;
        byte valueType = tMap.valueType;
        Map<Object, Object> obj = new HashMap<>();
        for (int i = 0; i < size; i++) {
            obj.put(this.readers[keyType].getRead().call(), this.readers[valueType].getRead().call());
        }
        return obj;
    }

    @Override
    public AbstractMap.SimpleEntry<Object, Object> readPair(IObjectProtocol protocol) throws Exception {
        byte keyType = this.readTypeAux(protocol);
        byte valueType = this.readTypeAux(protocol);
        return new AbstractMap.SimpleEntry<>(
                this.readers[keyType].getRead().call(),
                this.readers[valueType].getRead().call()
        );
    }

    @Override
    public byte[] readBinary(IObjectProtocol protocol) throws Exception {
        return protocol.readBinary().array().clone();
    }

    @Override
    public List<AbstractMap.SimpleEntry<Object, Object>> readPairList(IObjectProtocol protocol) throws Exception {
        TList tList = protocol.readListBegin();
        long size = tList.size;
        byte keyType = this.readTypeAux(protocol);
        byte valueType = this.readTypeAux(protocol);
        List<AbstractMap.SimpleEntry<Object, Object>> obj = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            obj.add(new AbstractMap.SimpleEntry<>(
                    this.readers[keyType].getRead().call(),
                    this.readers[valueType].getRead().call()
            ));
        }
        return obj;
    }

}



