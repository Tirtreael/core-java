package org.ignis.executor.api.functions;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;

public class FilterFunction implements IFunction {
    @Override
    public void before(IContext context) {
    }

    @Override
    public void before(Object obj, IContext context) {

    }

    @Override
    public void before(Object obj1, Object obj2, IContext context) {

    }

    @Override
    public Object call(IContext context) {
        return null;
    }

    @Override
    public Object call(Object obj, IContext context) {
        return ((Integer) obj)>50 ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public Object call(Object obj1, Object obj2, IContext context) {
        return null;
    }

    @Override
    public void after(Object obj, IContext context) {

    }

    @Override
    public void after(IContext context) {

    }

    @Override
    public void after(Object obj1, Object obj2, IContext context) {

    }
}
