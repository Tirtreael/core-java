package org.ignis.executor.api.function;


import org.ignis.executor.api.IContext;


public interface IVoidFunction extends IFunction {

    void before(IContext context);

    Void call(Object obj, IContext context);

    void after(IContext context);

}
