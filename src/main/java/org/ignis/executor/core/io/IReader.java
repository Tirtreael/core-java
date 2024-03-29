package org.ignis.executor.core.io;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TProtocol;
import org.ignis.executor.api.Pair;
import org.ignis.executor.api.myJson;

import java.util.*;

public interface IReader {

    Map<Byte, ReaderType> readers = Map.ofEntries(
            Map.entry(IEnumTypes.I_VOID.id, new ReaderType((protocol) -> null)),
            Map.entry(IEnumTypes.I_BOOL.id, new ReaderType((TProtocol::readBool))),
            Map.entry(IEnumTypes.I_I08.id, new ReaderType(TProtocol::readByte)),
            Map.entry(IEnumTypes.I_I16.id, new ReaderType(TProtocol::readI16)),
            Map.entry(IEnumTypes.I_I32.id, new ReaderType(TProtocol::readI32)),
            Map.entry(IEnumTypes.I_I64.id, new ReaderType(TProtocol::readI64)),
            Map.entry(IEnumTypes.I_DOUBLE.id, new ReaderType(TProtocol::readDouble)),
            Map.entry(IEnumTypes.I_STRING.id, new ReaderType(TProtocol::readString)),
            Map.entry(IEnumTypes.I_LIST.id, new ReaderType(IReader::readList)),
            Map.entry(IEnumTypes.I_SET.id, new ReaderType(IReader::readSet)),
            Map.entry(IEnumTypes.I_MAP.id, new ReaderType(IReader::readMap)),
            Map.entry(IEnumTypes.I_PAIR.id, new ReaderType(IReader::readPair)),
            Map.entry(IEnumTypes.I_BINARY.id, new ReaderType(IReader::readBinary)),
            Map.entry(IEnumTypes.I_PAIR_LIST.id, new ReaderType(IReader::readPairList)),
            Map.entry(IEnumTypes.I_JSON.id, new ReaderType(IReader::readJson))
    );


    static byte readType(TProtocol protocol) throws TException {
        return protocol.readByte();
    }

    static Object read(TProtocol protocol) throws TException {
        ReaderType readerType = getReaderType(readType(protocol));
        return readerType.getRead().read(protocol);
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
            list.add(readerType.getRead().read(protocol));
        }
        return list;
    }

    static Set<Object> readSet(TProtocol protocol) throws TException {
        long size = readSize(protocol);
        byte elemType = readType(protocol);
        ReaderType readerType = getReaderType(elemType);
        Set<Object> set = new HashSet<>();
        for (int i = 0; i < size; i++) {
            set.add(readerType.getRead().read(protocol));
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
            obj.put(readerTypeKey.getRead().read(protocol), readerTypeValue.getRead().read(protocol));
        }
        return obj;
    }

    static Pair<Object, Object> readPair(TProtocol protocol) throws TException {
        byte keyType = readType(protocol);
        byte valueType = readType(protocol);
        ReaderType readerTypeKey = getReaderType(keyType);
        ReaderType readerTypeValue = getReaderType(valueType);
        return new Pair<>(
                readerTypeKey.getRead().read(protocol), readerTypeValue.getRead().read(protocol)
        );
    }

    static byte[] readBinary(TProtocol protocol) throws TException {
        return protocol.readBinary().array();
    }

    static List<Pair<Object, Object>> readPairList(TProtocol protocol) throws TException {
        long size = readSize(protocol);
        byte keyType = readType(protocol);
        byte valueType = readType(protocol);
        ReaderType readerTypeKey = getReaderType(keyType);
        ReaderType readerTypeValue = getReaderType(valueType);
        List<Pair<Object, Object>> obj = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            obj.add(new Pair<>(
                    readerTypeKey.getRead().read(protocol), readerTypeValue.getRead().read(protocol)
            ));
        }
        return obj;
    }

    static myJson readJson(TProtocol protocol) throws TException {
        Object obj = read(protocol);
        if (obj instanceof Map)
            return new myJson((Map<?, ?>) obj);
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



