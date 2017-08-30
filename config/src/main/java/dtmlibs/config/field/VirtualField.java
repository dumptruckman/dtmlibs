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
package dtmlibs.config.field;

/**
 * Implemented by helper classes that should represent a <i>virtual</i> property.
 * {@link dtmlibs.config.SerializableConfig} does <b>not</b> save virtual properties, however
 * you can use {@link dtmlibs.config.serializers.Serializer}s, {@link Validator}s and all the other
 * awesome features of {@link dtmlibs.config.SerializableConfig} with them.
 * @param <T>
 */
public interface VirtualField<T> {
    /**
     * Called to get this {@link VirtualField}'s value.
     * @return This {@link VirtualField}'s value.
     */
    T get();

    /**
     * Called to set this {@link VirtualField}'s value.
     * @param newValue The new value.
     */
    void set(T newValue);
}
