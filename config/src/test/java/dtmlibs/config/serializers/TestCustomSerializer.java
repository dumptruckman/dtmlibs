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
package dtmlibs.config.serializers;

import dtmlibs.config.TestBase;
import dtmlibs.config.examples.Comprehensive;
import dtmlibs.config.field.Field;
import dtmlibs.config.field.FieldMap;
import dtmlibs.config.field.FieldMapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCustomSerializer extends TestBase {

    @Test
    public void testCustomSerializer() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);
        Field field = fieldMap.getField("name");
        assertNotNull(field);
        Serializer serializer = field.getSerializer();
        assertEquals(StringSerializer.class, serializer.getClass());

        field = fieldMap.getField("custom");
        assertNotNull(field);
        serializer = field.getSerializer();
        assertEquals(CustomSerializer.class, serializer.getClass());

        field = fieldMap.getField("custom2");
        assertNotNull(field);
        serializer = field.getSerializer();
        assertEquals(CustomSerializer2.class, serializer.getClass());
    }
}
