package org.ignis.executor.api.functions;

import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;

public class MapFunction implements IFunction {
    @Override
    public void before(IContext context) {
    }
    @Override
    public Object call(Object obj, IContext context) {
        return Math.cosh(Math.asin(Math.atan(Math.atan(Math.atan((Integer) obj * 2 * 3 + 2 - 135 * 4 % 1464732 - 77 / 10 - 56578 / 11 * 7 + 29 * 2 * 3 + 2 - 135 * 4 % 1464732 - 77 / 10 - 56578 / 11 * 7 + 29)))));
    }

    @Override
    public void after(IContext context) {

    }
}
