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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author César Pomar
 */
public class IClientPool {

    private ReentrantLock lock = new ReentrantLock();
    private int port;
    private int compression;
    private List<IClient> clients = new ArrayList<>();
    private LinkedList<IClient> queue = new LinkedList<>();


    public IClientPool(int port, int compression) {
        this.port = port;
        this.compression = compression;
    }

    public void destroy() {
        lock.lock();
        try {
            for (IClient client : this.clients) {
                client.close();
            }
            this.clients.clear();
        } finally {
            lock.unlock();
        }
    }

    public ClientBound getClient() {
        return new IClientPool.ClientBound(this.port, this.compression, this.clients, this.queue, this.lock);
    }


    private class ClientBound {
        private int port;

        private int compression;
        private List<IClient> clients;
        private LinkedList<IClient> queue;

        private ReentrantLock lock;
        private IClient client = null;

        public ClientBound(int port, int compression, List<IClient> clients, LinkedList<IClient> queue, ReentrantLock lock) {
            this.port = port;
            this.compression = compression;
            this.clients = clients;
            this.queue = queue;
            this.lock = lock;
        }

        public IClient enter() {
            this.lock.lock();
            try {
                if (this.queue.size() > 0)
                    this.queue.pop();
                if (this.client == null) {
                    this.client = new IClient(this.port, this.compression);
                    this.lock.lock();
                    try {
                        this.clients.add(this.client);
                    } finally {
                        this.lock.unlock();
                    }
                }
            } catch (TTransportException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                this.lock.unlock();
            }
            return this.client;
        }

        public void exit() {
            this.lock.lock();
            try {
                this.queue.add(this.client);
            } finally {
                this.client = null;
                this.lock.unlock();
            }
        }

    }


}
