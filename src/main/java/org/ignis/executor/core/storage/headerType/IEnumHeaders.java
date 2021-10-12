package org.ignis.executor.core.storage.headerType;

import org.ignis.executor.core.IIEnumTypes;
import org.ignis.executor.core.io.IEnumTypes;

import java.util.HashMap;
import java.util.Map;

public class IEnumHeaders implements IIEnumTypes<IHeaderType> {

    public static final Map<Byte, IHeaderType> headerTypes = new HashMap<>();
    public static final IHeaderType headerTypeBinary = new IHeaderTypeBinary();
    public static final IHeaderType headerTypeList = new IHeaderTypeList();
    public static final IHeaderType headerTypePairList = new IHeaderTypePairList();

    static {
        headerTypes.put(IEnumTypes.I_BINARY.id, headerTypeBinary);
        headerTypes.put(IEnumTypes.I_LIST.id, headerTypeList);
        headerTypes.put(IEnumTypes.I_PAIR_LIST.id, headerTypePairList);
    }

    private IHeaderType headerTypeNative;

    public IHeaderType getHeaderTypeId(byte id, boolean nativ) {
        if (nativ) return this.headerTypeNative;
        IHeaderType headerType = headerTypes.get(id);
        if (headerType != null) return headerType;
        else throw new IllegalArgumentException("IHeaderType not implemented for id " + id);
    }


    public void addType(byte id, IHeaderType iType) {
        headerTypes.put(id, iType);
    }

    @Override
    public void delType(IHeaderType iType) {

    }

    @Override
    public void delType(byte id) {

    }

    @Override
    public IHeaderType getType(byte id) {
        return null;
    }

    @Override
    public void setType(IHeaderType iType) {

    }

    @Override
    public byte getIdClazz(IHeaderType clazz) {
        return 0;
    }
}
