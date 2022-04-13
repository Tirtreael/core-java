package org.ignis.executor.api.function;


import org.ignis.executor.api.IContext;


public interface IVoidFunction2 extends IFunction2 {

    void before(IContext context);

    Void call(Object obj1, Object obj2, IContext context);

    void after(IContext context);

}
