package org.ignis.executor.core.io;

public class ReaderType {

    Class<?> type;
    Runnable read;

    public ReaderType(Class<?> type, Runnable read) {
        this.type = type;
        this.read = read;
    }


}
