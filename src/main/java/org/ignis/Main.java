package org.ignis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.transport.TTransportException;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IGeneralModule;
import org.ignis.executor.core.modules.IIOModule;
import org.ignis.executor.core.modules.impl.GeneralModule;
import org.ignis.executor.core.modules.impl.IExecutorServerModuleImpl;
import org.ignis.executor.core.modules.impl.IOModule;

public final class Main {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws TTransportException {


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

            @Override
            public void createServices(TProcessor processor) {
                super.createServices(processor);

                IIOModule io = new IOModule(executorData);
                ((TMultiplexedProcessor) processor).registerProcessor("IIO", new org.ignis.rpc.executor.IIOModule.Processor<>(io));
                IGeneralModule general = new GeneralModule(executorData);
                ((TMultiplexedProcessor) processor).registerProcessor("IGeneral", new org.ignis.rpc.executor.IGeneralModule.Processor<>(general));
            }
        }
        IExecutorData executorData = new IExecutorData();
        IExecutorServerModuleImpl server = new IExecutorServerModuleImpl(executorData);
        server.serve("IExecutorServer", port, compression);

    }
}
