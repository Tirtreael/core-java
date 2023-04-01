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

import java.util.Map;
import java.util.Properties;

/**
 * @author César Pomar
 */
public class IProperties extends Properties {

    private long id;

    public IProperties() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            this.id = client.getPropertiesService().newInstance();
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public IProperties(long properties) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            this.id = client.getPropertiesService().newInstance2(properties);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public long getId() {
        return id;
    }

    public void set(String key, String value) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getPropertiesService().setProperty(this.id, key, value);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public String get(String key) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return client.getPropertiesService().getProperty(this.id, key);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public String rm(String key) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return client.getPropertiesService().rmProperty(this.id, key);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public boolean contains(String key) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return client.getPropertiesService().contains(this.id, key);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public Map<String, String> toMap(boolean defaults) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            return client.getPropertiesService().toMap(this.id, defaults);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public void fromMap(Map<String, String> map) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getPropertiesService().fromMap(this.id, map);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public void load(String path) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getPropertiesService().load(this.id, path);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public void store(String path) {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getPropertiesService().store(this.id, path);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public void clear() {
        try (IClientPool.ClientBound clientBound = Ignis.getInstance().clientPool().getClient()) {
            IClient client = clientBound.getClient();
            client.getPropertiesService().clear(this.id);
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

}
