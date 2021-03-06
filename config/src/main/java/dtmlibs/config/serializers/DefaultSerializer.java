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
import dtmlibs.config.annotation.NoTypeKey;
import dtmlibs.config.annotation.SerializableAs;
import dtmlibs.config.field.Field;
import dtmlibs.config.field.FieldMap;
import dtmlibs.config.field.FieldMapper;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

class DefaultSerializer implements Serializer<Object> {

    @Nullable
    @Override
    public Object serialize(@Nullable Object object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (object == null) {
            return null;
        }
        FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
        Map<String, Object> serializedMap = new LinkedHashMap<>(fieldMap.size() + 1);
        if (!(object.getClass().isAnnotationPresent(NoTypeKey.class) && Modifier.isFinal(object.getClass().getModifiers()))) {
            serializedMap.put(SerializableConfig.SERIALIZED_TYPE_KEY, getAlias(object.getClass()));
        }
        for (Field field : fieldMap) {
            if (field.isPersistable()) {
                serializedMap.put(field.getName(), serializeField(object, field, serializerSet));
            }
        }
        return serializedMap;
    }

    @NotNull
    private String getAlias(@NotNull Class clazz) {
        SerializableAs alias = (SerializableAs) clazz.getAnnotation(SerializableAs.class);

        if ((alias != null) && (alias.value() != null)) {
            return alias.value();
        }

        return clazz.getName();
    }


    @Nullable
    @SuppressWarnings("unchecked")
    protected Object serializeField(@NotNull Object object, @NotNull Field field, @NotNull SerializerSet serializerSet) {
        Object value = field.getValue(object);
        if (value == null) {
            return null;
        }
        return field.getSerializer(serializerSet).serialize(value, serializerSet);
    }


    @Nullable
    @Override
    public Object deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (serialized == null) {
            return null;
        }
        if (wantedType.isAssignableFrom(serialized.getClass())) {
            // Already deserialized
            return serialized;
        }
        if (!(serialized instanceof Map)) {
            throw new IllegalArgumentException("Serialized value must be a map to be deserialized as an object");
        }
        Map data = (Map) serialized;
        Object typeInstance;
        if (wantedType.isEnum()) {
            Object name = data.get("name");
            if (name != null) {
                try {
                    typeInstance = Enum.valueOf(wantedType, name.toString());
                } catch (IllegalArgumentException e) {
                    typeInstance = Enum.valueOf(wantedType, name.toString().toUpperCase());;
                }
            } else {
                throw new IllegalArgumentException("The serialized enum does not contain a name which is required for deserialization");
            }
        } else if (Modifier.isFinal(wantedType.getModifiers())) {
            typeInstance = InstanceUtil.createInstance(wantedType);
        } else {
            Class clazz = SerializableConfig.getClassFromSerializedData(data);
            if (clazz != null) {
                typeInstance = InstanceUtil.createInstance(clazz);
            } else {
                try {
                    typeInstance = InstanceUtil.createInstance(wantedType);
                } catch (RuntimeException e) {
                    throw new IllegalArgumentException("The serialized form does not contain enough information to deserialize", e);
                }
            }
        }
        return deserializeToObject(data, typeInstance, serializerSet);
    }

    protected static Collection<?> deserializeCollection(@NotNull Field field, @NotNull Collection<?> data, @NotNull Class asClass, @NotNull SerializerSet serializerSet) {
        Collection collection = CollectionSerializer.createCollection(asClass, data.size());
        for (Object object : data) {
            Class collectionType = field.getCollectionType();
            if (collectionType != null && !collectionType.equals(Object.class)) {
                collection.add(SerializableConfig.deserializeAs(object, field.getCollectionType(), serializerSet));
            } else {
                collection.add(SerializableConfig.deserialize(object, serializerSet));
            }
        }
        return collection;
    }

    protected static Map<?, ?> deserializeMap(@NotNull Field field, @NotNull Map<?, ?> data, @NotNull Class asClass, @NotNull SerializerSet serializerSet) {
        Map map = MapSerializer.createMap(asClass, data.size());
        for (Map.Entry entry : data.entrySet()) {
            Class mapType = field.getMapType();
            if (mapType != null && !mapType.equals(Object.class)) {
                map.put(SerializableConfig.deserialize(entry.getKey(), serializerSet), SerializableConfig.deserializeAs(entry.getValue(), mapType, serializerSet));
            } else {
                map.put(SerializableConfig.deserialize(entry.getKey(), serializerSet), SerializableConfig.deserialize(entry.getValue(), serializerSet));
            }
        }
        return map;
    }

    protected static Object deserializeFieldAs(@NotNull Field field, @NotNull Object data, @NotNull Class asClass, @NotNull SerializerSet serializerSet) {
        try {
            return field.getSerializer(serializerSet).deserialize(data, asClass, serializerSet);
        } catch (Exception e) {
            throw new RuntimeException("Exception while deserializing field '" + field + "' as class '" + asClass + "'", e);
        }
    }
}
