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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Removes redundant memory storage for String objects.
 *
 * This replaces each unique String value with a unique Integer value.
 *
 * This is remove any redundancy of storing the same underlying array of
 * characters in multiple String objects.
 *
 * The StringCached instance will only contain an (int) which can be redeemed
 * for a String by calling StringCached.toString().
 *
 * The construction of the StringCached object incurs the cost when it searches
 * a HashMap for any previous entries that match.
 *
 * The redemption of the String is by index into an array so there is minimal
 * cost when calling toString().
 *
 * @author schorn
 *
 */
public class StringCached implements Comparable<StringCached> {

    static private final AtomicInteger ID = new AtomicInteger(0);
    static private final Map<String, Integer> MAP = new ConcurrentHashMap<>();
    static private final int INCR_SIZE = 1000;
    static String[] LIST = new String[INCR_SIZE];

    static public void initialize() {
        System.out.println(StringCached.class.getSimpleName());
    }

    private int _id;

    /**
     * Look up String when given an Id
     *
     * @param id
     * @return
     */
    static public String fromId(Integer id) {
        return LIST[id];
    }

    public StringCached(String value) {
        if (value == null) {
            value = "null";
        }
        Integer id = MAP.get(value);
        if (id == null) {
            synchronized (MAP) {
                id = MAP.get(value);
                if (id == null) {
                    id = ID.getAndIncrement();
                    MAP.put(value, id);
                    if (LIST.length <= id.intValue()) {
                        String[] list = new String[LIST.length + INCR_SIZE];
                        for (int i = 0; i < LIST.length; i = i + 1) {
                            list[i] = LIST[i];
                        }
                        synchronized (LIST) {
                            LIST = list;
                        }
                    }
                    LIST[id.intValue()] = value;
                }
            }
        }
        this._id = id.intValue();
    }

    /**
     * Returns the String object associated with this StringCached instance.
     *
     */
    @Override
    public String toString() {
        if (this._id < LIST.length) {
            return LIST[this._id];
        }
        return null;
    }

    /**
     * Returns the Integer that uniquely identifies the string behind this
     * instance.
     *
     * @return
     */
    public Integer toInteger() {
        return this._id;
    }

    /**
     * Compares the actual Strings
     *
     */
    @Override
    public int compareTo(StringCached that) {
        return LIST[this._id].compareTo(LIST[that._id]);
    }

}
