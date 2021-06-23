package org.ignis.executor.core.io;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum IEnumTypes {

    I_VOID(0x0, void.class),
    I_BOOL(0x1, boolean.class),
    I_I08(0x2, byte.class),
    I_I16(0x3, short.class),
    I_I32(0x4, int.class),
    I_I64(0x5, long.class),
    I_DOUBLE(0x6, double.class),
    I_STRING(0x7, String.class),
    I_LIST(0x8, List.class),
    I_SET(0x9, Set.class),
    I_MAP(0xa, Map.class),
    I_PAIR(0xb, AbstractMap.SimpleEntry.class),
    I_BINARY(0xc, Byte[].class),
    I_PAIR_LIST(0xd, List.class),
    I_JSON(0xe, JSONObject.class);

    public final int value;
    public final Class<?> clazz;

    IEnumTypes(int i, Class<?> clazz) {
        this.value = i;
        this.clazz = clazz;
    }

    public int getValue() {
        return value;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public IEnumTypes getIEnumType(Class<?> clazz) {
        for (IEnumTypes iEnumType : IEnumTypes.values()) {
            if (iEnumType.clazz.equals(clazz))
                return iEnumType;
        }
        return I_VOID;
    }
//        switch (clazz){
//            case clazz == Void.TYPE: return I_VOID;
//            case Boolean.TYPE: return I_BOOL;
//            case Byte.TYPE: return I_I08;
//            case Short.TYPE: return I_I16;
//            case Integer.TYPE: return I_I32;
//            case Long.TYPE: return I_I64;
//            case double.class: return I_DOUBLE;
//            case String.class: return I_STRING;
//            case List.class : return I_LIST;
//            case Set.class : return I_SET;
//            case Map.class: return I_MAP;
//            case AbstractMap.SimpleEntry.class : return I_PAIR;
//            case Byte[].class : return I_BINARY;
//            case List< : return I_PAIR_LIST;
//            case JSONObject.class : return I_JSON;
//        }
}
