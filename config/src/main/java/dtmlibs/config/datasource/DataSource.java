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
package dtmlibs.config.datasource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A storage medium for a single data object.
 */
public interface DataSource {

    /**
     * Attempts to extract the given type of data out of this storage medium.
     *
     * @return The data from the storage medium or null if no data exists.
     * @param <ObjectType> The type that the data will be deserialized/cast as when extracted.
     * @throws DataHandlingException if any sort of error occurs with loading, reading, parsing or casting the data.
     */
    @Nullable
    <ObjectType> ObjectType load(Class<ObjectType> wantedType) throws DataHandlingException;

    /**
     * Attempts to extract the data out of this storage medium.
     *
     * @return The data from the storage medium or null if no data exists.
     * @throws DataHandlingException if any sort of error occurs with loading, reading, or parsing the data.
     */
    @Nullable
    Object load() throws DataHandlingException;

    /**
     * Attempts to extract the data out of this storage medium and insert it into the given destination object.
     * <br>
     * Note that if the data is incomplete in the storage medium, it will not replace those missing values in the given
     * destination object.
     *
     * @param destination The object to populated with the data from this storage medium.
     * @param <ObjectType> The return
     * @return The destination object populated with the data from this storage medium or null if no data exists.
     * @throws DataHandlingException if any sort of error occurs with loading, reading, parsing or casting the data.
     */
    @Nullable
    <ObjectType> ObjectType loadToObject(@NotNull ObjectType destination) throws DataHandlingException;

    /**
     * Saves the given object to this storage medium.
     *
     * @param object The object to place into this storage medium.
     * @throws DataHandlingException if any sort of error occurs with writing or generating the configuration.
     */
    void save(Object object) throws DataHandlingException;
}
