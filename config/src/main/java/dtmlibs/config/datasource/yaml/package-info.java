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
 * This packages provides a YAML implementation of {@link dtmlibs.config.datasource.DataSource}.
 * <br>
 * <strong>Warning:</strong> This implementation tries to support comments through {@link dtmlibs.config.annotation.Comment}
 * though it may not always work and throw exceptions depending on the complexity of the yaml. For commented configuration
 * files, the Hocon package is much more reliable and performant.
 * <br>
 * <strong>Note:</strong> You must include org.yaml:snakeyaml:1.16 and ninja.leaping.configurate:configurate-yaml:3.0
 * or compatible libraries on the classpath to use this package.
 */
package dtmlibs.config.datasource.yaml;