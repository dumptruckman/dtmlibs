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

import org.junit.Test;

import static org.junit.Assert.*;

public class YamlCommentMapTest {

    @Test
    public void testCommentsForPath() throws Exception {
        YamlCommentMap yamlCommentMap = YamlCommentMap.getYamlCommentMap(2, '.');
        yamlCommentMap.setCommentsForPath("some.path", "test");

        assertEquals("", yamlCommentMap.getCommentsForPath("some"));
        assertEquals("  # test", yamlCommentMap.getCommentsForPath("some.path"));

        yamlCommentMap.setCommentsForPath("some.path");
        assertEquals("", yamlCommentMap.getCommentsForPath("some.path"));

        yamlCommentMap.setCommentsForPath("some", "Test", "", "# Okay", " # Three");
        assertEquals("# Test" + YamlCommentMap.LINE_SEPARATOR + " " + YamlCommentMap.LINE_SEPARATOR + "# Okay" + YamlCommentMap.LINE_SEPARATOR + "#  # Three", yamlCommentMap.getCommentsForPath("some"));

        yamlCommentMap.setCommentsForPath("some.path", "Test", "", "# Okay", " # Three");
        assertEquals("  # Test" + YamlCommentMap.LINE_SEPARATOR + " " + YamlCommentMap.LINE_SEPARATOR + "  # Okay" + YamlCommentMap.LINE_SEPARATOR + "  #  # Three", yamlCommentMap.getCommentsForPath("some.path"));
    }
}
