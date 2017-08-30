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
