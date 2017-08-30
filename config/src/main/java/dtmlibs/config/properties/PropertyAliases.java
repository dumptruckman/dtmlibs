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

import java.util.HashMap;
import java.util.Map;

public class PropertyAliases {

    @NotNull
    private static final Map<Class, PropertyAliases> classAliasMap = new HashMap<Class, PropertyAliases>();

    private final Map<String, String[]> aliasMap = new HashMap<String, String[]>();

    private static PropertyAliases getAliases(@NotNull Class clazz) {
        return classAliasMap.get(clazz);
    }

    private static PropertyAliases createAliases(@NotNull Class clazz) {
        PropertyAliases aliases = getAliases(clazz);
        if (aliases == null) {
            aliases = new PropertyAliases();
            classAliasMap.put(clazz, aliases);
        }
        return aliases;
    }

    public static void createAlias(@NotNull Class clazz, @NotNull String alias, @NotNull String... propertyName) {
        PropertyAliases aliases = createAliases(clazz);
        aliases.createAlias(alias, propertyName);
    }

    @Nullable
    public static String[] getPropertyName(@NotNull Class clazz, @NotNull String alias) {
        PropertyAliases aliases = getAliases(clazz);
        if (aliases == null) {
            return null;
        }
        return aliases.getPropertyName(alias);
    }

    private PropertyAliases() { }

    private void createAlias(@NotNull String alias, @NotNull String... propertyName) {
        if (propertyName.length == 0) {
            throw new IllegalArgumentException("propertyName cannot be 0 length array.");
        }
        aliasMap.put(alias.toLowerCase(), propertyName);
    }

    private String[] getPropertyName(@NotNull String alias) {
        return aliasMap.get(alias.toLowerCase());
    }
}
