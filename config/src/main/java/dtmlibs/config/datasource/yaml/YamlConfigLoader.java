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

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.loader.AbstractConfigurationLoader;
import ninja.leaping.configurate.loader.CommentHandler;
import ninja.leaping.configurate.loader.CommentHandlers;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

class YamlConfigLoader extends AbstractConfigurationLoader<ConfigurationNode> {

    private final ThreadLocal<Yaml> yaml;
    private final boolean commentsEnabled;
    private DumperOptions options;
    private Representer representer;

    public static class Builder extends AbstractConfigurationLoader.Builder<Builder> {

        private final DumperOptions options = new DumperOptions();
        private boolean commentsEnabled = false;

        protected Builder() {
            setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        }

        public Builder setIndent(int indent) {
            options.setIndent(indent);
            return this;
        }

        public Builder setCommentsEnabled(boolean commentsEnabled) {
            this.commentsEnabled = commentsEnabled;
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
         * @param style the appropritae flow style to use
         * @return this
         */
        public Builder setFlowStyle(DumperOptions.FlowStyle style) {
            options.setDefaultFlowStyle(style);
            return this;
        }

        @Override
        public YamlConfigLoader build() {
            return new YamlConfigLoader(this, options, commentsEnabled);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private YamlConfigLoader(@NotNull Builder builder, final DumperOptions options, boolean doComments) {
        super(builder, new CommentHandler[] {CommentHandlers.HASH});
        this.commentsEnabled = doComments; // TODO fix broken comment instrumenter D:
        this.options = options;
        representer = new Representer();
        representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yaml = new ThreadLocal<Yaml>() {
            @Override
            protected Yaml initialValue() {
                return new Yaml(representer, options);
            }
        };
    }

    @Override
    protected void loadInternal(ConfigurationNode node, BufferedReader reader) throws IOException {
        node.setValue(((Yaml)this.yaml.get()).load(reader));
    }

    @Override
    protected void saveInternal(ConfigurationNode node, Writer writer) throws IOException {
        if (commentsEnabled) {
            Object value = node.getValue();
            if (!(value instanceof Map)) {
                throw new IOException("Data must be in the form of a Map");
            }
            String dump = yaml.get().dump(value);
            YamlFileCommentInstrumenter commentInstrumenter = YamlCommentsMapper.createYamlCommentInstrumenter((Map<?, ?>) value, options.getIndent());
            dump = commentInstrumenter.addCommentsToYamlString(dump);
            writer.write(dump);
        } else {
            yaml.get().dump(node.getValue(), writer);
        }
    }

    @Override
    public ConfigurationNode createEmptyNode(ConfigurationOptions options) {
        return SimpleConfigurationNode.root(options);
    }
}
