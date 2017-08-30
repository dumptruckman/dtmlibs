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
import dtmlibs.config.examples.Custom;
import dtmlibs.config.examples.Unknown;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultSerializerTest extends TestBase {

    Serializer serializer;

    @Before
    public void setUp() throws Exception {
        serializer = new DefaultSerializer();
    }

    @Test
    public void testSerializeDeserializeComprehensive() throws Exception {
        Comprehensive comprehensive = new Comprehensive();
        Object serializedForm = serializer.serialize(comprehensive, SerializerSet.defaultSet());
        Comprehensive deserializedForm = (Comprehensive) serializer.deserialize(serializedForm, Comprehensive.class, SerializerSet.defaultSet());
        assertEquals(comprehensive, deserializedForm);
    }

    @Test
    public void testSerializeDeserializeComprehensiveNullKeyMap() throws Exception {
        Comprehensive comprehensive = new Comprehensive();
        comprehensive.stringObjectMap.put(null, "something");
        Object serializedForm = serializer.serialize(comprehensive, SerializerSet.defaultSet());
        Comprehensive deserializedForm = (Comprehensive) serializer.deserialize(serializedForm, Comprehensive.class, SerializerSet.defaultSet());
        assertFalse(comprehensive.equals(deserializedForm));
    }

    @Test
    public void testSerializeDeserializeComprehensiveNullValueMap() throws Exception {
        Comprehensive comprehensive = new Comprehensive();
        comprehensive.stringObjectMap.put("something", null);
        Object serializedForm = serializer.serialize(comprehensive, SerializerSet.defaultSet());
        Comprehensive deserializedForm = (Comprehensive) serializer.deserialize(serializedForm, Comprehensive.class, SerializerSet.defaultSet());
        assertFalse(comprehensive.equals(deserializedForm));
    }

    public void testSerializeUnknownObject() throws Exception {
        assertNotNull(serializer.serialize(new Unknown(), SerializerSet.defaultSet()));
    }

    @Test
    public void testSerializeCustom() {
        assertEquals(CustomSerializer.class, SerializerSet.defaultSet().getClassSerializer(Custom.class).getClass());
        Custom custom = new Custom("test");
    }
}
