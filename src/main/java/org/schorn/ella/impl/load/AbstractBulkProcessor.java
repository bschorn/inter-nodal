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
package org.schorn.ella.impl.load;

import java.io.Reader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import org.schorn.ella.load.ActiveObjectLoad.BulkProcessor;

/**
 *
 * @author schorn
 *
 * @param <R>
 */
public abstract class AbstractBulkProcessor<T, R> implements BulkProcessor<T, R> {

    private final Reader reader;
    private final Function<T, R> function;
    private final Predicate<R> predicate;
    private final Consumer<R> consumer;
    private Consumer<R> monitor;

    AbstractBulkProcessor(Reader reader, Function<T, R> function, Predicate<R> predicate, Consumer<R> consumer) {
        this.reader = reader;
        this.function = function;
        this.predicate = predicate;
        this.consumer = consumer;
    }

    @Override
    public Reader reader() {
        return this.reader;
    }

    @Override
    public Function<T, R> function() {
        return this.function;
    }

    @Override
    public Predicate<R> predicate() {
        return this.predicate;
    }

    @Override
    public Consumer<R> consumer() {
        return this.consumer;
    }

    @Override
    public Consumer<R> monitor() {
        return this.monitor;
    }

    @Override
    public void setMonitoring(Consumer<R> monitoring) {
        this.monitor = monitoring;
    }

    @Override
    public boolean monitoring() {
        return this.monitor != null;
    }
}
