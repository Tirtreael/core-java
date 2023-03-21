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
import org.ignis.rpc.ISource;
import org.ignis.rpc.driver.IDataFrameId;

/**
 * @author César Pomar
 */
public class IDataFrame {

    private final IDataFrameId id;

    public IDataFrame(IDataFrameId id) {
        this.id = id;
    }

    public void setName(String name) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().setName(this.id, name);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void persist(byte cacheLevel) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().persist(this.id, cacheLevel);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void cache() {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().cache(this.id);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void unpersist() {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().unpersist(this.id);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void uncache() {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().uncache(this.id);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void partitions() {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().partitions(this.id);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAsObjectFile(String path, byte compression) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().saveAsObjectFile(this.id, path, compression);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAsTextFile(String path) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().saveAsTextFile(this.id, path);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAsJsonFile(String path, boolean pretty) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().saveAsJsonFile(this.id, path, pretty);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void repartition(long numPartitions, boolean preserveOrdering, boolean global) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().repartition(this.id, numPartitions, preserveOrdering, global);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void partitionByRandom(long numPartitions, int seed) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().partitionByRandom(this.id, numPartitions, seed);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void partitionBy(ISource src, long numPartitions) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().partitionBy(this.id, src, numPartitions);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame map(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().map_(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame filter(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().filter(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame flatmap(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().flatmap(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame keyBy(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().keyBy(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame mapWithIndex(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().mapWithIndex(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame mapPartitions(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().mapPartitions(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame mapPartitionsWithIndex(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().mapPartitionsWithIndex(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame mapExecutor(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().mapExecutor(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame mapExecutorTo(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().mapExecutorTo(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public IDataFrame groupBy(ISource src, long numPartitions) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return new IDataFrame(client.getDataframeService().groupBy(this.id, src));
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    // @ToDo
/*

    public void sort(ISource src) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getDataframeService().sort(this.id, src);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

*/

}
