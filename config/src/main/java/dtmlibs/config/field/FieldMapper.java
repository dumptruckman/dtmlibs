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

import dtmlibs.logging.Logging;
import mu.KLogger;
import org.jetbrains.annotations.NotNull;
import dtmlibs.config.annotation.FauxEnum;
import dtmlibs.config.annotation.IgnoreSuperFields;
import dtmlibs.config.annotation.SerializeWith;
import dtmlibs.config.serializers.SerializerSet;
import dtmlibs.config.util.PrimitivesUtil;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FieldMapper {

    private static final Map<Class, FieldMap> compiledFieldMaps = new HashMap<Class, FieldMap>();

    private final Class clazz;

    public static <T> T mapFields(T source, T destination) {
        FieldMap sourceMap = getFieldMap(source.getClass());
        destination = mapFields(sourceMap, source, destination);
        return destination;
    }

    private static <T> T mapFields(FieldMap sourceMap, T source, T destination) {
        for (Field field : sourceMap) {
            if (field.hasChildFields()) {
                Object sourceChild = field.getValue(source);
                Object destinationChild = field.getValue(destination);
                if (destinationChild == null) {
                    try {
                        field.setValue(destination, sourceChild);
                    } catch (PropertyVetoException e) {
                        e.printStackTrace();
                    }
                } else {
                    mapFields(field, sourceChild, destinationChild);
                }
            } else {
                try {
                    field.setValue(destination, field.getValue(source));
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
            }
        }
        return destination;
    }

    public static FieldMap getFieldMap(@NotNull Class clazz) {
        if (compiledFieldMaps.containsKey(clazz)) {
            return compiledFieldMaps.get(clazz);
        }
        FieldMapper fieldMapper = new FieldMapper(clazz);
        FieldMap fieldMap = new FieldMap(fieldMapper.mapFields());
        compiledFieldMaps.put(clazz, fieldMap);
        return fieldMap;
    }

    private FieldMapper(@NotNull Class clazz) {
        this.clazz = clazz;
    }

    private Map<String, Field> mapFields() {
        getLogger().trace("Mapping fields for %s", this);
        java.lang.reflect.Field[] allFields = collectAllFieldsForClass(clazz);
        Map<String, Field> resultMap = new LinkedHashMap<String, Field>(allFields.length);
        for (java.lang.reflect.Field field : allFields) {
            Class fieldType = PrimitivesUtil.switchForWrapper(field.getType());
            getLogger().trace("Mapping %s of type %s", field, fieldType);
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                Field localField;
                if (Map.class.isAssignableFrom(fieldType)
                        || Collection.class.isAssignableFrom(fieldType)
                        || Enum.class.isAssignableFrom(fieldType)
                        || field.getType().isAnnotationPresent(FauxEnum.class)
                        || field.isAnnotationPresent(SerializeWith.class)
                        || field.getType().isArray()
                        || SerializerSet.defaultSet().hasSerializerForClass(fieldType)
                        || VirtualField.class.isAssignableFrom(fieldType)) {
                    localField = new Field(field);
                } else {
                    if (fieldType.equals(clazz)) {
                        throw new IllegalStateException("Mapping fields for " + clazz + " would result in infinite recursion due self containment.");
                    }
                    localField = new Field(field, getFieldMap(fieldType));
                }
                resultMap.put(localField.getName().toLowerCase(), localField);
            }
        }
        return resultMap;
    }

    private java.lang.reflect.Field[] collectAllFieldsForClass(Class clazz) {
        java.lang.reflect.Field[] declaredFields = clazz.getDeclaredFields();
        if (clazz.getAnnotation(IgnoreSuperFields.class) != null) {
            return declaredFields;
        }
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            java.lang.reflect.Field[] superFields = collectAllFieldsForClass(superClass);
            int length = declaredFields.length;
            declaredFields = Arrays.copyOf(declaredFields, declaredFields.length + superFields.length);
            System.arraycopy(superFields, 0, declaredFields, length, superFields.length);
        }
        return declaredFields;
    }

    @Override
    public String toString() {
        return "FieldMapper{" +
                "clazz=" + clazz +
                '}';
    }

    private static KLogger getLogger() {
        return Logging.getLogger(FieldMapper.class);
    }
}
