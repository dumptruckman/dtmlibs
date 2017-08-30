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
package dtmlibs.config.datasource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import dtmlibs.config.TestBase;
import dtmlibs.config.datasource.gson.GsonDataSource;
import dtmlibs.config.datasource.hocon.HoconDataSource;
import dtmlibs.config.datasource.json.JsonDataSource;
import dtmlibs.config.datasource.yaml.YamlDataSource;
import dtmlibs.config.examples.Comprehensive;
import dtmlibs.config.examples.Parent;

import java.io.File;

import static org.junit.Assert.*;

public class ConfigDataSourceTest extends TestBase {

    File hoconFile = new File("bin/test.conf");
    File jsonFile = new File("bin/test.json");
    File gsonFile = new File("bin/test.gson");
    File yamlFile = new File("bin/test.yml");
    Comprehensive expected;

    @Before
    public void setup() throws Exception {
        hoconFile.getParentFile().mkdirs();
        hoconFile.createNewFile();
        jsonFile.getParentFile().mkdirs();
        jsonFile.createNewFile();
        gsonFile.getParentFile().mkdirs();
        gsonFile.createNewFile();
        yamlFile.getParentFile().mkdirs();
        yamlFile.createNewFile();

        expected = new Comprehensive();
        expected.aInt = 5;
        ((Parent) expected.stringObjectMap.get("parent")).aChild.aBoolean = false;
    }

    @After
    public void tearDown() {
        hoconFile.delete();
        jsonFile.delete();
        gsonFile.delete();
        yamlFile.delete();
    }

    @Test
    public void testHoconConfigDataSource() throws Exception {
        HoconDataSource dataSource = HoconDataSource.builder().setFile(hoconFile).build();
        dataSource.save(expected);
        dataSource = HoconDataSource.builder().setFile(hoconFile).build();
        Comprehensive actual = (Comprehensive) dataSource.load();
        assertEquals(expected, actual);
    }

    @Test
    public void testJsonConfigDataSource() throws Exception {
        JsonDataSource dataSource = JsonDataSource.builder().setFile(jsonFile).build();
        dataSource.save(expected);
        dataSource = JsonDataSource.builder().setFile(jsonFile).build();
        Comprehensive actual = (Comprehensive) dataSource.load();
        assertEquals(expected, actual);
    }

    @Test
    public void testYamlConfigDataSource() throws Exception {
        YamlDataSource dataSource = YamlDataSource.builder().setFile(yamlFile).build();
        dataSource.save(expected);
        dataSource = YamlDataSource.builder().setFile(yamlFile).build();
        Comprehensive actual = (Comprehensive) dataSource.load();
        assertEquals(expected, actual);
    }

    @Test
    public void testGsonConfigDataSource() throws Exception {
        GsonDataSource dataSource = GsonDataSource.builder().setFile(gsonFile).build();
        dataSource.save(expected);
        dataSource = GsonDataSource.builder().setFile(gsonFile).build();
        Comprehensive actual = (Comprehensive) dataSource.load();
        assertEquals(expected, actual);
    }
}
