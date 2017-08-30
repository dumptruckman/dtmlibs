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
package dtmlibs.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

class NumberSerializer<N extends Number> implements Serializer<N> {

    @Nullable
    @Override
    public Object serialize(@Nullable N object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public N deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        try {
            Method valueOf = wantedType.getMethod("valueOf", String.class);
            return (N) valueOf.invoke(null, String.valueOf(serialized));
        } catch (Exception e) {
            throw new RuntimeException("There was a problem deserializing a primitive number: " + serialized, e);
        }
    }

    static class BigNumberSerializer<N extends Number> implements Serializer<N> {

        @Nullable
        @Override
        public Object serialize(@Nullable N object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            return object.toString();
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public N deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            try {
                return (N) InstanceUtil.createInstance(wantedType, new Class[] {String.class}, new Object[] {String.valueOf(serialized)});
            } catch (Exception e) {
                throw new RuntimeException("There was a problem deserializing a big number: " + serialized, e);
            }
        }
    }

    static class AtomicLongSerializer implements Serializer<AtomicLong> {

        @Nullable
        @Override
        public Object serialize(@Nullable AtomicLong object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            return object;
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public AtomicLong deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            try {
                return new AtomicLong(Long.valueOf(String.valueOf(serialized)));
            } catch (NumberFormatException e) {
                throw new RuntimeException("There was a problem deserializing an AtomicLong: " + serialized, e);
            }
        }
    }

    static class AtomicIntegerSerializer implements Serializer<AtomicInteger> {

        @Nullable
        @Override
        public Object serialize(@Nullable AtomicInteger object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            return object;
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public AtomicInteger deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
            try {
                return new AtomicInteger(Integer.valueOf(String.valueOf(serialized)));
            } catch (NumberFormatException e) {
                throw new RuntimeException("There was a problem deserializing an AtomicLong: " + serialized, e);
            }
        }
    }
}
