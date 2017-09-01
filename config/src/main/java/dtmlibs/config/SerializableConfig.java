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
package dtmlibs.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dtmlibs.config.annotation.SerializableAs;
import dtmlibs.config.serializers.*;
import dtmlibs.config.util.PrimitivesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A singleton used to serialize and deserialize objects to take advantage of all the features provided by
 * Serializable-Config.
 * <br>
 * Serialization is often as simple as calling {@link #serialize(Object)} on the object you want to serialize and
 * {@link #deserialize(Object)} on the object you want to deserialize. However, some classes may require more
 * instruction on how to be serialized in the form of a custom {@link Serializer} which can be added to a class via
 * {@link dtmlibs.config.annotation.SerializeWith} or added to a custom {@link SerializerSet} and passed utilized
 * via {@link #serialize(Object, SerializerSet)} and {@link #deserialize(Object, SerializerSet)}.
 * <br>
 * For simple and direct serialization to and from a storage medium, refer to {@link dtmlibs.config.datasource}.
 *
 * @see SerializerSet
 * @see dtmlibs.config.datasource
 */
public enum SerializableConfig {
    ;

    /**
     * The key string used to represent a serialized type within serialized data.
     */
    public static final String SERIALIZED_TYPE_KEY = "=$$=";

    private static Map<String, Class> serializableAliases = new HashMap<>();

    /**
     * Serializes the given object using the specified serializer set.
     *
     * @param object the object to serialize.
     * @param serializerSet the serializerSet to look for serializers in.
     * @return the serialized object which may be null.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static Object serialize(@NotNull Object object, @NotNull SerializerSet serializerSet) {
        Class clazz = object.getClass();
        clazz = PrimitivesUtil.switchForWrapper(clazz);
        return serializerSet.getClassSerializer(clazz).serialize(object, serializerSet);
    }

    /**
     * Deserializes the given data using the specified serializer set.
     * <br>
     * To use this method to its fullest potential, data should be in the form of a Map with string keys.
     *
     * @param data the data to deserialize.
     * @param serializerSet the serializerSet to look for serializers in.
     * @return the deserialized object which may be null.
     */
    @Nullable
    public static Object deserialize(@Nullable Object data, @NotNull SerializerSet serializerSet) {
        if (data instanceof Map) {
            Map map = (Map) data;
            Class<?> clazz = getClassFromSerializedData(map);
            if (clazz == null) {
                return deserializeAs(data, data.getClass(), serializerSet);
                //throw new IllegalArgumentException("The given data is not valid for type " + map.get(SERIALIZED_TYPE_KEY) + ".  Was the type registered?");
            }
            return deserializeAs(data, clazz, serializerSet);
        } else {
            return data != null ? deserializeAs(data, data.getClass(), serializerSet) : null;
        }
    }

    /**
     * Deserializes the given data in the form of the specified class using the specified serializer set.
     * <br>
     * To use this method to its fullest potential, data should be in the form of a Map with string keys.
     *
     * @param data the data to deserialize.
     * @param clazz the type to deserialize as.
     * @param serializerSet the serializerSet to look for serializers in.
     * @return the deserialized object which may be null.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T deserializeAs(@NotNull Object data, @NotNull Class<T> clazz, @NotNull SerializerSet serializerSet) {
        clazz = PrimitivesUtil.switchForWrapper(clazz);
        return (T) serializerSet.getClassSerializer(clazz).deserialize(data, clazz, serializerSet);
    }

    /**
     * Registers the serialization alias for a class annotated with {@link SerializableAs}. This must be called before
     * any deserialization occurs or else the annotated class will not be properly deserialized.
     *
     * @param clazz The class to register the serialization alias for.
     */
    public static void registerSerializableAsClass(@NotNull Class<?> clazz) {
        if (!clazz.isAnnotationPresent(SerializableAs.class)) {
            throw new IllegalArgumentException("The class must be annotated with SerializableAs");
        }

        SerializableAs serializableAs = clazz.getAnnotation(SerializableAs.class);
        Map<String, Class> newCopy = new HashMap<>(serializableAliases);
        newCopy.put(serializableAs.value(), clazz);
        serializableAliases = newCopy;
    }

    /**
     * Serializes the given object using the {@link SerializerSet#defaultSet()}.
     *
     * @param object the object to serialize.
     * @return the serialized object which may be null.
     */
    @Nullable
    public static Object serialize(@NotNull Object object) {
        return serialize(object, SerializerSet.defaultSet());
    }

    /**
     * Deserializes the given data using the {@link SerializerSet#defaultSet()}.
     * <br>
     * To use this method to its fullest potential, data should be in the form of a Map with string keys.
     *
     * @param data the data to deserialize.
     * @return the deserialized object which may be null.
     */
    @Nullable
    public static Object deserialize(@Nullable Object data) {
        return deserialize(data, SerializerSet.defaultSet());
    }

    /**
     * Deserializes the given data in the form of the specified class using the {@link SerializerSet#defaultSet()}.
     * <br>
     * To use this method to its fullest potential, data should be in the form of a Map with string keys.
     *
     * @param data the data to deserialize.
     * @param clazz the type to deserialize as.
     * @return the deserialized object which may be null.
     */
    @Nullable
    public static <T> T deserializeAs(@NotNull Object data, @NotNull Class<T> clazz) {
        return deserializeAs(data, clazz, SerializerSet.defaultSet());
    }

    /**
     * This method attempts to determine the class type from a given set of data. It looks for the {@link #SERIALIZED_TYPE_KEY}
     * to determine what class the data represents. As such, the data should be in the form of a Map with string keys.
     *
     * @param data the serialized data.
     * @return the class type represented by the data or null if it could not be determined.
     */
    @Nullable
    public static Class getClassFromSerializedData(Map data) {
        Object object = data.get(SERIALIZED_TYPE_KEY);
        if (object == null) {
            return null;
        }
        if (!(object instanceof String)) {
            logger.warn("Serialized type key found in %s should be a string but is not.", data);
            return null;
        }
        String className = (String) object;
        Class clazz = getClassByAlias(className);
        if (clazz != null) {
            return clazz;
        } else {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                logger.warn("Found serialized type key '%s' but could not find an associated class.", className);
                return null;
            }
        }
    }

    @Nullable
    private static Class getClassByAlias(@NotNull String className) {
        return serializableAliases.get(className);
    }

    private static Logger logger = LoggerFactory.getLogger(SerializableConfig.class);
}
