package org.ignis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TMultiplexedProcessor;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IGeneralModule;
import org.ignis.executor.core.modules.IIOModule;
import org.ignis.executor.core.modules.impl.GeneralModule;
import org.ignis.executor.core.modules.impl.IOModule;

public final class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {


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

        class IExecutorServerModuleImpl extends org.ignis.executor.core.modules.impl.IExecutorServerModuleImpl {

            final IExecutorData executorData;

            IExecutorServerModuleImpl(IExecutorData executorData) {
                super(executorData);
                this.executorData = executorData;
            }

            void createServices(TMultiplexedProcessor processor) {
                IIOModule io = new IOModule(executorData);
                processor.registerProcessor("IIO", new org.ignis.rpc.executor.IIOModule.Processor<>(io));
                IGeneralModule general = new GeneralModule(executorData);
                processor.registerProcessor("IGeneral", new org.ignis.rpc.executor.IGeneralModule.Processor<>(general));
            }
        }
        IExecutorData executorData = new IExecutorData();
        IExecutorServerModuleImpl server = new IExecutorServerModuleImpl(executorData);
//        server"IExecutorServer", port, compression);


        // =========================
        /* Testing */
//        try {
//            Map<String, IFunction> myFuns = executorData.loadLibraryFunctions("artifacts/FunctionExample.jar");
//            System.out.println(myFuns.keySet());
//            for (IFunction fun : myFuns.values()) {
//                fun.before(null);
//            }
//            LOGGER.error("Este es un test de error");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }
}
