package org.ignis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TMultiplexedProcessor;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.io.IEnumTypes;
import org.ignis.executor.core.modules.IIOModule;
import org.ignis.executor.core.modules.impl.IOModule;
import org.ignis.rpc.executor.IExecutorServerModule;

import java.util.Map;
import java.util.Queue;

public final class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        IEnumTypes.addType((byte) 0xfa, Queue.class);

        if (args.length < 2) {
            LOGGER.error("Executor need a server port and compression as argument");
            return;
        }
        int port = 7878;
        int compression = 6;
        try {
            port = Integer.parseInt(args[0]);
            compression = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        class IExecutorServerModuleImpl extends IExecutorServerModule {

            final IExecutorData executorData;

            IExecutorServerModuleImpl(IExecutorData executorData) {
                this.executorData = executorData;
            }

            void createServices(TMultiplexedProcessor processor) {
                IIOModule io = new IOModule(executorData, LOGGER);
                processor.registerProcessor("IIO", new org.ignis.rpc.executor.IIOModule.Processor<>(io));
            }
        }
        IExecutorData executorData = new IExecutorData();
        IExecutorServerModuleImpl server = new IExecutorServerModuleImpl(executorData);
//        server"IExecutorServer", port, compression);



        // =========================
        /* Testing */
        try {
            Map<String, IFunction> myFuns = executorData.loadLibrary("out/artifacts/jarFuncEx2/jarFuncEx2.jar");
            for (IFunction fun : myFuns.values()) {
                fun.before(null);
            }
            LOGGER.error("Este es un test de error");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
