package org.ignis.executor.core.io;

import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum IType {

    I_VOID(0x0, Void.class),
    I_BOOL(0x1, Boolean.class),
    I_I08(0x2, Byte.class),
    I_I16(0x3, Short.class),
    I_I32(0x4, Integer.class),
    I_I64(0x5, Long.class),
    I_DOUBLE(0x6, Double.class),
    I_STRING(0x7, String.class),
    I_LIST(0x8, List.class),
    I_SET(0x9, Set.class),
    I_MAP(0xa, Map.class),
    I_PAIR(0xb, Map.Entry.class),
    I_BINARY(0xc, Byte[].class),
    I_PAIR_LIST(0xd, List.class),
    I_JSON(0xe, JSONObject.class);

    public final byte id;
    public final Class<?> type;

    IType(int id, Class<?> type) {
        this.id = (byte) id;
        this.type = type;
    }

    public byte id() {
        return id;
    }

    public Class<?> type() {
        return type;
    }

    public static byte getId(Object obj) {
        if(obj instanceof List){
            if(((List<?>) obj).size()>0 && ((List<?>) obj).get(0) instanceof AbstractMap.SimpleEntry){
                return IType.I_PAIR_LIST.id;
            }
            else {
                return IType.I_LIST.id;
            }
        }
        else for(IType t1 : IType.values()) {
                if (t1.type.isInstance(obj)){
                    return t1.id;
                }
        }

        return 0x0; // Return void id
    }

    public static byte getIdClazz(Class<?> clazz) {
        for(IType t1 : IType.values()) {
            if (t1.type == clazz || t1.type.isAssignableFrom(clazz)){
                return t1.id;
            }
        }
        return 0x0; // Return void id
    }



}
