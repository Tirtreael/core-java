package org.ignis.executor.core.io;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.*;

public class IType implements Type {

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
    public static final Map<Type, IType> types = Map.ofEntries(
            Map.entry(IType.I_VOID.type(), IType.I_VOID),
            Map.entry(IType.I_BOOL.type(), IType.I_BOOL),
            Map.entry(IType.I_I08.type(), IType.I_I08),
            Map.entry(IType.I_I16.type(), IType.I_I16),
            Map.entry(IType.I_I32.type(), IType.I_I32),
            Map.entry(IType.I_I64.type(), IType.I_I64),
            Map.entry(IType.I_DOUBLE.type(), IType.I_DOUBLE),
            Map.entry(IType.I_STRING.type(), IType.I_STRING),
            Map.entry(IType.I_LIST.type(), IType.I_LIST),
            Map.entry(IType.I_SET.type(), IType.I_SET),
            Map.entry(IType.I_MAP.type(), IType.I_MAP),
            Map.entry(IType.I_PAIR.type(), IType.I_PAIR),
            Map.entry(IType.I_BINARY.type(), IType.I_BINARY),
            Map.entry(IType.I_PAIR_LIST.type(), IType.I_PAIR_LIST),
            Map.entry(IType.I_JSON.type(), IType.I_JSON)

    );
    private final byte id;
    private final Type type;


    public IType(byte id, Type type) {
        this.id = id;
        this.type = type;
    }

    public byte id() {
        return id;
    }

    public Type type() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (IType) obj;
        return this.id == that.id &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return "IType[" +
                "id=" + id + ", " +
                "type=" + type + ']';
    }


}
