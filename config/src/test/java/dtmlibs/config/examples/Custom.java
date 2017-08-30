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
package dtmlibs.config.examples;

import dtmlibs.config.annotation.Comment;
import dtmlibs.config.annotation.SerializeWith;
import dtmlibs.config.serializers.CustomSerializer;
import dtmlibs.config.util.ObjectStringifier;

import java.util.Arrays;

@SerializeWith(CustomSerializer.class)
public class Custom {

    @Comment("A name")
    public String name;
    @Comment("Some data")
    public Data data = new Data();

    public Custom(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Custom)) return false;

        final Custom custom = (Custom) o;

        if (!name.equals(custom.name)) return false;
        return data.equals(custom.data);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + data.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return ObjectStringifier.toString(this, true);
    }

    public static class Data {

        public Object[] array = new Object[] {1, 2, 3};

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Data)) return false;

            final Data data = (Data) o;

            if (array == null && data.array == null) return true;
            if (array == null || data.array == null) return false;
            if (array.length != data.array.length) return false;

            for (int i = 0; i < array.length; i++) {
                if (!array[i].equals(data.array[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(array);
        }

        @Override
        public String toString() {
            return ObjectStringifier.toString(this, true);
        }
    }
}
