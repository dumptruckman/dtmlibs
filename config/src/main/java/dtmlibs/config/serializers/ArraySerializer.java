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
import dtmlibs.config.SerializableConfig;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

class ArraySerializer implements Serializer {

    @Nullable
    @Override
    public Object serialize(@Nullable Object object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (object == null) {
            return null;
        }
        if (object instanceof int[]) {
            int[] array = (int[]) object;
            List list = new ArrayList<>(array.length);
            for (int i = 0; i < array.length; i++) {
                list.add(i, SerializableConfig.serialize(array[i], serializerSet));
            }
            return list;
        } else if (object instanceof long[]) {
            long[] array = (long[]) object;
            List list = new ArrayList<>(array.length);
            for (int i = 0; i < array.length; i++) {
                list.add(i, SerializableConfig.serialize(array[i], serializerSet));
            }
            return list;
        } else if (object instanceof double[]) {
            double[] array = (double[]) object;
            List list = new ArrayList<>(array.length);
            for (int i = 0; i < array.length; i++) {
                list.add(i, SerializableConfig.serialize(array[i], serializerSet));
            }
            return list;
        } else if (object instanceof float[]) {
            float[] array = (float[]) object;
            List list = new ArrayList<>(array.length);
            for (int i = 0; i < array.length; i++) {
                list.add(i, SerializableConfig.serialize(array[i], serializerSet));
            }
            return list;
        } else if (object instanceof short[]) {
            short[] array = (short[]) object;
            List list = new ArrayList<>(array.length);
            for (int i = 0; i < array.length; i++) {
                list.add(i, SerializableConfig.serialize(array[i], serializerSet));
            }
            return list;
        } else if (object instanceof byte[]) {
            byte[] array = (byte[]) object;
            List list = new ArrayList<>(array.length);
            for (int i = 0; i < array.length; i++) {
                list.add(i, SerializableConfig.serialize(array[i], serializerSet));
            }
            return list;
        } else if (object instanceof boolean[]) {
            boolean[] array = (boolean[]) object;
            List list = new ArrayList<>(array.length);
            for (int i = 0; i < array.length; i++) {
                list.add(i, SerializableConfig.serialize(array[i], serializerSet));
            }
            return list;
        } else if (object instanceof Object[]) {
            Object[] array = (Object[]) object;
            List list = new ArrayList<>(array.length);
            for (int i = 0; i < array.length; i++) {
                list.add(i, SerializableConfig.serialize(array[i], serializerSet));
            }
            return list;
        } else {
            throw new IllegalArgumentException("Object is not an array");
        }
    }

    @Nullable
    @Override
    public Object deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        if (!(serialized instanceof List)) {
            throw new IllegalArgumentException("Serialized value must be a list to be deserialized as an array");
        }
        if (!wantedType.isArray()) {
            throw new IllegalArgumentException("Wanted type must be an array to use array deserialization");
        }
        List list = (List) serialized;
        if (wantedType.equals(int[].class)) {
            int[] array = new int[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = SerializableConfig.deserializeAs(list.get(i), int.class, serializerSet);
            }
            return array;
        } else if (wantedType.equals(long[].class)) {
            long[] array = new long[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = SerializableConfig.deserializeAs(list.get(i), long.class, serializerSet);
            }
            return array;
        } else if (wantedType.equals(double[].class)) {
            double[] array = new double[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = SerializableConfig.deserializeAs(list.get(i), double.class, serializerSet);
            }
            return array;
        } else if (wantedType.equals(float[].class)) {
            float[] array = new float[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = SerializableConfig.deserializeAs(list.get(i), float.class, serializerSet);
            }
            return array;
        } else if (wantedType.equals(short[].class)) {
            short[] array = new short[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = SerializableConfig.deserializeAs(list.get(i), short.class, serializerSet);
            }
            return array;
        } else if (wantedType.equals(byte[].class)) {
            byte[] array = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = SerializableConfig.deserializeAs(list.get(i), byte.class, serializerSet);
            }
            return array;
        } else if (wantedType.equals(boolean[].class)) {
            boolean[] array = new boolean[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = SerializableConfig.deserializeAs(list.get(i), boolean.class, serializerSet);
            }
            return array;
        } else if (wantedType.equals(Object[].class)) {
            Object[] array = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                array[i] = SerializableConfig.deserializeAs(list.get(i), wantedType.getComponentType(), serializerSet);
            }
            return array;
        }
        return list.toArray((Object[]) Array.newInstance(wantedType, list.size()));
    }
}
