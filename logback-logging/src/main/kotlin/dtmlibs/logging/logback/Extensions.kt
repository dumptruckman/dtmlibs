package dtmlibs.logging.logback

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import dtmlibs.logging.Logging
import org.slf4j.LoggerFactory

/**
 * Sets the default root log level for [Logging].
 */
fun Logging.initialize() {
    this.setRootLogLevel(Level.INFO)
}

/**
 * Sets the root log level to the given level.
 */
fun Logging.setRootLogLevel(level: Level) {
    val logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
    logger.level = level
}