package org.ignis.executor.core.io;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record IType(byte id, Type type) {


    public static final IType I_VOID = new IType((byte) 0x0, void.class);
    public static final IType I_BOOL = new IType((byte) 0x1, boolean.class);
    public static final IType I_I08 = new IType((byte) 0x2, byte.class);
    public static final IType I_I16 = new IType((byte) 0x3, short.class);
    public static final IType I_I32 = new IType((byte) 0x4, int.class);
    public static final IType I_I64 = new IType((byte) 0x5, long.class);
    public static final IType I_DOUBLE = new IType((byte) 0x6, double.class);
    public static final IType I_STRING = new IType((byte) 0x7, String.class);
    public static final IType I_LIST = new IType((byte) 0x8, List.class);
    public static final IType I_SET = new IType((byte) 0x9, Set.class);
    public static final IType I_MAP = new IType((byte) 0xa, Map.class);
    public static final IType I_PAIR = new IType((byte) 0xb, AbstractMap.SimpleEntry.class);
    public static final IType I_BINARY = new IType((byte) 0xc, byte[].class);
    public static final IType I_PAIR_LIST = new IType((byte) 0xd, List.class);
    public static final IType I_JSON = new IType((byte) 0xe, JSONObject.class);

}
