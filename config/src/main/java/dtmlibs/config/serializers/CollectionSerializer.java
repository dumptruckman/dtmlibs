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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class CollectionSerializer implements Serializer<Collection> {

    @Nullable
    @Override
    public Object serialize(@Nullable Collection object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (object == null) {
            return null;
        }
        List<Object> serialized = new ArrayList<>(object.size());
        for (Object o : object) {
            serialized.add(SerializableConfig.serialize(o, serializerSet));
        }
        return new CopyOnWriteArrayList<Object>(serialized);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Collection deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        if (!(serialized instanceof Collection)) {
            throw new IllegalArgumentException("Serialized value must be a collection to be deserialized as a collection");
        }
        Collection data = (Collection) serialized;
        Collection collection = createCollection(wantedType, data.size());
        for (Object object : data) {
            collection.add(SerializableConfig.deserialize(object, serializerSet));
        }
        return collection;
    }

    @NotNull
    protected static Collection createCollection(@NotNull Class<? extends Collection> wantedType, int size) {
        if (wantedType.isInterface()) {
            return new ArrayList(size);
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
                            return new ArrayList(size);
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
