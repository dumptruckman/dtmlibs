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

import com.coekie.gentyref.GenericTypeReflector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dtmlibs.config.annotation.Comment;
import dtmlibs.config.annotation.Description;
import dtmlibs.config.annotation.HandlePropertyWith;
import dtmlibs.config.annotation.Immutable;
import dtmlibs.config.annotation.Name;
import dtmlibs.config.annotation.SerializeWith;
import dtmlibs.config.annotation.ValidateWith;
import dtmlibs.config.properties.PropertyAliases;
import dtmlibs.config.properties.PropertyHandler;
import dtmlibs.config.properties.PropertyHandlers;
import dtmlibs.config.serializers.Serializer;
import dtmlibs.config.serializers.SerializerSet;
import dtmlibs.config.util.PrimitivesUtil;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Map;

public class Field extends FieldMap {

    @NotNull
    private final java.lang.reflect.Field field;
    private final boolean persistable;
    private final boolean immutable;
    private final String name;
    private final Type type;
    private final Class typeClass;
    private final Class collectionType;
    private final Class mapType;
    @Nullable
    private final Class<? extends PropertyHandler> propertyHandlerClass;
    @Nullable
    private final Class<? extends Serializer> serializerClass;
    @Nullable
    private final Class<? extends Validator> validatorClass;
    @Nullable
    private final String description;
    @Nullable
    private final String[] comments;

    @Nullable
    public static FieldInstance getInstance(@NotNull Object object, @NotNull String... name) {
        if (name.length == 0) {
            throw new IllegalArgumentException("name cannot be 0 length array.");
        }
        if (name.length == 1) {
            String[] actualName = PropertyAliases.getPropertyName(object.getClass(), name[0]);
            if (actualName != null) {
                name = actualName;
            }
        }
        return new FieldInstance(object, name).locateField();
    }

    Field(@NotNull java.lang.reflect.Field field) {
        this(field, null);
    }

    Field(@NotNull java.lang.reflect.Field field, @Nullable FieldMap children) {
        super(children == null ? null : children.fieldMap);
        this.field = field;
        this.persistable = !Modifier.isTransient(field.getModifiers());
        this.name = getName(field);
        this.immutable = field.getAnnotation(Immutable.class) != null;
        this.type = determineActualType(field);
        this.typeClass = determineTypeClass(type);
        this.collectionType = determineCollectionType(type);
        this.mapType = determineMapType(type);
        this.propertyHandlerClass = getPropertyHandlerClass(field);
        this.serializerClass = getSerializerClass(field);
        this.validatorClass = getValidatorClass(field);
        this.description = getDescription(field);
        this.comments = getComments(field);
    }

    private String getName(@NotNull java.lang.reflect.Field field) {
        Name name = field.getAnnotation(Name.class);
        if (name == null) {
            name = field.getType().getAnnotation(Name.class);
        }
        if (name != null) {
            return name.value();
        } else {
            return field.getName();
        }
    }

    @NotNull
    private Type determineActualType(@NotNull java.lang.reflect.Field field) {
        Type type = field.getGenericType();
        Class clazz = GenericTypeReflector.erase(type);
        if (VirtualField.class.isAssignableFrom(clazz)) {
            type = GenericTypeReflector.getExactSuperType(type, VirtualField.class);
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                type = parameterizedType.getActualTypeArguments()[0];
            }
        }
        return type;
    }

    @NotNull
    private Class determineTypeClass(@NotNull Type type) {
        if (type instanceof Class) {
            return PrimitivesUtil.switchForWrapper((Class) type);
        } else {
            if (type instanceof WildcardType) {
                return Object.class;
            } else {
                return GenericTypeReflector.erase(type);
            }
        }
    }

    @Nullable
    private Class determineCollectionType(@NotNull Type type) {
        if (Collection.class.isAssignableFrom(getType())) {
            Type collectionType = GenericTypeReflector.getExactSuperType(type, Collection.class);
            if (collectionType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) collectionType;
                collectionType = parameterizedType.getActualTypeArguments()[0];
                if (collectionType instanceof Class) {
                    return (Class) collectionType;
                }
            }
            if (collectionType instanceof WildcardType) {
                return Object.class;
            }
            return GenericTypeReflector.erase(collectionType);
        }
        return null;
    }

    @Nullable
    private Class determineMapType(@NotNull Type type) {
        if (Map.class.isAssignableFrom(getType())) {
            Type mapType = GenericTypeReflector.getExactSuperType(type, Map.class);
            if (mapType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) mapType;
                mapType = parameterizedType.getActualTypeArguments()[1];
                if (mapType instanceof Class) {
                    return (Class) mapType;
                }
            }
            if (mapType instanceof WildcardType) {
                return Object.class;
            }
            return GenericTypeReflector.erase(mapType);
        }
        return null;
    }

    @Nullable
    private Class<? extends PropertyHandler> getPropertyHandlerClass(@NotNull java.lang.reflect.Field field) {
        HandlePropertyWith handlePropertyWith = field.getAnnotation(HandlePropertyWith.class);
        if (handlePropertyWith != null) {
            return handlePropertyWith.value();
        } else {
            return null;
        }
    }

    @Nullable
    private Class<? extends Serializer> getSerializerClass(@NotNull java.lang.reflect.Field field) {
        SerializeWith serializeWith = field.getAnnotation(SerializeWith.class);
        if (serializeWith != null) {
            return serializeWith.value();
        } else {
            serializeWith = field.getType().getAnnotation(SerializeWith.class);
            if (serializeWith != null) {
                return serializeWith.value();
            }
        }
        return null;
    }

    @Nullable
    private Class<? extends Validator> getValidatorClass(@NotNull java.lang.reflect.Field field) {
        ValidateWith validateWith = field.getAnnotation(ValidateWith.class);
        if (validateWith != null) {
            return validateWith.value();
        }
        return null;
    }

    @Nullable
    private String getDescription(@NotNull java.lang.reflect.Field field) {
        Description description = field.getAnnotation(Description.class);
        if (description != null) {
            return description.value();
        } else {
            description = field.getType().getAnnotation(Description.class);
            if (description != null) {
                return description.value();
            }
        }
        return null;
    }

    @Nullable
    private String[] getComments(@NotNull java.lang.reflect.Field field) {
        Comment comment = field.getAnnotation(Comment.class);
        if (comment != null) {
            return comment.value();
        } else {
            comment = field.getType().getAnnotation(Comment.class);
            if (comment != null) {
                return comment.value();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public boolean hasChildFields() {
        return fieldMap != null;
    }

    public boolean isPersistable() {
        return persistable;
    }

    public boolean isImmutable() {
        return immutable;
    }

    @Nullable
    public Class getSerializerClass() {
        return serializerClass;
    }

    @NotNull
    public Serializer getSerializer() {
        return getSerializer(SerializerSet.defaultSet());
    }

    @NotNull
    public Serializer getSerializer(@NotNull SerializerSet serializerSet) {
        /* TODO: decide if override serializers are higher priority than @SerializeWith on a field. For now, they are lower priority.
        Serializer serializer = serializerSet.getOverrideSerializer(field.getType());
        if (serializer != null) {
            return serializer;
        }
        */
        if (getSerializerClass() != null) {
            return serializerSet.getSerializerInstance(getSerializerClass());
        }
        return serializerSet.getClassSerializer(getType());
    }

    @Nullable
    public Validator getValidator() {
        return validatorClass != null ? Validators.getValidator(validatorClass) : null;
    }

    @Nullable
    public Object getValue(@NotNull Object object) {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            if (VirtualField.class.isAssignableFrom(field.getType())) {
                VirtualField vProp = (VirtualField) field.get(object);
                return vProp != null ? vProp.get() : null;
            } else {
                return field.get(object);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError("This should never happen.");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The specified object does not contain this field.", e);
        } finally {
            if (!accessible) {
                field.setAccessible(false);
            }
        }
    }

    public void setValue(@NotNull Object object, @Nullable Object value) throws PropertyVetoException {
        if (isImmutable()) {
            return;
        }
        forceSet(object, value);
    }

    public void forceSet(@NotNull Object object, @Nullable Object value) throws PropertyVetoException {
        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            if (VirtualField.class.isAssignableFrom(field.getType())) {
                setVirtualProperty((VirtualField) field.get(object), value);
            } else {
                setProperty(object, value);
            }
        } catch (IllegalAccessException e) {
            throw new IllegalAccessError("This should never happen.");
        } catch (IllegalArgumentException e) {
            try {
                FieldMap fieldMap = FieldMapper.getFieldMap(object.getClass());
                if (fieldMap.hasField(getName())) {
                    throw new IllegalArgumentException("The specified value is not an instance of type " + getType(), e);
                }
            } catch (IllegalArgumentException ignore) { }
            throw new IllegalArgumentException("The specified object does not contain " + this, e);
        } finally {
            if (!accessible) {
                field.setAccessible(false);
            }
        }
    }

    private void setVirtualProperty(@Nullable VirtualField vProp, @Nullable Object value) throws PropertyVetoException {
        if (vProp == null) {
            return;
        }
        Validator validator = getValidator();
        if (validator != null) {
            vProp.set(validator.validateChange(value, vProp.get()));
        } else {
            vProp.set(value);
        }
    }

    private void setProperty(@NotNull Object object, @Nullable Object value) throws IllegalAccessException, PropertyVetoException {
        Validator validator = getValidator();
        if (validator != null) {
            field.set(object, validator.validateChange(value, field.get(object)));
        } else {
            field.set(object, value);
        }
    }

    @NotNull
    public Class getType() {
        return typeClass;
    }

    @Nullable
    public Class getCollectionType() {
        return collectionType;
    }

    @Nullable
    public Class getMapType() {
        return mapType;
    }

    @NotNull
    public PropertyHandler getPropertyHandler() {
        return propertyHandlerClass != null ? PropertyHandlers.getHandler(propertyHandlerClass) : PropertyHandlers.getDefaultHandler();
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String[] getComments() {
        if (this.comments == null) {
            return null;
        }
        String[] comments = new String[this.comments.length];
        System.arraycopy(this.comments, 0, comments, 0, this.comments.length);
        return comments;
    }

    @Override
    public String toString() {
        return "Field{" +
                "field=" + field +
                '}';
    }
}
