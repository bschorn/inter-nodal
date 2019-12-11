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
package org.schorn.ella.impl.node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.schorn.ella.context.AppContext;

/**
 *
 * @author schorn
 *
 */
public class TypeLibrary {

    private AppContext context;

    Map<Class<?>, Map<String, Object>> typeMap = new HashMap<>();

    TypeLibrary(AppContext context) {
        this.context = context;
    }

    AppContext context() {
        return this.context;
    }

    <T> void set(Class<T> classOfT, String name, Object typeObj) {
        Map<String, Object> map = this.typeMap.get(classOfT);
        if (map == null) {
            map = new HashMap<>();
            this.typeMap.put(classOfT, map);
        }
        map.put(name, typeObj);
    }

    @SuppressWarnings("unchecked")
    <T> T get(Class<T> classOfT, String name) {
        Map<String, Object> map = this.typeMap.get(classOfT);
        if (map == null) {
            return null;
        }
        return (T) map.get(name);
    }

    @SuppressWarnings("unchecked")
    <T> List<T> get(Class<T> classOfT) {
        Map<String, Object> map = this.typeMap.get(classOfT);
        if (map == null) {
            return null;
        }
        return map.values().stream()
                .map(o -> ((T) o))
                .collect(Collectors.toList());
    }
}
