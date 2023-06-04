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

import org.apache.thrift.transport.TTransportException;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class IClientPool {

    private int port;
    private int compression;
    private List<IClient> clients = new ArrayList<>();
    private LinkedList<IClient> queue = new LinkedList<>();


    public IClientPool(int port, int compression) {
        this.port = port;
        this.compression = compression;
    }

    public synchronized void destroy() {
        for (IClient client : this.clients) {
            client.close();
        }
        this.clients.clear();
    }

    public ClientBound getClient() {
        return new IClientPool.ClientBound(this.port, this.compression, this.clients, this.queue);
    }


    public class ClientBound implements Closeable {
        private int port;

        private int compression;
        private List<IClient> clients;
        private LinkedList<IClient> queue;

        private IClient client = null;

        public ClientBound(int port, int compression, List<IClient> clients, LinkedList<IClient> queue) {
            this.port = port;
            this.compression = compression;
            this.clients = clients;
            this.queue = queue;
        }

        public synchronized IClient getClient() {
            try {
                if (this.queue.size() > 0)
                    this.client = this.queue.pop();
                if (this.client == null) {
                    this.client = new IClient(this.port, this.compression);
                    this.clients.add(this.client);
                }
            } catch (TTransportException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            return this.client;
        }

        public synchronized void close() {
            this.queue.add(this.client);
            this.client = null;
        }
    }


}
