package org.ignis.executor.api.functions;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;

import java.util.ArrayList;
import java.util.List;

public class MapExecutorToStringFunction implements IFunction {
    @Override
    public void before(IContext context) {
    }

    @Override
    public Object call(Object parts, IContext context) {
        List<List<String>> w = new ArrayList<>();
        for (List<Integer> elems : (List<List<Integer>>) parts) {
            List<String> v = new ArrayList<>();
            for (Integer elem : elems) {
                v.add(elem.toString());
            }
            w.add(v);
        }
        return w;
    }

    @Override
    public void after(IContext context) {

    }
}
