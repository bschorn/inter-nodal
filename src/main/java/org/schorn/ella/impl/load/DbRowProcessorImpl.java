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

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.schorn.ella.load.ActiveTabularLoad.ActiveTypeValue;
import org.schorn.ella.load.ActiveTabularLoad.DbRowProcessor;
import org.schorn.ella.node.ActiveNode.ActiveType;

/**
 *
 * @author schorn
 *
 */
public class DbRowProcessorImpl implements DbRowProcessor {

    protected final List<ActiveType> types;
    protected final Function<ResultSet, List<ActiveTypeValue>> parser;
    protected final Predicate<ActiveTypeValue> validate;
    protected final Consumer<List<ActiveTypeValue>> consumer;

    /**
     *
     * @param types
     * @param parser
     * @param validate
     * @param consumer
     */
    public DbRowProcessorImpl(List<ActiveType> types,
            Function<ResultSet, List<ActiveTypeValue>> parser,
            Predicate<ActiveTypeValue> validate,
            Consumer<List<ActiveTypeValue>> consumer) {
        this.types = types;
        this.parser = parser;
        this.validate = validate;
        this.consumer = consumer;
    }

    @Override
    public List<ActiveType> types() {
        return this.types;
    }

    @Override
    public Function<ResultSet, List<ActiveTypeValue>> parser() {
        return this.parser;
    }

    @Override
    public Predicate<ActiveTypeValue> validate() {
        return this.validate;
    }

    @Override
    public Consumer<List<ActiveTypeValue>> consumer() {
        return this.consumer;
    }

}
