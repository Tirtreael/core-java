package org.ignis.executor.core.io;

public class IType {

    public final byte id;
    public final Class<?> type;

    IType(int id, Class<?> type) {
        this.id = (byte) id;
        this.type = type;
    }

}
