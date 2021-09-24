package org.ignis.executor.api.function;


import org.ignis.executor.api.IContext;


public interface IFunction {

    void before(IContext context);

    void before(Object obj, IContext context);

    void before(Object obj1, Object obj2, IContext context);

    Object call(IContext context);

    Object call(Object obj, IContext context);

    Object call(Object obj1, Object obj2, IContext context);

    void after(Object obj, IContext context);

    void after(IContext context);

    void after(Object obj1, Object obj2, IContext context);

}
