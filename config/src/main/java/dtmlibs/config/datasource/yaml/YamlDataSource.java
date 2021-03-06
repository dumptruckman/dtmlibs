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
