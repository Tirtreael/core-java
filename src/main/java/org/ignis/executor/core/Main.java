package org.ignis.executor.core;

import org.ignis.executor.api.function.IFunction;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class Main {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        IExecutorData executorData = new IExecutorData();

        try {

            Map<String, IFunction> myFuns = executorData.loadLibrary("out/artifacts/jarFuncEx2/jarFuncEx2.jar");
            for (IFunction fun : myFuns.values()) {
                fun.before(null);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
