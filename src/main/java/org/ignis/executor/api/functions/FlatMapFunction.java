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
    public List<Object> call(Object obj, IContext context) {
        return List.of(obj, obj);
    }

    @Override
    public void after(IContext context) {

    }
}
