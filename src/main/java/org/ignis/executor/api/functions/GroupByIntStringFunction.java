package org.ignis.executor.api.functions;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;

public class GroupByIntStringFunction implements IFunction {
    @Override
    public void before(IContext context) {
    }

    @Override
    public Object call(Object obj, IContext context) {
        return ((String) obj).length();
    }

    @Override
    public void after(IContext context) {

    }
}
