package org.ignis.executor.core.io;

import org.json.JSONObject;

import java.util.*;

public class ITypes {

    public static final int I_VOID = 0x0;
    public static final int I_BOOL = 0x1;
    public static final int I_I08 = 0x2;
    public static final int I_I16 = 0x3;
    public static final int I_I32 = 0x4;
    public static final int I_I64 = 0x5;
    public static final int I_DOUBLE = 0x6;
    public static final int I_STRING = 0x7;
    public static final int I_LIST = 0x8;
    public static final int  I_SET = 0x9;
    public static final int I_MAP = 0xa;
    public static final int I_PAIR = 0xb;
    public static final int I_BINARY = 0xc;
    public static final int I_PAIR_LIST = 0xd;
    public static final int I_JSON = 0xe;

    public static final byte SIZE = 15;

    public static final Class<?>[] TYPES = new Class[SIZE];

    static {
        TYPES[I_VOID] = void.class;
        TYPES[I_BOOL] = boolean.class;
        TYPES[I_I08] = byte.class;
        TYPES[I_I16] = short.class;
        TYPES[I_I32] = int.class;
        TYPES[I_I64] = long.class;
        TYPES[I_DOUBLE] = double.class;
        TYPES[I_STRING] = String.class;
        TYPES[I_LIST] = List.class;
        TYPES[I_SET] = Set.class;
        TYPES[I_MAP] = Map.class;
        TYPES[I_PAIR] = AbstractMap.SimpleEntry.class;
        TYPES[I_BINARY] = byte[].class;
        TYPES[I_PAIR_LIST] = List.class;
        TYPES[I_JSON] = JSONObject.class;
    }

    public static <T2> byte getTypeIndex(Class<T2> clazz) {
        for(byte i=0; i<SIZE; i++){
            if(TYPES[i] == clazz){
                return i;
            }
        }
        return I_VOID;
    }

}
