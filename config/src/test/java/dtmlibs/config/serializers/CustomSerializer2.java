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

import dtmlibs.config.SerializableConfig;
import dtmlibs.config.examples.Custom;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomSerializer2 implements Serializer<Custom> {

    @Nullable
    @Override
    public Object serialize(@Nullable Custom object, @NotNull SerializerSet serializerSet) {
        if (object == null) {
            return null;
        } else {
            Map<String, Object> result = new HashMap<>(3);
            result.put(SerializableConfig.SERIALIZED_TYPE_KEY, Custom.class.getName());
            result.put("name", object.name.toUpperCase());
            result.put("data", SerializableConfig.serialize(object.data));
            return result;
        }
    }

    @Nullable
    @Override
    public Custom deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized instanceof Map) {
            Custom custom = new Custom(((Map) serialized).get("name").toString().toLowerCase());
            List<?> data = ((List<?>)((Map)((Map) serialized).get("data")).get("array"));
            custom.data.array = data.toArray(new Object[data.size()]);
            for (int i = 0; i < data.size(); i++) {
                custom.data.array[i] = Integer.valueOf(String.valueOf(data.get(i)));
            }
            return custom;
        } else {
            throw new IllegalArgumentException("serialized form must be a map");
        }
    }
}
