package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TList;
import org.apache.thrift.protocol.TMap;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TSet;

import java.util.*;

public class Reader implements IReader {

    public final Map<Byte, ReaderType> readers = Map.ofEntries(
            Map.entry(IType.I_VOID.id(), new ReaderType((protocol) -> null)),
            Map.entry(IType.I_BOOL.id(), new ReaderType(TProtocol::readBool)),
            Map.entry(IType.I_I08.id(), new ReaderType(TProtocol::readByte)),
            Map.entry(IType.I_I16.id(), new ReaderType(TProtocol::readI16)),
            Map.entry(IType.I_I32.id(), new ReaderType(TProtocol::readI32)),
            Map.entry(IType.I_I64.id(), new ReaderType(TProtocol::readI64)),
            Map.entry(IType.I_DOUBLE.id(), new ReaderType(TProtocol::readDouble)),
            Map.entry(IType.I_STRING.id(), new ReaderType(TProtocol::readString)),
            Map.entry(IType.I_LIST.id(), new ReaderType(this::readList)),
            Map.entry(IType.I_SET.id(), new ReaderType(this::readSet)),
            Map.entry(IType.I_MAP.id(), new ReaderType(this::readMap)),
            Map.entry(IType.I_PAIR.id(), new ReaderType(this::readPair)),
            Map.entry(IType.I_BINARY.id(), new ReaderType(TProtocol::readBinary)),
            Map.entry(IType.I_PAIR_LIST.id(), new ReaderType(this::readPairList))
    );


    public byte readType(TProtocol protocol) throws TException {
        return protocol.readByte();
    }

    public ReaderType getReaderType(byte type) {
        return readers.get(type);
    }

    public long readSize(TProtocol protocol) throws TException {
        return protocol.readI64();
    }


    public List<Object> readList(TProtocol protocol) throws TException {
        TList tList = protocol.readListBegin();
        long size = tList.size;
        byte elemType = tList.elemType;
        ReaderType readerType = this.getReaderType(elemType);
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(readerType.getRead().apply(protocol));
        }
        protocol.readListEnd();
        return list;
    }

    public Set<Object> readSet(TProtocol protocol) throws TException {
        TSet tSet = protocol.readSetBegin();
        long size = tSet.size;
        byte elemType = tSet.elemType;
        ReaderType readerType = this.getReaderType(elemType);
        Set<Object> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            set.add(readerType.getRead().apply(protocol));
        }
        protocol.readSetEnd();
        return set;
    }

    public Map<Object, Object> readMap(TProtocol protocol) throws TException {
        TMap tMap = protocol.readMapBegin();
        long size = tMap.size;
        byte keyType = tMap.keyType;
        byte valueType = tMap.valueType;
        ReaderType readerTypeKey = this.getReaderType(keyType);
        ReaderType readerTypeValue = this.getReaderType(valueType);
        Map<Object, Object> obj = new HashMap<>();
        for (int i = 0; i < size; i++) {
            obj.put(readerTypeKey.getRead().apply(protocol), readerTypeValue.getRead().apply(protocol));
        }
        return obj;
    }

    public AbstractMap.SimpleEntry<Object, Object> readPair(TProtocol protocol) throws TException {
        byte keyType = this.readType(protocol);
        byte valueType = this.readType(protocol);
        ReaderType readerTypeKey = this.getReaderType(keyType);
        ReaderType readerTypeValue = this.getReaderType(valueType);
        return new AbstractMap.SimpleEntry<>(
                readerTypeKey.getRead().apply(protocol), readerTypeValue.getRead().apply(protocol)
        );
    }


    public byte[] readBinary(TProtocol protocol) throws TException {
        return protocol.readBinary().array().clone();
    }

    public List<Map.Entry<Object, Object>> readPairList(TProtocol protocol) throws TException {
        TList tList = protocol.readListBegin();
        long size = tList.size;
        byte keyType = this.readType(protocol);
        byte valueType = this.readType(protocol);
        ReaderType readerTypeKey = this.getReaderType(keyType);
        ReaderType readerTypeValue = this.getReaderType(valueType);
        List<Map.Entry<Object, Object>> obj = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            obj.add(new AbstractMap.SimpleEntry<>(
                    readerTypeKey.getRead().apply(protocol), readerTypeValue.getRead().apply(protocol)
            ));
        }
        return obj;
    }

}



