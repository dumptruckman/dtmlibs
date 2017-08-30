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
package dtmlibs.config.util;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for switched primitive classes with their respective wrapper class.
 */
public enum PrimitivesUtil {
    ;

    /** Contains a mapping of primitive classes to their object forms. */
    protected static final Map<Class, Class> PRIMITIVE_WRAPPER_MAP;

    static {
        Map<Class, Class> map = new HashMap<>();
        map.put(int.class, Integer.class);
        map.put(boolean.class, Boolean.class);
        map.put(long.class, Long.class);
        map.put(double.class, Double.class);
        map.put(float.class, Float.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        map.put(char.class, Character.class);
        PRIMITIVE_WRAPPER_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * Switches any primitive classes with their respective wrapper class. Returns the input argument directly for
     * non-primitive classes.
     *
     * @param clazz the class to switch, if primitive.
     * @return the primitive wrapper class for a primitive class input or the input argument if not a primitive class.
     */
    @NotNull
    public static <T> Class<T> switchForWrapper(@NotNull Class<T> clazz) {
        if (PRIMITIVE_WRAPPER_MAP.containsKey(clazz)) {
            clazz = PRIMITIVE_WRAPPER_MAP.get(clazz);
        }
        return clazz;
    }
}
