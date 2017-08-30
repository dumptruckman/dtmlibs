package dtmlibs.config.datasource.yaml;


import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import dtmlibs.config.datasource.AbstractDataSource;
import dtmlibs.config.serializers.SerializerSet;

public class YamlDataSource extends AbstractDataSource {

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final YamlConfigLoader.Builder builder = YamlConfigLoader.builder();

        protected Builder() { }

        public Builder setIndent(int indent) {
            builder.setIndent(indent);
            return this;
        }

        /**
         * Sets the flow style for this configuration
         * Flow: the compact, json-like representation.<br>
         * Example: <code>
         *     {value: [list, of, elements], another: value}
         * </code>
         *
         * Block: expanded, traditional YAML<br>
         * Emample: <code>
         *     value:
         *     - list
         *     - of
         *     - elements
         *     another: value
         * </code>
         *
         * @param style the appropriate flow style to use
         * @return this
         */
        public Builder setFlowStyle(DumperOptions.FlowStyle style) {
            builder.setFlowStyle(style);
            return this;
        }

        @Override
        public Builder setCommentsEnabled(boolean commentsEnabled) {
            super.setCommentsEnabled(commentsEnabled);
            builder.setCommentsEnabled(commentsEnabled);
            return this;
        }

        @NotNull
        @Override
        public YamlDataSource build() {
            return new YamlDataSource(builder.setSource(source).setSink(sink).build(), getBuiltSerializerSet(), commentsEnabled);
        }
    }

    private YamlDataSource(@NotNull YamlConfigLoader loader, @NotNull SerializerSet serializerSet, boolean commentsEnabled) {
        super(loader, serializerSet, commentsEnabled);
    }
}
