package org.ignis.executor.api.function;


import org.ignis.executor.api.IContext;


public interface IVoidFunction extends IFunction {

    void before(IContext context);

    void before(Object obj, IContext context);

    void before(Object obj1, Object obj2, IContext context);

    Void call(IContext context);

    Void call(Object obj, IContext context);

    Void call(Object obj1, Object obj2, IContext context);

    void after(IContext context);

    void after(Object obj, IContext context);

    void after(Object obj1, Object obj2, IContext context);

}
