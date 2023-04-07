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

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TZlibTransport;
import org.ignis.rpc.driver.*;

import static java.lang.Thread.sleep;


public class IClient {

    private TZlibTransport transport;
    private IBackendService.Client backendService;
    private IPropertiesService.Client propertiesService;
    private IClusterService.Client clusterService;
    private IWorkerService.Client workerService;
    private IDataFrameService.Client dataframeService;

    public IClient(int port, int compression) throws TTransportException, InterruptedException {
        transport = new TZlibTransport(new TSocket("localhost", port), compression);
        for (int i = 0; i < 10; i++) {
            try {
                transport.open();
                break;
            } catch (TTransportException e) {
                if (i == 9)
                    throw new RuntimeException(e);
                sleep(i);
            }
        }

        TProtocol protocol = new TCompactProtocol(transport);
        this.backendService = new IBackendService.Client(new TMultiplexedProtocol(protocol, "IBackend"));
        this.propertiesService = new IPropertiesService.Client(new TMultiplexedProtocol(protocol, "IProperties"));
        this.clusterService = new IClusterService.Client(new TMultiplexedProtocol(protocol, "ICluster"));
        this.workerService = new IWorkerService.Client(new TMultiplexedProtocol(protocol, "IWorker"));
        this.dataframeService = new IDataFrameService.Client(new TMultiplexedProtocol(protocol, "IDataFrame"));
    }

    public IBackendService.Client getBackendService() {
        return backendService;
    }

    public IPropertiesService.Client getPropertiesService() {
        return propertiesService;
    }

    public IClusterService.Client getClusterService() {
        return clusterService;
    }

    public IWorkerService.Client getWorkerService() {
        return workerService;
    }

    public IDataFrameService.Client getDataframeService() {
        return dataframeService;
    }

    public void close() {
        this.transport.close();
    }
}
