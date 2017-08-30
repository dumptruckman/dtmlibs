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
package dtmlibs.config.properties;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dtmlibs.config.field.FieldInstance;
import dtmlibs.config.field.PropertyVetoException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class DefaultPropertyHandler implements PropertyHandler<Object> {

    private static final Map<Class, Method> VALUE_OF_METHODS = new HashMap<Class, Method>();
    private static final Map<Class, Class> PRIMITIVE_WRAPPER_MAP;

    static {
        Map<Class, Class> map = new HashMap<Class, Class>();
        map.put(int.class, Integer.class);
        map.put(boolean.class, Boolean.class);
        map.put(long.class, Long.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        PRIMITIVE_WRAPPER_MAP = Collections.unmodifiableMap(map);
    }

    protected static Method getValueOfMethod(Class clazz) throws NoSuchMethodException {
        Method method = VALUE_OF_METHODS.get(clazz);
        if (method == null) {
            method = cacheValueOfMethod(clazz);
        }
        return method;
    }

    private static Method cacheValueOfMethod(Class clazz) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod("valueOf", String.class);
        if (Modifier.isStatic(method.getModifiers())) {
            method.setAccessible(true);
            VALUE_OF_METHODS.put(clazz, method);
            return method;
        }
        throw new NoSuchMethodException("valueOf(String) method is not static.");
    }

    private static Object getValueFromString(String value, Class desiredType) {
        if (PRIMITIVE_WRAPPER_MAP.containsKey(desiredType)) {
            desiredType = PRIMITIVE_WRAPPER_MAP.get(desiredType);
        }
        if (desiredType.equals(String.class)) {
            return value;
        }
        if (Enum.class.isAssignableFrom(desiredType)) {
            Enum e = EnumUtil.matchEnum(desiredType, value);
            if (e != null) {
                return e;
            }
        }
        try {
            Method method = getValueOfMethod(desiredType);
            return method.invoke(null, value);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void set(@NotNull FieldInstance field, @NotNull String newValue) throws PropertyVetoException, UnsupportedOperationException {
        if (field.getCollectionType() != null) {
            throw new UnsupportedOperationException();
        }
        field.setValue(getValueFromString(newValue, field.getType()));
    }

    @Override
    public void add(@NotNull FieldInstance field, @NotNull String valueToAdd) throws PropertyVetoException, UnsupportedOperationException {
        if (field.getCollectionType() == null) {
            throw new UnsupportedOperationException();
        }
        Object currentValue = field.getValue();
        if (currentValue == null) {
            throw new IllegalStateException("Cannot modify null collection");
        }
        Collection collection = (Collection) currentValue;
        Object value = getValueFromString(valueToAdd, field.getCollectionType());
        collection.add(value);
    }

    @Override
    public void remove(@NotNull FieldInstance field, @NotNull String valueToRemove) throws PropertyVetoException, UnsupportedOperationException {
        if (field.getCollectionType() == null) {
            throw new UnsupportedOperationException();
        }
        Object currentValue = field.getValue();
        if (currentValue == null) {
            throw new IllegalStateException("Cannot modify null collection");
        }
        Collection collection = (Collection) currentValue;
        Object value = getValueFromString(valueToRemove, field.getCollectionType());
        collection.remove(value);
    }

    @Override
    public void clear(@NotNull FieldInstance field, @Nullable String valueToClear) throws PropertyVetoException, UnsupportedOperationException {
        if (field.getCollectionType() == null) {
            throw new UnsupportedOperationException();
        }
        Object currentValue = field.getValue();
        if (currentValue == null) {
            throw new IllegalStateException("Cannot modify null collection");
        }
        Collection collection = (Collection) currentValue;
        collection.clear();
    }
}
