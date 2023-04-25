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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TException;
import org.ignis.driver.core.ICallBack;
import org.ignis.driver.core.IClientPool;
import org.ignis.driver.core.IDriverContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Ignis {

    public static final Ignis instance = new Ignis();
    private static final Logger LOGGER = LogManager.getLogger();
    private Process backend;
    private IClientPool pool;
    private ICallBack callback;

    public static Ignis getInstance() {
        return Ignis.instance;
    }

    public synchronized void start() {
        try {
            if (this.pool == null) {
                this.backend = new ProcessBuilder("ignis-backend").start();

                BufferedReader backendStdOut = new BufferedReader(new InputStreamReader(this.backend.getInputStream()));
                int backendPort = Integer.parseInt(backendStdOut.readLine());
                int backendCompression = Integer.parseInt(backendStdOut.readLine());
                int callbackPort = Integer.parseInt(backendStdOut.readLine());
                int callbackCompression = Integer.parseInt(backendStdOut.readLine());

                this.callback = new ICallBack(callbackPort, callbackCompression);
                this.pool = new IClientPool(backendPort, backendCompression);
            }
        } catch (Exception ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public synchronized void stop() {
        try {
            if (this.pool == null)
                return;
            try {
                this.pool.getClient().getClient().getBackendService().stop();
            } catch (TException ex) {
                throw new IDriverException(ex.getMessage(), ex.getCause());
            }

            this.backend.waitFor();
            this.pool.destroy();
            try {
                this.callback.stop();
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            }

            this.backend = null;
            this.pool = null;
            this.callback = null;

        } catch (Exception ex) {
            throw new IDriverException(ex.getMessage(), ex.getCause());
        }
    }

    public IClientPool clientPool() {
        if (this.pool == null) {
            throw new IDriverException("Ignis.start() must be invoked before the other routines");
        }
        return this.pool;
    }

    public IDriverContext driverContext() {
        if (this.callback == null) {
            throw new IDriverException("Ignis.start() must be invoked before the other routines");
        }
        return this.callback.driverContext();
    }
}
