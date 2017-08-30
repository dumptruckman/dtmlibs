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
