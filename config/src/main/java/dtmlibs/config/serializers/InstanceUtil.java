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

import java.lang.reflect.Constructor;

class InstanceUtil {

    private static final Class[] EMPTY_PARAM_TYPE_ARRAY = new Class[0];
    private static final Object[] EMPTY_PARAM_VALUE_ARRAY = new Object[0];
    static final Class[] SIZE_PARAM_TYPE_ARRAY = new Class[] {Integer.class};

    @NotNull
    static <T> T createInstance(@NotNull Class<T> wantedType, @NotNull Class[] parameterTypes, @NotNull Object[] parameterValues) {
        try {
            Constructor<T> constructor = wantedType.getDeclaredConstructor(parameterTypes);
            boolean accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            try {
                return constructor.newInstance(parameterValues);
            } finally {
                if (!accessible) {
                    constructor.setAccessible(false);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    static <T> T createInstance(@NotNull Class<T> wantedType) {
        return createInstance(wantedType, EMPTY_PARAM_TYPE_ARRAY, EMPTY_PARAM_VALUE_ARRAY);
    }
}
