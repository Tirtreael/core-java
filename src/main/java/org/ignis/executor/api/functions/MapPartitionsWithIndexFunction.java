package org.ignis.executor.api.functions;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.api.function.IFunction2;
import org.ignis.executor.core.storage.IMemoryPartition;

import java.util.List;

public class MapPartitionsWithIndexFunction implements IFunction2 {
    @Override
    public void before(IContext context) {
    }
    @Override
    public Object call(Object i, Object obj, IContext context) {
        return new IMemoryPartition.IMemoryReadIterator(List.of(obj));
    }

    @Override
    public void after(IContext context) {

    }
}
