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
package dtmlibs.config.datasource.yaml;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

class FileUtil {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Pass a file and it will return it's contents as a string.
     *
     * @param file File to read.
     * @return Contents of file.  String will be empty in case of any errors.
     */
    public static String getFileContentsAsString(@NotNull final File file) throws IOException {
        if (!file.exists())
            throw new IOException("File " + file + " does not exist");
        if (!file.canRead())
            throw new IOException("Cannot read file " + file);
        if (file.isDirectory())
            throw new IOException("File " + file + " is directory");

        Writer writer = new StringWriter();
        InputStream is = null;
        Reader reader = null;
        try {
            is = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            int numberOfCharsRead;
            char[] buffer = new char[BUFFER_SIZE];
            while ((numberOfCharsRead = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, numberOfCharsRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
        }
        return writer.toString();
    }

    public static void writeStringToFile(@NotNull String sourceString, @NotNull final File destinationFile) throws IOException {
        OutputStreamWriter out = null;
        try {
            sourceString = sourceString.replaceAll("\n", System.getProperty("line.separator"));

            out = new OutputStreamWriter(new FileOutputStream(destinationFile), "UTF-8");
            out.write(sourceString);
            out.close();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignore) {
                }
            }
        }
    }
}
