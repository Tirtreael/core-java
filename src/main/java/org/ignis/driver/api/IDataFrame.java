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
import org.ignis.rpc.ISource;
import org.ignis.rpc.driver.IDataFrameId;


public class IDataFrame {

    private final IDataFrameId id;

    public IDataFrame(IDataFrameId id) {
        this.id = id;
    }

    public void setName(String name) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().setName(this.id, name);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void persist(byte cacheLevel) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().persist(this.id, cacheLevel);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void cache() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().cache(this.id);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void unpersist() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().unpersist(this.id);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void uncache() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().uncache(this.id);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void partitions() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().partitions(this.id);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void saveAsObjectFile(String path, byte compression) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().saveAsObjectFile(this.id, path, compression);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void saveAsTextFile(String path) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().saveAsTextFile(this.id, path);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void saveAsJsonFile(String path, boolean pretty) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().saveAsJsonFile(this.id, path, pretty);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void repartition(long numPartitions, boolean preserveOrdering, boolean global) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().repartition(this.id, numPartitions, preserveOrdering, global);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void partitionByRandom(long numPartitions, int seed) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().partitionByRandom(this.id, numPartitions, seed);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void partitionBy(ISource src, long numPartitions) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getDataframeService().partitionBy(this.id, src, numPartitions);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame map(ISource src) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().map_(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame filter(ISource src) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().filter(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame flatmap(ISource src) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().flatmap(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame keyBy(ISource src) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().keyBy(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame mapWithIndex(ISource src) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().mapWithIndex(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame mapPartitions(ISource src) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().mapPartitions(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame mapPartitionsWithIndex(ISource src) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().mapPartitionsWithIndex(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame mapExecutor(ISource src) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().mapExecutor(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame mapExecutorTo(ISource src) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().mapExecutorTo(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public IDataFrame groupBy(ISource src, long numPartitions) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return new IDataFrame(client.getDataframeService().groupBy(this.id, src));
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    // @ToDo
/*

    public void sort(ISource src) {
        try (IClient client = Ignis.getInstance().clientPool().getClient().getClient()) {
            client.getDataframeService().sort(this.id, src);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

*/

}
