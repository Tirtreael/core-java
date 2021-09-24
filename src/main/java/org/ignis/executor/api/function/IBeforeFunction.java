package org.ignis.executor.api.function;

import org.apache.thrift.TException;
import org.ignis.executor.api.IContext;


public interface IBeforeFunction {

    void before(IContext context) throws TException;

}
