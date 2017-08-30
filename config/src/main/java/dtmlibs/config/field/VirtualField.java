package dtmlibs.config.field;

/**
 * Implemented by helper classes that should represent a <i>virtual</i> property.
 * {@link dtmlibs.config.SerializableConfig} does <b>not</b> save virtual properties, however
 * you can use {@link dtmlibs.config.serializers.Serializer}s, {@link Validator}s and all the other
 * awesome features of {@link dtmlibs.config.SerializableConfig} with them.
 * @param <T>
 */
public interface VirtualField<T> {
    /**
     * Called to get this {@link VirtualField}'s value.
     * @return This {@link VirtualField}'s value.
     */
    T get();

    /**
     * Called to set this {@link VirtualField}'s value.
     * @param newValue The new value.
     */
    void set(T newValue);
}
