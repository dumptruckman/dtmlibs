package dtmlibs.config.datasource.json;


import com.fasterxml.jackson.core.JsonFactory;
import ninja.leaping.configurate.json.FieldValueSeparatorStyle;
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import dtmlibs.config.datasource.AbstractDataSource;
import dtmlibs.config.datasource.serializers.DoubleAsStringSerializer;
import dtmlibs.config.datasource.serializers.LongAsStringSerializer;
import dtmlibs.config.serializers.SerializerSet;

public class JsonDataSource extends AbstractDataSource {

    private static final LongAsStringSerializer LONG_AS_STRING_SERIALIZER = new LongAsStringSerializer();
    private static final DoubleAsStringSerializer DOUBLE_AS_STRING_SERIALIZER  = new DoubleAsStringSerializer();
    private static final SerializerSet DEFAULT_SERIALIZER_SET = SerializerSet.builder()
            .addSerializer(Long.class, () -> LONG_AS_STRING_SERIALIZER)
            .addSerializer(Double.class, () -> DOUBLE_AS_STRING_SERIALIZER)
            .build();

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final JSONConfigurationLoader.Builder builder = JSONConfigurationLoader.builder();

        protected Builder() { }

        public JsonFactory getFactory() {
            return this.builder.getFactory();
        }

        public Builder setIndent(int indent) {
            builder.setIndent(indent);
            return this;
        }

        public Builder setFieldValueSeparatorStyle(FieldValueSeparatorStyle style) {
            builder.setFieldValueSeparatorStyle(style);
            return this;
        }

        @Override
        protected SerializerSet getDataSourceDefaultSerializerSet() {
            return DEFAULT_SERIALIZER_SET;
        }

        @NotNull
        @Override
        public JsonDataSource build() {
            return new JsonDataSource(builder.setSource(source).setSink(sink).build(), getBuiltSerializerSet());
        }
    }

    private JsonDataSource(@NotNull JSONConfigurationLoader loader, @NotNull SerializerSet serializerSet) {
        super(loader, serializerSet, false);
    }
}
