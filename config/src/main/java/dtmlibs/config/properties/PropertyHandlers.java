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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public enum PropertyHandlers {
    ;

    private static final Map<Class<? extends PropertyHandler>, PropertyHandler> HANDLER_MAP = new HashMap<Class<? extends PropertyHandler>, PropertyHandler>();

    public static <T extends PropertyHandler> PropertyHandler getHandler(Class<T> handlerClass) {
        if (HANDLER_MAP.containsKey(handlerClass)) {
            return HANDLER_MAP.get(handlerClass);
        }
        try {
            Constructor<T> constructor = handlerClass.getDeclaredConstructor();
            boolean accessible = constructor.isAccessible();
            if (!accessible) {
                constructor.setAccessible(true);
            }
            T handler = constructor.newInstance();
            registerHandlerInstance(handlerClass, handler);
            if (!accessible) {
                constructor.setAccessible(false);
            }
            return handler;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static PropertyHandler<Object> getDefaultHandler() {
        return getHandler(DefaultPropertyHandler.class);
    }

    public static void setDefaultHandler(PropertyHandler<Object> handler) {
        HANDLER_MAP.put(DefaultPropertyHandler.class, handler);
    }

    public static <T extends PropertyHandler> void registerHandlerInstance(Class<T> handlerClass, T handler) {
        HANDLER_MAP.put(handlerClass, handler);
    }
}
