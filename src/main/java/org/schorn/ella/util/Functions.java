/* 
 * The MIT License
 *
 * Copyright 2019 Bryan Schorn.
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
package org.schorn.ella.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bschorn
 */
public interface Functions {

    /**
     * String split without regular expressions.
     */
    static String[] split(String delimiter, String input) {
        char[] delim = delimiter.toCharArray();
        char[] cin = input.toCharArray();
        char[] buf = null;
        List<String> output = new ArrayList<>();
        int pos = 0;
        int len = 0;
        for (int i = 0, dpos = 0; i < cin.length; ++i) {
            if (cin[i] == delim[dpos++]) {
                // we are inside the delimiter - are we done with it?
                if (dpos == delim.length) {
                    len += (1 - dpos);
                    // create a destination
                    buf = new char[len];
                    // copy part of cin array to buf array 
                    System.arraycopy(cin, pos, buf, 0, len);
                    // convert buf to string and add to our collection
                    output.add(new String(buf));
                    // reset new start position
                    pos += len + delim.length;
                    // reset character counter
                    len = 0;
                    // reset delimiter index
                    dpos = 0;
                } else {
                    len += 1;
                }
            } else {
                // reset delimiter index
                dpos = 0;
                // no delimiter keep accumulating characters
                len += 1;
            }
        }
        buf = new char[len];
        System.arraycopy(cin, pos, buf, 0, len);
        output.add(new String(buf));
        return output.toArray(new String[0]);
    }

    /**
     * String trim() each element in array of String
     *
     * @param inputs
     * @return
     */
    static String[] trim(String[] inputs) {
        for (int i = 0; i < inputs.length; ++i) {
            inputs[i] = inputs[i].trim();
        }
        return inputs;
    }

    /**
     * Split-n-Trim
     *
     * @param delimiter
     * @param input
     * @return
     */
    static String[] split_trim(String delimiter, String input) {
        return trim(split(delimiter, input));
    }

    static String getStackTraceAsString(Throwable throwable) {
        if (throwable.getMessage() != null) {
            return throwable.getMessage();
        }
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    static String stackTraceToString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        if (throwable.getMessage() != null) {
            return String.format("%s -> %s", throwable.getMessage(), stringWriter.toString());
        } else {
            return stringWriter.toString();
        }
    }

    /**
     * Utility Function
     *
     * Returns pre-sized ArrayList with the initValue
     *
     * @param classOfT - element type
     * @param size - initial capacity & initialized to defaultT
     * @param initValue - initialization value for each element from 0..size
     * @return
     */
    static <T> ArrayList<T> createPresizedArrayList(Class<T> classOfT, int size, T initValue) {
        List<T> list = new ArrayList<>(size);
        while (--size >= 0) {
            list.add(initValue);
        }
        return (ArrayList<T>) list;
    }

}
