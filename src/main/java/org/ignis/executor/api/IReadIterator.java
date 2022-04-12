package org.ignis.executor.api;

import java.util.Iterator;

public interface IReadIterator extends Iterator<Object> {

    Object next();

    boolean hasNext();

}
