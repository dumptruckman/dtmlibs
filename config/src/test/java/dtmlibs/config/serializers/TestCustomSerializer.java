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
