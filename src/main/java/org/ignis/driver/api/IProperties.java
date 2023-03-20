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

import java.util.Map;
import java.util.Properties;

/**
 * @author César Pomar
 */
public class IProperties extends Properties {

    private long id;

    public long getId() {
        return id;
    }

    IProperties(long properties) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            if (properties > 0) {
                this.id = client.getPropertiesService().newInstance2(properties);
            } else {
                this.id = client.getPropertiesService().newInstance();
            }
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public void set(String key, String value) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getPropertiesService().setProperty(this.id, key, value);
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return client.getPropertiesService().getProperty(this.id, key);
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public String rm(String key) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return client.getPropertiesService().rmProperty(this.id, key);
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean contains(String key) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return client.getPropertiesService().contains(this.id, key);
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> toMap(boolean defaults) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            return client.getPropertiesService().toMap(this.id, defaults);
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void fromMap(Map<String, String> map) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getPropertiesService().fromMap(this.id, map);
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(String path) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getPropertiesService().load(this.id, path);
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void store(String path) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getPropertiesService().store(this.id, path);
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getPropertiesService().clear(this.id);
        } catch (org.ignis.rpc.driver.IDriverException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

}
