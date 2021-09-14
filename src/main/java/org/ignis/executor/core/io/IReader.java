package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public interface IReader {

    Map<Byte, ReaderType> readers = Map.ofEntries(
            Map.entry(IType.I_VOID.id(), new ReaderType((protocol) -> null)),
            Map.entry(IType.I_BOOL.id(), new ReaderType((TProtocol::readBool))),
            Map.entry(IType.I_I08.id(), new ReaderType(TProtocol::readByte)),
            Map.entry(IType.I_I16.id(), new ReaderType(TProtocol::readI16)),
            Map.entry(IType.I_I32.id(), new ReaderType(TProtocol::readI32)),
            Map.entry(IType.I_I64.id(), new ReaderType(TProtocol::readI64)),
            Map.entry(IType.I_DOUBLE.id(), new ReaderType(TProtocol::readDouble)),
            Map.entry(IType.I_STRING.id(), new ReaderType(TProtocol::readString)),
            Map.entry(IType.I_LIST.id(), new ReaderType(IReader::readList)),
            Map.entry(IType.I_SET.id(), new ReaderType(IReader::readSet)),
            Map.entry(IType.I_MAP.id(), new ReaderType(IReader::readMap)),
            Map.entry(IType.I_PAIR.id(), new ReaderType(IReader::readPair)),
            Map.entry(IType.I_BINARY.id(), new ReaderType(IReader::readBinary)),
            Map.entry(IType.I_PAIR_LIST.id(), new ReaderType(IReader::readPairList)),
            Map.entry(IType.I_JSON.id(), new ReaderType(IReader::readJson))
    );


    static byte readType(TProtocol protocol) throws TException {
        return protocol.readByte();
    }

    static Object read(TProtocol protocol) throws TException {
        ReaderType readerType = getReaderType(readType(protocol));
        return readerType.getRead().apply(protocol);
    }

    static ReaderType getReaderType(byte type) {
        return readers.get(type);
    }

    static long readSize(TProtocol protocol) throws TException {
        return protocol.readI64();
    }

    static List<Object> readList(TProtocol protocol) throws TException {
        long size = readSize(protocol);
        byte elemType = readType(protocol);
        ReaderType readerType = getReaderType(elemType);
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(readerType.getRead().apply(protocol));
        }
        return list;
    }

    static Set<Object> readSet(TProtocol protocol) throws TException {
        long size = readSize(protocol);
        byte elemType = readType(protocol);
        ReaderType readerType = getReaderType(elemType);
        Set<Object> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            set.add(readerType.getRead().apply(protocol));
        }
        return set;
    }

    static Map<Object, Object> readMap(TProtocol protocol) throws TException {
        long size = readSize(protocol);
        byte keyType = readType(protocol);
        byte valueType = readType(protocol);
        ReaderType readerTypeKey = getReaderType(keyType);
        ReaderType readerTypeValue = getReaderType(valueType);
        Map<Object, Object> obj = new HashMap<>();
        for (int i = 0; i < size; i++) {
            obj.put(readerTypeKey.getRead().apply(protocol), readerTypeValue.getRead().apply(protocol));
        }
        return obj;
    }

    static Map.Entry<Object, Object> readPair(TProtocol protocol) throws TException {
        byte keyType = readType(protocol);
        byte valueType = readType(protocol);
        ReaderType readerTypeKey = getReaderType(keyType);
        ReaderType readerTypeValue = getReaderType(valueType);
        return new AbstractMap.SimpleEntry<>(
                readerTypeKey.getRead().apply(protocol), readerTypeValue.getRead().apply(protocol)
        );
    }

    static byte[] readBinary(TProtocol protocol) throws TException {
        return protocol.readBinary().array();
    }

    static List<Map.Entry<Object, Object>> readPairList(TProtocol protocol) throws TException {
        long size = readSize(protocol);
        byte keyType = readType(protocol);
        byte valueType = readType(protocol);
        ReaderType readerTypeKey = getReaderType(keyType);
        ReaderType readerTypeValue = getReaderType(valueType);
        List<Map.Entry<Object, Object>> obj = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            obj.add(new AbstractMap.SimpleEntry<>(
                    readerTypeKey.getRead().apply(protocol), readerTypeValue.getRead().apply(protocol)
            ));
        }
        return obj;
    }

    static JSONObject readJson(TProtocol protocol) throws TException {
        Object obj = read(protocol);
        if(obj instanceof Map)
            return new JSONObject((Map<?,?>) obj);
        else return null;
//        if(obj instanceof List) {
//            JSONArray jsonArray = new JSONArray((List<?>) obj);
////            JSONObject jsonObject = new JSONObject();
////            jsonObject.append("r1", jsonArray.get(0));
//            return new JSONObject(jsonArray);
//        }
//        else return null;
    }

}



