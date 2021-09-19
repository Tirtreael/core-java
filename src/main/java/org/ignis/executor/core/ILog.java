package org.ignis.executor.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ILog /*extends LogManager*/{

    private static final Logger LOGGER = LogManager.getLogger();

    public ILog() {
    }

    public void enable() {
        LOGGER.atLevel(Level.OFF);
    }
}
