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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dtmlibs.config.properties.PropertyHandler;

public class FieldInstance {

    private Object containingObject;
    private String[] name;
    private int nameIndex = 0;
    private FieldMap fieldMap;
    private Field field;

    FieldInstance(Object parentObject, @NotNull String... name) {
        this.containingObject = parentObject;
        this.name = name;
    }

    @Nullable
    FieldInstance locateField() {
        fieldMap = FieldMapper.getFieldMap(containingObject.getClass());
        field = fieldMap.getField(name[nameIndex]);
        if (field == null) {
            return null;
        }
        if (nameIndex + 1 < name.length) {
            if (field.hasChildFields()) {
                nameIndex++;
                containingObject = field.getValue(containingObject);
                if (containingObject == null) {
                    return null;
                }
                return locateField();
            } else {
                return null;
            }
        }
        return this;
    }

    @NotNull
    public Class getType() {
        return field.getType();
    }

    @Nullable
    public Class getCollectionType() {
        return field.getCollectionType();
    }

    @Nullable
    public Object getValue() {
        return field.getValue(containingObject);
    }

    public void setValue(@Nullable Object value) throws PropertyVetoException {
        field.setValue(containingObject, value);
    }

    public Validator getValidator() {
        return field.getValidator();
    }

    public boolean isImmutable() {
        return field.isImmutable();
    }

    @NotNull
    public PropertyHandler getPropertyHandler() {
        return field.getPropertyHandler();
    }
}
