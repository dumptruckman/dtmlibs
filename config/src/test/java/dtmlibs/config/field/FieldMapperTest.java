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
package dtmlibs.config.field;

import dtmlibs.config.TestBase;
import dtmlibs.config.examples.*;
import dtmlibs.config.serializers.CustomSerializer;
import dtmlibs.config.serializers.CustomSerializer2;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldMapperTest extends TestBase {

    @Test
    public void testRecursiveProtection() {
        boolean thrown = false;
        try {
            FieldMapper.getFieldMap(Recursive.class);
        } catch (IllegalStateException e) {
            thrown = e.getMessage().equals("Mapping fields for " + Recursive.class + " would result in infinite recursion due self containment.");
        }
        assertTrue(thrown);
    }

    @Test
    public void testBasic() {
        assertTrue(FieldMapper.getFieldMap(Child.class).hasField("aBoolean"));
    }

    @Test
    public void testParentChild() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Parent.class);
        assertTrue(fieldMap.hasField("aChild"));
        Field childField = fieldMap.getField("aChild");
        assertTrue(childField.hasField("aBoolean"));
    }

    @Test
    public void testValueGet() {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        FieldMap fieldMap = FieldMapper.getFieldMap(Parent.class);
        Field childField = fieldMap.getField("aChild");
        Field booleanField = childField.getField("aBoolean");
        assertTrue((Boolean) booleanField.getValue(child));
    }

    @Test
    public void testFieldGetSerializer() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);

        Field field = fieldMap.getField("custom");
        assertNotNull(field);
        assertEquals(CustomSerializer.class, field.getSerializer().getClass());

        field = fieldMap.getField("custom2");
        assertNotNull(field);
        assertEquals(CustomSerializer2.class, field.getSerializer().getClass());
    }

    @Test
    public void testFieldGetDescription() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);

        Field field = fieldMap.getField("aInt");
        assertNotNull(field);
        assertEquals(Comprehensive.A_INT_DESCRIPTION, field.getDescription());
    }

    @Test
    public void testFieldGetComments() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);

        Field field = fieldMap.getField("aInt");
        assertNotNull(field);
        assertArrayEquals(Comprehensive.A_INT_COMMENTS, field.getComments());
    }

    @Test
    public void testMapFields() {
        Comprehensive expected = new Comprehensive();
        expected.aInt = 125612;
        expected.custom.name = "asdhhojao";
        expected.simpleList.add(new Simple("asdfaghrhah"));
        expected.custom2 = new Custom("135r3trgah");

        Comprehensive actual = new Comprehensive();
        assertFalse(expected.equals(actual));

        FieldMapper.mapFields(expected, actual);

        assertEquals(expected, actual);
    }
}
