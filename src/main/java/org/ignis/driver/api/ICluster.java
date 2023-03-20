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

import java.util.List;


/**
 * @author César Pomar
 */
public class ICluster {

    private long id;

    public ICluster(IProperties properties, String name) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
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
        } catch (TException ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public void start() {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getClusterService().start(this.id);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getClusterService().destroy(this.id);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(String name) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getClusterService().setName(this.id, name);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(List<String> cmd) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getClusterService().execute(this.id, cmd);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeScript(String script) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getClusterService().executeScript(this.id, script);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFile(String source, String target) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getClusterService().sendFile(this.id, source, target);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCompressedFile(String source, String target) {
        try {
            IClient client = Ignis.getInstance().clientPool().getClient().getClient();
            client.getClusterService().sendCompressedFile(this.id, source, target);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }


}
