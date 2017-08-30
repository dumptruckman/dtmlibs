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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum Validators {
    ;

    private static Map<Class<? extends Validator>, Validator> validatorMap = new HashMap<Class<? extends Validator>, Validator>();

    public static <T, V extends Validator<T>> Validator<T> getValidator(Class<V> validatorClass) {
        if (validatorMap.containsKey(validatorClass)) {
            return validatorMap.get(validatorClass);
        }
        Constructor constructor = null;
        boolean accessible = true;
        try {
            constructor = validatorClass.getDeclaredConstructor();
            accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            Validator validator = (Validator) constructor.newInstance();
            validatorMap.put(validatorClass, validator);
            return validator;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } finally {
            if (constructor != null && !accessible) {
                constructor.setAccessible(accessible);
            }
        }
    }
}
