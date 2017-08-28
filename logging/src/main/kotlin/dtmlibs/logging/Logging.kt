/*
 * This file is part of dtmlibs.
 *
 * Copyright (c) 2017 Jeremy Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dtmlibs.logging

import mu.KLogger
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

/**
 * A simple logging object for obtaining loggers with specially formatted names.
 *
 * Loggers obtained from this object will be named based on the name of each [Loggable]'s [LogOwner].
 * The default name format is `logOwnerSimpleClassName/loggableSimpleClassName`.
 *
 * A [LogOwner] can be registered with a custom name via [Logging.registerLogOwner]. The name format will then be
 * `logOwnerCustomName/loggableSimpleClassName`.
 */
object Logging {

    /**
     * Sets the [LoggerCache] that will be used for caching loggers.
     */
    @JvmStatic
    fun setLoggerCache(loggerCache: LoggerCache) {
        logCache = loggerCache
    }

    private val logNames = ConcurrentHashMap<Class<out LogOwner>, String>()
    private var logCache: LoggerCache = SimpleLoggerCache()

    /**
     * Registers a [LogOwner]'s name to be used with this Logging library.
     *
     * This name will be used to prepend logging messages for loggables from this owner.
     *
     * @param logOwner The log owner to register.
     * @param name The name to register the log owner with.
     */
    @JvmStatic
    fun registerLogOwner(logOwner: Class<out LogOwner>, name: CharSequence) {
        logNames[logOwner] = name.toString()
    }

    /**
     * Retrieves the [Klogger] for the given [Loggable].
     */
    @JvmStatic
    fun getLogger(loggable: Loggable): KLogger {
        if (loggable.logOwner !in logNames) {
            registerLogOwner(loggable.logOwner, loggable.logOwner.simpleName)
        }
        return logCache.getOrPut(loggable, {
            KotlinLogging.logger("${logNames[loggable.logOwner]}/${loggable::class.java.simpleName}")
        })
    }

}