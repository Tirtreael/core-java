package org.ignis.executor.core.storage.header;

import org.ignis.executor.core.IIEnumTypes;
import org.ignis.executor.core.io.IEnumTypes;

import java.util.HashMap;
import java.util.Map;

public class IEnumHeaders implements IIEnumTypes<IHeader> {

    public static final Map<Byte, IHeader> headers = new HashMap<>();

    public static final IHeader headerBinary = new IHeaderTypeBinary(IEnumTypes.I_BINARY.id, IEnumTypes.I_BINARY.type);
    public static final IHeader headerList = new IHeaderTypeList(IEnumTypes.I_LIST.id, IEnumTypes.I_LIST.type);
    public static final IHeader headerPairList = new IHeaderTypePairList(IEnumTypes.I_PAIR_LIST.id, IEnumTypes.I_PAIR_LIST.type);

    static {
        headers.put(IEnumTypes.I_BINARY.id, headerBinary);
        headers.put(IEnumTypes.I_LIST.id, headerList);
        headers.put(IEnumTypes.I_PAIR_LIST.id, headerPairList);
    }

    private IHeader headerTypeNative;

    public IHeader getHeaderTypeId(byte id, boolean nativ) {
        if (nativ) return this.headerTypeNative;
        IHeader headerType = headers.get(id);
        if (headerType != null) return headerType;
        else throw new IllegalArgumentException("IHeader not implemented for id " + id);
    }


    public void addType(byte id, IHeader iType) {
        headers.put(id, iType);
    }

    @Override
    public void delType(IHeader iType) {

    }

    @Override
    public void delType(byte id) {

    }

    @Override
    public IHeader getType(byte id) {
        return null;
    }

    @Override
    public void setType(IHeader iType) {

    }

    @Override
    public byte getIdClazz(IHeader iHeader) {
        return iHeader.id;
    }

    public byte getIdClazz(Class<?> clazz) {
        for (IHeader t1 : headers.values()) {
            if (t1.type == clazz || t1.type.isAssignableFrom(clazz)) {
                return t1.id;
            }
        }
        return 0x0; // Return void id
    }
}