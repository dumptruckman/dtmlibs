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
package dtmlibs.config.datasource.gson;


import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import dtmlibs.config.datasource.AbstractDataSource;
import dtmlibs.config.datasource.serializers.LongAsStringSerializer;
import dtmlibs.config.serializers.SerializerSet;

public class GsonDataSource extends AbstractDataSource {

    private static final LongAsStringSerializer LONG_AS_STRING_SERIALIZER = new LongAsStringSerializer();
    private static final SerializerSet DEFAULT_SERIALIZER_SET = SerializerSet.builder().addSerializer(Long.class, () -> LONG_AS_STRING_SERIALIZER).build();

    /**
     * Returns the default serializer set used for a Gson data source.
     * <br>
     * Long values are given special treatment in this set.
     *
     * @return the default serializer set used for a Gson data source.
     */
    public static SerializerSet defaultSerializerSet() {
        return DEFAULT_SERIALIZER_SET;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final GsonConfigurationLoader.Builder builder = GsonConfigurationLoader.builder();

        protected Builder() { }

        public Builder setLenient(boolean lenient) {
            builder.setLenient(lenient);
            return this;
        }

        @Override
        protected SerializerSet getDataSourceDefaultSerializerSet() {
            return DEFAULT_SERIALIZER_SET;
        }

        @NotNull
        @Override
        public GsonDataSource build() {
            return new GsonDataSource(builder.setSource(source).setSink(sink).build(), getBuiltSerializerSet());
        }
    }

    private GsonDataSource(@NotNull GsonConfigurationLoader loader, @NotNull SerializerSet serializerSet) {
        super(loader, serializerSet, false);
    }
}
