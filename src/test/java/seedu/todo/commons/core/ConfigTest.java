package seedu.todo.commons.core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
/*
    @Test
    public void toString_defaultObject_stringReturned() {
        String defaultConfigAsString = "App title : Address App\n" +
                "Current log level : INFO\n" +
                "Preference file Location : preferences.json\n" +
                "Local data file location : data/addressbook.xml\n" +
                "ToDoList name : MyAddressBook";

        assertEquals(defaultConfigAsString, new Config().toString());
    }*/

    @Test
    public void equalsMethod(){
        Config defaultConfig = new Config();
        assertNotNull(defaultConfig);
        assertTrue(defaultConfig.equals(defaultConfig));
    }


}
