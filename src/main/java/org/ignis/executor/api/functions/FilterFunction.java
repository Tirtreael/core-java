package org.ignis.executor.api.functions;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;

public class FilterFunction implements IFunction {
    @Override
    public void before(IContext context) {
    }

    @Override
    public Object call(Object obj, IContext context) {
        return ((Integer) obj)>50 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public void after(IContext context) {

    }
}
