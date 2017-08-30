package dtmlibs.config.datasource.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import dtmlibs.config.serializers.Serializer;
import dtmlibs.config.serializers.SerializerSet;

public class LongAsStringSerializer implements Serializer<Long> {

    @Nullable
    @Override
    public Object serialize(@Nullable Long object, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        return object != null ? object.toString() : null;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Long deserialize(@Nullable Object serialized, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        try {
            return Long.valueOf(String.valueOf(serialized));
        } catch (Exception e) {
            throw new RuntimeException("There was a problem deserializing a primitive number: " + serialized, e);
        }
    }
}
