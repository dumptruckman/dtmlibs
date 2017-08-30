package dtmlibs.config.properties;

import dtmlibs.config.TestBase;
import dtmlibs.config.examples.Comprehensive;
import dtmlibs.config.field.Field;
import dtmlibs.config.field.FieldMap;
import dtmlibs.config.field.FieldMapper;
import dtmlibs.config.field.Validator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class CustomValidatorTest extends TestBase {

    @Test
    public void testCustomValidator() {
        FieldMap fieldMap = FieldMapper.getFieldMap(Comprehensive.class);
        Field field = fieldMap.getField("name");
        assertNotNull(field);
        Validator validator = field.getValidator();
        assertNotNull(validator);
        assertEquals(Comprehensive.NameValidator.class, validator.getClass());

        field = fieldMap.getField("custom");
        assertNotNull(field);
        validator = field.getValidator();
        assertNull(validator);
    }
}
