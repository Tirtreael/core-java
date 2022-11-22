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
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.TZlibTransport;
import org.ignis.rpc.driver.IBackendService;
import org.ignis.rpc.driver.IClusterService;
import org.ignis.rpc.driver.IPropertiesService;
import org.ignis.rpc.driver.IWorkerService;

import static java.lang.Thread.sleep;

/**
 * @author César Pomar
 */
public class IClient {

    private TZlibTransport transport;
    private IBackendService backendService;
    private IPropertiesService propertiesService;
    private IClusterService clusterService;
    private IWorkerService workerService;
    private org.ignis.rpc.driver.IDataFrameId dataFrameId;

    public IClient(int port, int compression) throws TTransportException, InterruptedException {
        transport = new TZlibTransport(new TSocket("localhost", port), compression);
        for (int i = 0; i < 10; i++) {
            try{
                transport.open();
                break;
            } catch (TTransportException e) {
                if(i==9)
                    throw new RuntimeException(e);
                sleep(i);
            }
        }

        TProtocol protocol = new TCompactProtocol(transport);



    }
}
