package org.ignis.executor.api.functions;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;

public class IFunctionExample implements IFunction {
    @Override
    public void before(IContext context) {
        System.out.println("Executing before method from " + getClass().getName());
    }

    @Override
    public Object call(Object obj, IContext context) {
        return null;
    }

    @Override
    public void after(IContext context) {

    }
}
