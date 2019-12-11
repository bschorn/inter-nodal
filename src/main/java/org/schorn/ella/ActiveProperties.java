/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.schorn.ella;

import java.util.Properties;

/**
 *
 * @author bschorn
 */
public interface ActiveProperties {

    public Properties properties();

    default String getProperty(String key) {
        return this.properties().getProperty(key);
    }
    default String getProperty(String key, String defaultValue) {
        return this.properties().getProperty(key, defaultValue);
    }
    default Object setProperty(String key, String value) {
        return this.properties().setProperty(key, value);
    }

    /**
     * If an exception has been caught instead of thrown, this method will throw
     * the last caught exception or do nothing if there have been no caught
     * exception. This provides for the control of when an exception is thrown
     * by the caller.
     *
     * @throws Exception
     */
    public void checkForException() throws Exception;

}
