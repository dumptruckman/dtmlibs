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
import dtmlibs.config.field.Field;
import dtmlibs.config.field.FieldMap;
import dtmlibs.config.field.FieldMapper;
import dtmlibs.config.field.PropertyVetoException;

import java.util.Collection;
import java.util.Map;

/**
 * Implemented by helper classes that are used to serialize objects.
 * <p>
 * @param <T> The type of the object to be serialized.
 */
public interface Serializer<T> {

    /**
     * Transforms the specified object of type {@code T} to a type recognized by a serialization format.
     * <br>
     * The most common format for serialization is YAML.  Types typically recognized by YAML include:
     * <ul>
     *     <li>all primitives and their wrapper classes</li>
     *     <li>{@link String}</li>
     *     <li>{@link java.util.List} of primitives or {@link String}</li>
     *     <li>{@link Map} of primitives or {@link String}</li>
     *     <li>arrays of primitives or {@link String}</li>
     * </ul>
     *
     * @param object the object to serialize.
     * @param serializerSet the serializerSet that contains serializers that may need to be used to serialize child objects.
     * @return the serialized form of the object.
     * @throws IllegalArgumentException if the deserialized object's type is not serializable.
     */
    @Nullable
    Object serialize(@Nullable T object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException;

    /**
     * Transforms the specified object to the type specified by wantedType.
     *
     * @param serialized The object to transform.
     * <br>
     * The type of the serialized object <em>should</em> be one of the following:
     * <ul>
     *     <li>all primitives and their wrapper classes</li>
     *     <li>{@link String}</li>
     *     <li>{@link java.util.List} of primitives or {@link String}</li>
     *     <li>{@link Map} of primitives or {@link String}</li>
     *     <li>arrays of primitives or {@link String}</li>
     * </ul>
     * However, it is possible that serialization could take other forms.<br>
     * @param wantedType The {@link Class} of the object that should be returned.
     * @param serializerSet the serializerSet that contains serializers that may need to be used to deserialize child objects.
     * @return The deserialized form of the serialized object.
     * @throws IllegalArgumentException if the serialized object's type is not deserializable.
     */
    @Nullable
    T deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException;

    @Nullable
    default Object serialize(@Nullable T object) throws IllegalArgumentException {
        return serialize(object, SerializerSet.defaultSet());
    }

    @Nullable
    default T deserialize(@Nullable Object serialized, @NotNull Class wantedType) throws IllegalArgumentException {
        return deserialize(serialized, wantedType, SerializerSet.defaultSet());
    }

    /**
     * This method attempts to map the values contained in the given data Map to the fields of the given target object.
     * <br>
     * The keys in the data map should represent the field names that will be set with the deserialized value.
     *
     * @param data the Map of serialized data with keys representing field names.
     * @param target the object to populate the fields of.
     * @param serializerSet the serializerSet that contains serializers that may need to be used to deserialize child objects.
     * @return The given target object is returned with its fields populated with deserialized data.
     */
    @NotNull
    default T deserializeToObject(@NotNull Map data, @NotNull T target, @NotNull SerializerSet serializerSet) {
        FieldMap fieldMap = FieldMapper.getFieldMap(target.getClass());
        for (Object key : data.keySet()) {
            if (key.equals(SerializableConfig.SERIALIZED_TYPE_KEY)) {
                continue;
            }
            Field field = fieldMap.getField(key.toString());
            if (field != null) {
                Object fieldValue = field.getValue(target);
                Object serializedFieldData = data.get(key);
                if (serializedFieldData == null) {
                    fieldValue = null;
                } else {
                    Class asClass = fieldValue != null ? fieldValue.getClass() : field.getType();
                    if (Collection.class.isAssignableFrom(field.getType()) && serializedFieldData instanceof Collection) {
                        fieldValue = DefaultSerializer.deserializeCollection(field, (Collection<?>) serializedFieldData, asClass, serializerSet);
                    } else if (Map.class.isAssignableFrom(field.getType()) && serializedFieldData instanceof Map) {
                        fieldValue = DefaultSerializer.deserializeMap(field, (Map<?, ?>) serializedFieldData, asClass, serializerSet);
                    } else if (fieldValue != null && serializedFieldData instanceof Map) {
                        fieldValue = DefaultSerializer.deserializeFieldAs(field, serializedFieldData, fieldValue.getClass(), serializerSet);
                    } else {
                        fieldValue = DefaultSerializer.deserializeFieldAs(field, serializedFieldData, field.getType(), serializerSet);
                    }
                }
                try {
                    field.forceSet(target, fieldValue);
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
            } else {
                // TODO fail silently or no?
            }
        }
        return target;
    }

    @NotNull
    default T deserializeToObject(@NotNull Map data, @NotNull T object) {
        return deserializeToObject(data, object, SerializerSet.defaultSet());
    }
}
