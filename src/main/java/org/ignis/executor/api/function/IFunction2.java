package org.ignis.executor.api.function;


import org.ignis.executor.api.IContext;


public interface IFunction2 {

    void before(IContext context);

    Object call(Object obj1, Object obj2, IContext context);

    void after(IContext context);

}
