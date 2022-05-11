package org.ignis.executor.api.functions;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IVoidFunction;

import java.util.List;

public class MapExecutorFunction implements IVoidFunction {
    @Override
    public void before(IContext context) {
    }

    @Override
    public Void call(Object parts, IContext context) {
        for (List<Integer> elems : (List<List<Integer>>) parts) {
            for (int i = 0; i < elems.size(); i++) {
                elems.set(i, elems.get(i) + 1);
            }
        }
        return null;
    }

    @Override
    public void after(IContext context) {

    }
}
