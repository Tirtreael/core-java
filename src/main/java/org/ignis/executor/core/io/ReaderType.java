package org.ignis.executor.core.io;

import java.util.concurrent.Callable;

public class ReaderType<K> {

    private final Class<K> type;
    private final Callable<K> read;

    public ReaderType(Class<K> type, Callable<K> read) {
        this.type = type;
        this.read = read;
    }

    public Class<K> getType() {
        return type;
    }

    public Callable<K> getRead() {
        return read;
    }
}
