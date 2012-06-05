package org.ubiquity.logging.impl;

import org.ubiquity.logging.Logger;

import java.util.logging.Level;

/**
 * TODO : explain.me
 * Date: 05/06/12
 * Time: 11:40
 *
 * @author françois LAROCHE
 */
public class JdkLogging {

    private class JdkLogger implements Logger {
        private final java.util.logging.Logger logger;

        JdkLogger(java.util.logging.Logger logger) {
            this.logger = logger;
        }

        @Override
        public void trace(String message) {
            logger.finest(message);
        }

        @Override
        public void debug(String message) {
            logger.finer(message);
        }

        @Override
        public void info(String message) {
            logger.info(message);
        }

        @Override
        public void warn(String message) {
            logger.warning(message);
        }

        @Override
        public void error(String message) {
            logger.severe(message);
        }

        @Override
        public void error(String message, Throwable e) {
            logger.severe(message + " : " + e.getMessage());
            if(logger.isLoggable(Level.SEVERE)) {
                e.printStackTrace();
            }
        }
    }
}
