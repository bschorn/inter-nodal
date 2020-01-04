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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import org.schorn.ella.load.ActiveTabularLoad.LoadFromQuery.Result;
import org.schorn.ella.node.ActiveNode.ObjectType;

public class LoadFromQueryResultImpl implements Result {

    private final ObjectType objectType;
    private final LocalDateTime startTime;
    private final String sql;

    private int rows;
    private LocalDateTime endTime;
    private Exception exception = null;
    private boolean killed = false;

    public LoadFromQueryResultImpl(ObjectType objectType, String sql) {
        this.objectType = objectType;
        this.sql = sql;
        this.startTime = LocalDateTime.now();
    }

    @Override
    public Result result(int rows) {
        this.rows = rows;
        this.endTime = LocalDateTime.now();
        return this;
    }

    @Override
    public Result exception(int rows, Exception exception) {
        this.rows = rows;
        this.exception = exception;
        this.endTime = LocalDateTime.now();
        return this;
    }

    @Override
    public Result killed(int rows) {
        this.rows = rows;
        this.killed = true;
        this.endTime = LocalDateTime.now();
        return this;
    }

    @Override
    public String sql() {
        return this.sql;
    }

    @Override
    public int rows() {
        return this.rows;
    }

    @Override
    public ObjectType objectType() {
        return this.objectType;
    }

    @Override
    public LocalDateTime startTime() {
        return this.startTime;
    }

    @Override
    public LocalDateTime endTime() {
        return this.endTime;
    }

    @Override
    public long elapsedTime(TimeUnit timeUnit) {
        return timeUnit.convert(Duration.between(this.startTime, this.endTime).toNanos(), TimeUnit.NANOSECONDS);
    }

    @Override
    public boolean wasKilled() {
        return this.killed;
    }

    @Override
    public void throwException() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
    }

    @Override
    public String toString() {
        return this.displayResult();
    }
}
