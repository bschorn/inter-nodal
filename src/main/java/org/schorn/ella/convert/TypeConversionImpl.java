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
package org.schorn.ella.convert;

import java.util.function.Function;

import org.schorn.ella.node.ActiveNode.TypeConversion;

/**
 *
 * @author schorn
 *
 * @param <T>
 * @param <R>
 */
public class TypeConversionImpl<T, R> implements TypeConversion<T, R> {

    private String description;
    private Class<?> sourceClass;
    private Class<?> targetClass;
    private Function<T, R> function;

    TypeConversionImpl(Class<T> sourceClass, Class<R> targetClass,
            Function<T, R> function, String description) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.function = function;
        this.description = description;
    }

    @Override
    public String description() {
        return description;
    }

    @SuppressWarnings({})
    @Override
    public Function<T, R> function() {
        return function;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Class sourceClass() {
        return sourceClass;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Class targetClass() {
        return targetClass;
    }

    /**
     *
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public R apply(Object value) {
        return (R) this.function.apply((T) value);
    }

}
