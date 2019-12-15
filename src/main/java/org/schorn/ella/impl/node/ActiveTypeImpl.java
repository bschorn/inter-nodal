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

import java.util.concurrent.atomic.AtomicInteger;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.format.SupportString;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.util.StringCached;

/**
 *
 * @author schorn
 *
 */
public abstract class ActiveTypeImpl implements ActiveType {

    private static final AtomicInteger NEXT_ACTIVE_TYPE_ID = new AtomicInteger(0);

    private final AppContext context;
    private final StringCached nameCached;
    private final int activeId;
    private final short activeIdx;

    protected ActiveTypeImpl(AppContext context, String name, Short activeIdx) {
        this.context = context;
        this.nameCached = new StringCached(name);
        this.activeId = NEXT_ACTIVE_TYPE_ID.getAndIncrement();
        this.activeIdx = activeIdx;
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    @Override
    public String name() {
        return nameCached.toString();
    }

    @Override
    public Integer activeId() {
        return this.activeId;
    }

    @Override
    public int activeIdx() {
        return (int) this.activeIdx;
    }

    @Override
    public String toString() {
        return SupportString.format(this);
    }

    @Override
    public int compareTo(ActiveType other) {
        return this.activeId - ((ActiveTypeImpl) other).activeId;
    }

}
