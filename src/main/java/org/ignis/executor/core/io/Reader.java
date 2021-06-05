package org.ignis.executor.core.io;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Reader implements IReader {

    public static final ReaderType readerTypeVoid = new ReaderType(Void.TYPE, () -> {
    });
    public static final ReaderType readerTypeBool = new ReaderType(Boolean.TYPE, () -> {
    });
    public static final ReaderType readerTypeI08 = new ReaderType(Byte.TYPE, () -> {
    });
    public static final ReaderType readerTypeI16 = new ReaderType(Short.TYPE, () -> {
    });
    public static final ReaderType readerTypeI32 = new ReaderType(Integer.TYPE, () -> {
    });
    public static final ReaderType readerTypeI64 = new ReaderType(Long.TYPE, () -> {
    });
    public static final ReaderType readerTypeDouble = new ReaderType(Double.TYPE, () -> {
    });
    public static final ReaderType readerTypeString = new ReaderType(String.class, () -> {
    });
    public static final ReaderType readerTypeList = new ReaderType(List.class, () -> {
    });
    public static final ReaderType readerTypeSet = new ReaderType(Set.class, () -> {
    });
    public static final ReaderType readerTypeMap = new ReaderType(Map.class, () -> {
    });
    public static final ReaderType readerTypePair = new ReaderType(AbstractMap.SimpleEntry.class, () -> {
    });
    public static final ReaderType readerTypeBinary = new ReaderType(Byte[].class, () -> {
    });
    public static final ReaderType readerTypePairList = new ReaderType(AbstractMap.SimpleEntry[].class, () -> {
    });
    public static final ReaderType readerTypeJson = new ReaderType(JSONObject.class, () -> {
    });
    private IEnumTypes type;

    private Reader(IEnumTypes type) {
        this.type = type;
    }


}



