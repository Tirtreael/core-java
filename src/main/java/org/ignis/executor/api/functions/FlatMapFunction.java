package org.ignis.executor.api.functions;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;

import java.util.ArrayList;
import java.util.List;

public class FlatMapFunction implements IFunction {
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
    public List<Object> call(Object obj, IContext context) {
        return List.of(obj, obj);
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
