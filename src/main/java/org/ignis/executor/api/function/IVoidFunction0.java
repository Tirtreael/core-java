package org.ignis.executor.api.function;


import org.ignis.executor.api.IContext;


public interface IVoidFunction0 extends IFunction0 {

    void before(IContext context);

    Void call(IContext context);

    void after(IContext context);

}
