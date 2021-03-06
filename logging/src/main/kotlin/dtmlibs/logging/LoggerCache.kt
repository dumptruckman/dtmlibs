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

/**
 * A simple interface for providing [KLogger] objects for [Loggable]s from some sort of cache mechanism.
 */
interface LoggerCache {

    /**
     * Returns the cached logger for the given loggable. If no logger exists in the cache, one is created using
     * the defaultValue and stored in the cache.
     */
    fun getOrPut(loggable: Loggable, defaultValue: (Loggable) -> KLogger): KLogger

    /**
     * Returns the cached logger for the given class. If no logger exists in the cache, one is created using the
     * defaultValue and stored in the cache.
     */
    fun getOrPut(clazz: Class<*>, defaultValue: (Class<*>) -> KLogger): KLogger

    /**
     * Removes any cached logger for the given class.
     */
    fun remove(clazz: Class<*>)
}