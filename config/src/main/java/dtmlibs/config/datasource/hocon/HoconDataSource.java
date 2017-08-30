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
package dtmlibs.config.datasource.hocon;


import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.jetbrains.annotations.NotNull;
import dtmlibs.config.datasource.AbstractDataSource;
import dtmlibs.config.serializers.SerializerSet;

public class HoconDataSource extends AbstractDataSource {

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Used to build a Hocon data source.
     *
     * @see AbstractDataSource.Builder
     */
    public static class Builder extends AbstractDataSource.Builder<Builder> {

        private final HoconConfigurationLoader.Builder builder = HoconConfigurationLoader.builder();

        protected Builder() { }

        @NotNull
        public ConfigRenderOptions getRenderOptions() {
            return builder.getRenderOptions();
        }

        @NotNull
        public ConfigParseOptions getParseOptions() {
            return builder.getParseOptions();
        }

        @NotNull
        public Builder setRenderOptions(@NotNull ConfigRenderOptions options) {
            builder.setRenderOptions(options);
            return this;
        }

        @NotNull
        public Builder setParseOptions(@NotNull ConfigParseOptions options) {
            builder.setParseOptions(options);
            return this;
        }

        @NotNull
        @Override
        public HoconDataSource build() {
            return new HoconDataSource(builder.setSource(source).setSink(sink).build(), getBuiltSerializerSet(), commentsEnabled);
        }
    }

    private HoconDataSource(@NotNull HoconConfigurationLoader loader, @NotNull SerializerSet serializerSet, boolean commentsEnabled) {
        super(loader, serializerSet, commentsEnabled);
    }
}
