package dtmlibs.config.serializers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

class LocaleSerializer implements Serializer<Locale> {

    @Nullable
    @Override
    public Object serialize(@Nullable Locale locale, @NotNull SerializerSet serializerSet) {
        return locale != null ? locale.toString() : Locale.getDefault();
    }

    @Nullable
    @Override
    public Locale deserialize(@Nullable Object object, @NotNull Class wantedType, @NotNull SerializerSet serializerSet) throws IllegalArgumentException {
        if (object == null) {
            return Locale.getDefault();
        }
        String[] split = object.toString().split("_");
        switch (split.length) {
            case 1:
                return new Locale(split[0]);
            case 2:
                return new Locale(split[0], split[1]);
            case 3:
                return new Locale(split[0], split[1], split[2]);
            default:
                return new Locale(object.toString());
        }
    }
}
