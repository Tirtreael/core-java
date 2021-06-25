package org.ignis.executor.core.io;

import java.util.concurrent.Callable;

public record ReaderType<K>(Class<K> type, Callable<K> read) {

}
