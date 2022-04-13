package org.ignis.executor.api.function;


import org.ignis.executor.api.IContext;


public interface IFunction {

    void before(IContext context);

    Object call(Object obj, IContext context);

    void after(IContext context);

}
