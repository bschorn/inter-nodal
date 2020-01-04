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
import org.schorn.ella.node.ActiveNode.ActiveData;

/**
 *
 * @author schorn
 *
 */
public abstract class ActiveDataImpl implements ActiveData {

    static private int DATA_SIZE = 1000000;
    static private AtomicInteger NEXT_ACTIVE_DATA_ID = new AtomicInteger(0);
    static private ActiveData[] DATA = new ActiveData[DATA_SIZE];
    static private Object LOCK = new Object();

    public static ActiveData forActiveId(Integer activeId) {
        if (activeId < DATA_SIZE) {
            return DATA[activeId];
        }
        return null;
    }

    private final ActiveType activeType;
    private final int activeId;

    /**
     *
     * @param activeType
     */
    public ActiveDataImpl(ActiveType activeType) {
        this.activeType = activeType;
        this.activeId = NEXT_ACTIVE_DATA_ID.getAndIncrement();
        if (this.activeId >= DATA_SIZE) {
            synchronized (LOCK) {
                if (this.activeId >= DATA_SIZE) {
                    ActiveData[] temp = new ActiveData[DATA_SIZE * 2];
                    System.arraycopy(DATA, 0, temp, 0, DATA_SIZE);
                    DATA = temp;
                    DATA_SIZE *= 2;
                }
            }
        }
        DATA[this.activeId] = this;
    }

    @Override
    public AppContext context() {
        return this.activeType.context();
    }

    @Override
    public String name() {
        return this.activeType.name();
    }

    @Override
    public int bytes() {
        return this.activeType.bytes();
    }

    @Override
    public Integer activeId() {
        return this.activeId;
    }

    @Override
    public ActiveType activeType() {
        return this.activeType;
    }

    @Override
    public String toString() {
        return SupportString.format(this);
    }

    @Override
    public int compareTo(ActiveData other) {
        return this.activeId - ((ActiveDataImpl) other).activeId;
    }

}
