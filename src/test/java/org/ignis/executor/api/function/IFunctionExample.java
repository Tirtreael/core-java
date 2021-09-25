package org.ignis.executor.api.function;

import org.ignis.executor.api.IContext;

public class IFunctionExample implements IFunction {
    @Override
    public void before(IContext context) {
        System.out.println("Executing before method from " + getClass().getName());
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
        return null;
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
