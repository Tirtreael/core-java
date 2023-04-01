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
package org.ignis.driver.api;

import org.apache.thrift.TException;
import org.ignis.driver.core.IClient;
import org.ignis.driver.core.IClientPool;
import org.ignis.rpc.driver.IWorkerId;

/**
 * @author César Pomar
 */
public class IWorker {

    private ICluster cluster;
    private IWorkerId id;

    public IWorker(ICluster cluster, String type, String name, int cores, int instances) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            if (name == null) {
                if (cores == 0)
                    this.id = client.getWorkerService().newInstance(cluster.getId(), type);
                else this.id = client.getWorkerService().newInstance4(cluster.getId(), type, cores, instances);
            } else {
                if (cores == 0)
                    this.id = client.getWorkerService().newInstance3(cluster.getId(), name, type);
                else this.id = client.getWorkerService().newInstance5(cluster.getId(), name, type, cores, instances);
            }
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }


    public void start() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getWorkerService().start(this.id);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void destroy() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getWorkerService().destroy(this.id);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public ICluster getCluster() {
        return cluster;
    }

    public IWorkerId getId() {
        return id;
    }

    public void setName(String name) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getWorkerService().setName(this.id, name);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    // @Todo


    public IDataFrame textFile(String path, int minPartitions) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            if (minPartitions < 1)
                return new IDataFrame(client.getWorkerService().textFile(this.id, path));
            else return new IDataFrame(client.getWorkerService().textFile3(this.id, path, minPartitions));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

}
