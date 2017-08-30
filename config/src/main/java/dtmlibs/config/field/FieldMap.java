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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class FieldMap implements Iterable<Field> {

    @Nullable
    Map<String, Field> fieldMap;

    FieldMap(@Nullable Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public boolean hasField(@NotNull String fieldName) {
        return fieldMap != null && fieldMap.containsKey(fieldName.toLowerCase());
    }

    @Nullable
    public Field getField(@NotNull String fieldName) {
        return fieldMap == null ? null : fieldMap.get(fieldName.toLowerCase());
    }

    public int size() {
        return fieldMap == null ? 0 : fieldMap.size();
    }

    @Override
    public Iterator<Field> iterator() {
        return new FieldIterator(fieldMap == null ? null : fieldMap.values().iterator());
    }

    private static class FieldIterator implements Iterator<Field> {

        @Nullable
        private Iterator<Field> fieldIterator;

        FieldIterator(@Nullable Iterator<Field> fieldIterator) {
            this.fieldIterator = fieldIterator;
        }

        @Override
        public boolean hasNext() {
            return fieldIterator != null && fieldIterator.hasNext();
        }

        @Override
        public Field next() {
            if (fieldIterator == null) {
                throw new NoSuchElementException();
            }
            return fieldIterator.next();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
