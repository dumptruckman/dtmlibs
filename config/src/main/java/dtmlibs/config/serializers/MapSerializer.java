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

import java.util.LinkedHashMap;
import java.util.Map;

class MapSerializer implements Serializer<Map<?, ?>> {

    @Nullable
    @Override
    public Object serialize(@Nullable Map<?, ?> object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (object == null) {
            return null;
        }
        Map<String, Object> result = new LinkedHashMap<>(object.size());
        for (Map.Entry entry : object.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key != null && value != null) {
                result.put(key.toString(), SerializableConfig.serialize(entry.getValue(), serializerSet));
            }
        }
        return result;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Map<?, ?> deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        if (!(serialized instanceof Map)) {
            throw new IllegalArgumentException("Serialized value must be a map to be deserialized as a map");
        }
        Map<?, ?> data = (Map<?, ?>) serialized;
        Map map = createMap(wantedType, data.size());
        for (Map.Entry entry : data.entrySet()) {
            map.put(SerializableConfig.deserialize(entry.getKey(), serializerSet), SerializableConfig.deserialize(entry.getValue(), serializerSet));
        }
        return map;
    }

    @NotNull
    protected static Map createMap(@NotNull Class<? extends Map> wantedType, int size) {
        if (wantedType.isInterface()) {
            return new LinkedHashMap(size);
        } else {
            try {
                Object[] paramValues = new Object[] { size };
                return InstanceUtil.createInstance(wantedType, InstanceUtil.SIZE_PARAM_TYPE_ARRAY, paramValues);
            } catch (RuntimeException e) {
                if (e.getCause() instanceof NoSuchMethodException) {
                    try {
                        return InstanceUtil.createInstance(wantedType);
                    } catch (RuntimeException e1) {
                        if (e1.getCause() instanceof NoSuchMethodException) {
                            return new LinkedHashMap(size);
                        } else {
                            throw e;
                        }
                    }
                } else {
                    throw e;
                }
            }
        }
    }
}
