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
/**
 * This packages contains classes related to saving and loading data into various storage mediums.
 * <br>
 * For human readable data files, Hocon is recommended as it is the most straight forward for the end user. Hocon also
 * supports comments through the {@link dtmlibs.config.annotation.Comment} annotation. To create a Hocon data source
 * see {@link dtmlibs.config.datasource.hocon.HoconDataSource#builder()}.
 * <br>
 * <strong>Note:</strong> You must include ninja.leaping.configurate:configurate-core:3.0 or compatible libraries on the
 * classpath to use this package. Each data source implementation has additional library requirements. Refer to each
 * package for specific details.
 */
package dtmlibs.config.datasource;