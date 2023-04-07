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

import java.util.List;


public class ICluster {

    private long id;

    public ICluster(IProperties properties, String name) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            if (properties == null) {
                if (name == null) {
                    this.id = client.getClusterService().newInstance0();
                } else {
                    this.id = client.getClusterService().newInstance1a(name);
                }
            } else {
                if (name == null) {
                    this.id = client.getClusterService().newInstance1b(properties.getId());
                } else {
                    this.id = client.getClusterService().newInstance2(name, properties.getId());
                }
            }
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public long getId() {
        return id;
    }

    public void start() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getClusterService().start(this.id);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void destroy() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getClusterService().destroy(this.id);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void setName(String name) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getClusterService().setName(this.id, name);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void execute(List<String> cmd) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getClusterService().execute(this.id, cmd);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void executeScript(String script) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getClusterService().executeScript(this.id, script);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void sendFile(String source, String target) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getClusterService().sendFile(this.id, source, target);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }

    public void sendCompressedFile(String source, String target) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getClusterService().sendCompressedFile(this.id, source, target);
        } catch (TException e) {
            throw new IDriverException(e.getMessage(), e.getCause());
        }
    }


}
