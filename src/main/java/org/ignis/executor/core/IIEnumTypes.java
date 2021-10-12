package org.ignis.executor.core;

public interface IIEnumTypes<T> {

    void addType(byte id, T iType);

    void delType(T iType);

    void delType(byte id);

    T getType(byte id);

    void setType(T iType);

//    T getType(Object obj);

//    byte getId(Object obj);

    byte getIdClazz(T iType);

}
