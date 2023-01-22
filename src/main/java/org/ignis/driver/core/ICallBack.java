/*
 * Copyright (C) 2018
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ignis.driver.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TMultiplexedProcessor;
import org.ignis.executor.core.IExecutorData;
import org.ignis.executor.core.modules.IIOModule;
import org.ignis.executor.core.modules.impl.IExecutorServerModuleImpl;
import org.ignis.executor.core.modules.impl.IOModule;
import org.ignis.executor.core.modules.ICacheContextModule;
import org.ignis.rpc.executor.ICommModule;

/**
 * @author César Pomar
 */
public class ICallBack {

    private static final Logger LOGGER = LogManager.getLogger();
    private int port;
    private int compression;

    public ICallBack(int port, int compression) {
        this.port = port;
        this.compression = compression;

        class IIExecutorServerModuleImpl extends IExecutorServerModuleImpl {
            private ICacheContextModule driverContext;

            public IIExecutorServerModuleImpl(IExecutorData executorData, ICacheContextModule driverContext) {
                super(executorData);
                this.driverContext = driverContext;
            }

            public void createServices(TMultiplexedProcessor processor) {
                super.createServices(processor);
                IIOModule io = new IOModule(this.getExecutorData());
//                @ToDo
                processor.registerProcessor("IIO", new org.ignis.rpc.executor.IIOModule.Processor<>(io));
//                processor.registerProcessor("ICacheContext", new org.ignis.rpc.executor.ICacheContextModule.Processor<>(this.driverContext));
//                var comm = new  ICommModule(this.getExecutorData());
            }
        }
    }

}
