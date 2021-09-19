package org.ignis.executor.api.function;


import org.apache.thrift.TException;
import org.ignis.executor.api.IContext;


public interface IFunction {

    void before(IContext context) throws TException;

    Object call(IContext context) throws TException;

    Object call(Object elem, IContext context) throws TException;

    Object call(Object elemA, Object elemB, IContext context) throws TException;

    void after(IContext context) throws TException;

}
