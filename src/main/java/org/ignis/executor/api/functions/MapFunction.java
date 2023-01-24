package org.ignis.executor.api.functions;

import org.apache.logging.log4j.LogManager;
import org.ignis.executor.api.IContext;
import org.ignis.executor.api.function.IFunction;

public class MapFunction implements IFunction {
    @Override
    public void before(IContext context) {
    }
    @Override
    public Object call(Object obj, IContext context) {
        return "|" + obj + "|";
    }

    @Override
    public void after(IContext context) {

    }
}
