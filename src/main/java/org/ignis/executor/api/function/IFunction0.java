package org.ignis.executor.api.function;


import org.ignis.executor.api.IContext;


public interface IFunction0 {

    void before(IContext context);

    Object call(IContext context);

    void after(IContext context);

}
