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
import dtmlibs.config.field.Field;
import dtmlibs.config.field.FieldMap;
import dtmlibs.config.field.FieldMapper;

import java.util.Map;

class YamlCommentsMapper {

    @NotNull
    private String currentPath;
    private Map<?, ?> configMap = null;
    private FieldMap fieldMap = null;
    @NotNull
    private YamlFileCommentInstrumenter commentInstrumenter;

    public static YamlFileCommentInstrumenter createYamlCommentInstrumenter(@NotNull Map<?, ?> configMap, int indentAmount) {
        return new YamlCommentsMapper("", configMap, YamlFileCommentInstrumenter.createCommentInstrumenter(indentAmount)).getCommentInstrumenter();
    }

    private YamlCommentsMapper(@NotNull String currentPath, @NotNull Map<?, ?> configMap, @NotNull YamlFileCommentInstrumenter commentInstrumenter) {
        this.currentPath = currentPath;
        this.configMap = configMap;
        this.commentInstrumenter = commentInstrumenter;
    }

    private YamlCommentsMapper(@NotNull String currentPath, @NotNull FieldMap fieldMap, @NotNull YamlFileCommentInstrumenter commentInstrumenter) {
        this.currentPath = currentPath;
        this.fieldMap = fieldMap;
        this.commentInstrumenter = commentInstrumenter;
    }

    private YamlFileCommentInstrumenter getCommentInstrumenter() {
        if (configMap != null) {
            processConfigMap();
        } else if (fieldMap != null) {
            processFieldMap();
        }
        return commentInstrumenter;
    }

    private YamlFileCommentInstrumenter processConfigMap() {
        for (Map.Entry<?, ?> entry : configMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            Class entryClass = entry.getValue().getClass();
            if (Map.class.isAssignableFrom(entryClass)) {
                Map<?, ?> configMap = (Map<?, ?>) entry.getValue();
                commentInstrumenter = new YamlCommentsMapper(getNewPath(entry.getKey()), configMap, commentInstrumenter).getCommentInstrumenter();
            } else {
                FieldMap fieldMap = FieldMapper.getFieldMap(entryClass);
                commentInstrumenter = new YamlCommentsMapper(getNewPath(entry.getKey()), fieldMap, commentInstrumenter).getCommentInstrumenter();
            }
        }
        return commentInstrumenter;
    }

    private String getNewPath(Object key) {
        return currentPath + (currentPath.isEmpty() ? "" : ".") + key.toString();
    }

    private YamlFileCommentInstrumenter processFieldMap() {
        for (Field field : fieldMap) {
            String newPath = getNewPath(field);
            commentInstrumenter = new YamlCommentsMapper(newPath, field, commentInstrumenter).getCommentInstrumenter();
            String[] comments = field.getComments();
            if (comments != null) {
                newPath = getNewPath(field);
                commentInstrumenter.setCommentsForPath(newPath, comments);
            }
        }
        return commentInstrumenter;
    }

    private String getNewPath(Field field) {
        return currentPath + (currentPath.isEmpty() ? "" : ".") + field.getName();
    }

}
