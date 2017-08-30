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
import dtmlibs.config.examples.Child;
import dtmlibs.config.examples.Parent;
import org.junit.Test;

import static org.junit.Assert.*;

public class FieldInstanceTest extends TestBase {

    @Test
    public void testLocateField() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        FieldInstance fieldInstance = Field.getInstance(parent, "achild", "aboolean");

        assertNotNull(fieldInstance);
        assertTrue((Boolean) fieldInstance.getValue());
        fieldInstance.setValue(false);
        assertFalse((Boolean) fieldInstance.getValue());
    }

    @Test
    public void testLocateFieldWithAlias() throws Exception {
        Child child = new Child(true);
        Parent parent = new Parent(child);
        FieldInstance fieldInstance = Field.getInstance(parent, "cbool");

        assertNotNull(fieldInstance);
        assertTrue((Boolean) fieldInstance.getValue());
        fieldInstance.setValue(false);
        assertFalse((Boolean) fieldInstance.getValue());
    }
}
