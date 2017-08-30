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
