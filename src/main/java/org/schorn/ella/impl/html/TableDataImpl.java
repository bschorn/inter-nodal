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
package org.schorn.ella.impl.html;

import java.util.List;
import java.util.function.Predicate;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.html.ActiveHtml.TableData;

/**
 *
 * @author schorn
 *
 */
public class TableDataImpl implements TableData {

    private final ArrayData resultData;
    private final String caption;

    TableDataImpl() {
        this.resultData = null;
        this.caption = null;
    }

    private TableDataImpl(ArrayData resultData, String caption) {
        this.resultData = resultData;
        this.caption = caption;
    }

    @Override
    public TableData renew(Object... params) {
        ArrayData resultData = this.resultData;
        String caption = this.caption;
        for (Object param : params) {
            if (param instanceof ArrayData) {
                resultData = (ArrayData) param;
            } else if (param instanceof String) {
                caption = (String) param;
            }
        }
        return new TableDataImpl(resultData, caption);
    }

    @Override
    public String getCaption() {
        return this.caption;
    }

    @Override
    public ActiveType memberType() {
        return this.resultData.memberType();
    }

    @Override
    public boolean add(ActiveData activeData) {
        return false;
    }

    @Override
    public List<ActiveData> nodes() {
        return this.resultData.nodes();
    }

    @Override
    public int size() {
        return this.resultData.size();
    }

    @Override
    public ActiveType activeType() {
        return this.resultData.activeType();
    }

    @Override
    public Object activeValue() {
        return this.resultData.activeValue();
    }

    @Override
    public List<Object> activeValues() {
        return this.resultData.activeValues();
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT) {
        return this.resultData.activeValues(classForT);
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, Predicate<T> filter) {
        return this.resultData.activeValues(classForT, filter);
    }

    @Override
    public List<Object> activeValues(int startIdx, int length) {
        return this.resultData.activeValues(startIdx, length);
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, int startIdx, int length) {
        return this.resultData.activeValues(classForT, startIdx, length);
    }

    @Override
    public String name() {
        return this.resultData.name();
    }

    @Override
    public AppContext context() {
        return this.resultData.context();
    }

    @Override
    public int bytes() {
        return this.resultData.bytes();
    }

    @Override
    public int compareTo(ActiveData other) {
        return this.resultData.compareTo(other);
    }

    @Override
    public Integer activeId() {
        return this.resultData.activeId();
    }

    @Override
    public boolean isNull() {
        return this.resultData.isNull();
    }

    @Override
    public boolean isBlank() {
        return this.resultData.isBlank();
    }

    @Override
    public ActiveData get(int index) {
        return this.resultData.get(index);
    }

}
