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
package dtmlibs.config.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dtmlibs.config.field.FieldInstance;
import dtmlibs.config.field.PropertyVetoException;

/**
 * A property handler defines how to a property's value is changeable through the methods in {@link Properties}.
 *
 * @param <T> The type of value this handler is responsible for.
 */
public interface PropertyHandler<T> {

    void set(@NotNull FieldInstance field, @NotNull String newValue) throws PropertyVetoException, UnsupportedOperationException;

    void add(@NotNull FieldInstance field, @NotNull String valueToAdd) throws PropertyVetoException, UnsupportedOperationException;

    void remove(@NotNull FieldInstance field, @NotNull String valueToRemove) throws PropertyVetoException, UnsupportedOperationException;

    void clear(@NotNull FieldInstance field, @Nullable String valueToClear) throws PropertyVetoException, UnsupportedOperationException;
}
