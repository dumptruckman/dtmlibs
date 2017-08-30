package dtmlibs.config.annotation;

import dtmlibs.config.serializers.Serializer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to define what {@link Serializer} to use to serialize/deserialize the value of a class field to/from its
 * serialized form.
 * <br>
 * When annotated on a type, this serializer will be the preferred serializer to use for objects of the annotated type
 * except when an override serializers exists in a relevant {@link dtmlibs.config.serializers.SerializerSet}.
 * When annotated on a field, this will <em>always</em> be the preferred serializer to use for that field's value, even
 * over override serializers.
 */
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeWith {
    Class<? extends Serializer> value();
}
