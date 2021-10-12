package org.ignis.executor.core.io;

import org.ignis.executor.core.IIEnumTypes;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IEnumTypes implements IIEnumTypes<IType> {

    public static final IType I_VOID = new IType(0x0, Void.class);
    public static final IType I_BOOL = new IType(0x1, Boolean.class);
    public static final IType I_I08 = new IType(0x2, Byte.class);
    public static final IType I_I16 = new IType(0x3, Short.class);
    public static final IType I_I32 = new IType(0x4, Integer.class);
    public static final IType I_I64 = new IType(0x5, Long.class);
    public static final IType I_DOUBLE = new IType(0x6, Double.class);
    public static final IType I_STRING = new IType(0x7, String.class);
    public static final IType I_LIST = new IType(0x8, List.class);
    public static final IType I_SET = new IType(0x9, Set.class);
    public static final IType I_MAP = new IType(0xa, Map.class);
    public static final IType I_PAIR = new IType(0xb, Map.Entry.class);
    public static final IType I_BINARY = new IType(0xc, byte[].class);
    public static final IType I_PAIR_LIST = new IType(0xd, List.class);
    public static final IType I_JSON = new IType(0xe, JSONObject.class);

    public static final Map<Byte, IType> types = new HashMap<>();

    static {
        addType(I_VOID);
        addType(I_BOOL);
        addType(I_I08);
        addType(I_I16);
        addType(I_I32);
        addType(I_I64);
        addType(I_DOUBLE);
        addType(I_STRING);
        addType(I_LIST);
        addType(I_SET);
        addType(I_MAP);
        addType(I_PAIR);
        addType(I_BINARY);
        addType(I_PAIR_LIST);
        addType(I_JSON);
    }

    public static void addType(byte id, Class<?> clazz) {
        types.put(id, new IType(id, clazz));
    }

    public static void addType(IType iType) {
        types.put(iType.id, iType);
    }

    public static IType getType(Object obj) {
        if (obj instanceof List) {
            if (((List<?>) obj).size() > 0 && ((List<?>) obj).get(0) instanceof Map.Entry) {
                return IEnumTypes.I_PAIR_LIST;
            } else {
                return IEnumTypes.I_LIST;
            }
        } else for (IType t1 : types.values()) {
            if (t1.type == obj.getClass() || t1.type.isAssignableFrom(obj.getClass())) {
                return t1;
            }
        }

        return I_VOID;
    }

    public static byte getId(Object obj) {
        return getType(obj).id;
    }

    public static byte getIdClazz(Class<?> clazz) {
        for (IType t1 : types.values()) {
            if (t1.type == clazz || t1.type.isAssignableFrom(clazz)) {
                return t1.id;
            }
        }
        return 0x0; // Return void id
    }

    @Override
    public void addType(byte id, IType iType) {
        types.put(id, iType);
    }

    public void delType(IType iType) {
        types.remove(iType.id);
    }

    public void delType(byte id) {
        types.remove(id);
    }

    public IType getType(byte id) {
        return types.get(id);
    }

    public void setType(byte id, Class<?> clazz) {
        addType(id, clazz);
    }

    public void setType(IType iType) {
        addType(iType);
    }

    @Override
    public byte getIdClazz(IType iType) {
        return iType.id;
    }

}
