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
import dtmlibs.config.properties.PropertyAliases;
import dtmlibs.config.util.ObjectStringifier;

public final class Parent {

    static {
        PropertyAliases.createAlias(Parent.class, "cbool", "aChild", "aBoolean");
    }

    @Comment({"# Test comment 1", "Test comment 2"})
    public Child aChild;

    private Parent() { }

    public Parent(Child aChild) {
        this.aChild = aChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parent)) return false;

        final Parent parent = (Parent) o;

        if (!aChild.equals(parent.aChild)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return aChild.hashCode();
    }

    @Override
    public String toString() {
        return ObjectStringifier.toString(this, true);
    }
}
