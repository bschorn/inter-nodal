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
package org.schorn.ella.impl.repo;

import java.util.List;
import java.util.function.Predicate;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.QueryData;

/**
 *
 *
 * @author schorn
 *
 */
public class QueryDataImpl extends AbstractContextual implements QueryData {

    private final ArrayData arrayData;
    private final ActiveQuery activeQuery;
    private final String title;
    private Exception exception;
    private String clientId;

    QueryDataImpl(AppContext context) {
        super(context);
        this.arrayData = null;
        this.activeQuery = null;
        this.title = null;
        this.exception = null;
    }

    private QueryDataImpl(ArrayData resultData, ActiveQuery activeQuery, String title) {
        super(resultData.context());
        this.arrayData = resultData;
        this.activeQuery = activeQuery;
        this.title = title;
        this.exception = null;
    }

    @Override
    public QueryData renew(Object... params) {
        ArrayData arrayData = this.arrayData;
        ActiveQuery activeQuery = this.activeQuery;
        String title = this.title;
        Exception exception = null;
        for (Object param : params) {
            if (param instanceof ArrayData) {
                arrayData = (ArrayData) param;
            } else if (param instanceof ActiveQuery) {
                activeQuery = (ActiveQuery) param;
            } else if (param instanceof String) {
                title = (String) param;
            } else if (param instanceof Exception) {
                exception = (Exception) param;
            }
        }
        QueryDataImpl queryData = new QueryDataImpl(arrayData, activeQuery, title);
        queryData.exception = exception;
        return queryData;
    }

    @Override
    public ActiveQuery activeQuery() {
        return this.activeQuery;
    }

    @Override
    public String title() {
        if (this.title == null) {
            return this.activeQuery.toString().replace("\n", " ").replaceAll("\\s+", " ");
        }
        return this.title;
    }

    @Override
    public ArrayData arrayData() {
        return this.arrayData;
    }

    @Override
    public ActiveType memberType() {
        return this.arrayData.memberType();
    }

    @Override
    public boolean add(ActiveData activeData) {
        return false;
    }

    @Override
    public List<ActiveData> nodes() {
        return this.arrayData.nodes();
    }

    @Override
    public int size() {
        return this.arrayData.size();
    }

    @Override
    public ActiveType activeType() {
        return this.arrayData.activeType();
    }

    @Override
    public Object activeValue() {
        return this.arrayData.activeValue();
    }

    @Override
    public List<Object> activeValues() {
        return this.arrayData.activeValues();
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT) {
        return this.arrayData.activeValues(classForT);
    }

    @Override
    public List<Object> activeValues(int startIdx, int length) {
        return this.arrayData.activeValues(startIdx, length);
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, int startIdx, int length) {
        return this.arrayData.activeValues(classForT, startIdx, length);
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, Predicate<T> filter) {
        return this.arrayData.activeValues(classForT, filter);
    }

    @Override
    public String name() {
        return this.arrayData.name();
    }

    @Override
    public AppContext context() {
        return this.arrayData.context();
    }

    @Override
    public int bytes() {
        return this.arrayData.bytes();
    }

    @Override
    public int compareTo(ActiveData other) {
        return this.arrayData.compareTo(other);
    }

    @Override
    public Integer activeId() {
        return this.arrayData.activeId();
    }

    @Override
    public boolean isNull() {
        return this.arrayData.isNull();
    }

    @Override
    public boolean isBlank() {
        return this.arrayData.isBlank();
    }

    @Override
    public ActiveData get(int index) {
        return this.arrayData.get(index);
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public String toString() {
        return String.format("[ActiveQuery] %s\n[ClientId] %s\n[Title] %s\n[Exception] %s\n[ArrayData] %d bytes",
                this.activeQuery.toString(),
                this.clientId != null ? this.clientId : "null",
                this.title != null ? this.title : "null",
                this.exception != null ? this.exception.getMessage() : "null",
                this.arrayData != null ? this.arrayData.bytes() : 0);
    }
}
