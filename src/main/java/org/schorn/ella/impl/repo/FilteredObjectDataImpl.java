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
import java.util.stream.Collectors;

import org.schorn.ella.repo.RepoSupport.FilteredObjectData;
import org.schorn.ella.repo.RepoSupport.ActiveFilter;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.format.SupportString;

/**
 *
 *
 * @author schorn
 *
 */
public class FilteredObjectDataImpl implements FilteredObjectData {

    static public FilteredObjectData create(AppContext context, ObjectData objectData, ActiveFilter activeFilter) {
        return new FilteredObjectDataImpl(context, objectData, activeFilter);
    }

    private final AppContext context;
    private final ObjectData objectData;
    private final ActiveFilter activeFilter;

    public FilteredObjectDataImpl(AppContext context) {
        this.context = context;
        this.objectData = null;
        this.activeFilter = null;
    }

    public FilteredObjectDataImpl(AppContext context, ObjectData objectData, ActiveFilter activeFilter) {
        this.context = context;
        this.objectData = objectData;
        this.activeFilter = activeFilter;
    }

    @Override
    public FilteredObjectData renew(Object... params) {
        AppContext context = this.context;
        ObjectData objectData = this.objectData;
        ActiveFilter activeFilter = this.activeFilter;
        for (Object param : params) {
            if (param == null) {
                continue;
            }
            if (param instanceof ObjectData) {
                objectData = (ObjectData) param;
            } else if (param instanceof ActiveFilter) {
                activeFilter = (ActiveFilter) param;
            } else if (param instanceof AppContext) {
                context = (AppContext) param;
            }
        }
        return new FilteredObjectDataImpl(context, objectData, activeFilter);
    }

    @Override
    public Role role() {
        return this.objectData.role();
    }

    /**
     *
     * @return
     */
    @Override
    public List<ActiveData> nodes() {
        return this.objectData.nodes().stream().filter(this.activeFilter).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return this.objectData.size();
    }

    /**
     *
     * @return
     */
    @Override
    public ActiveType activeType() {
        return this.objectData.activeType();
    }

    @Override
    public Object activeValue() {
        return this.objectData.activeValue();
    }

    @Override
    public List<Object> activeValues() {
        return this.objectData.activeValues();
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT) {
        return this.objectData.activeValues(classForT);
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, Predicate<T> filter) {
        return this.objectData.activeValues(classForT, filter);
    }

    @Override
    public List<Object> activeValues(int startIdx, int length) {
        return this.objectData.activeValues(startIdx, length);
    }

    @Override
    public <T extends ActiveData> List<T> activeValues(Class<T> classForT, int startIdx, int length) {
        return this.objectData.activeValues(classForT, startIdx, length);
    }

    @Override
    public AppContext context() {
        return this.objectData.context();
    }

    @Override
    public String name() {
        return this.objectData.name();
    }

    @Override
    public int bytes() {
        return this.objectData.bytes();
    }

    @Override
    public int compareTo(ActiveData other) {
        return this.compareTo(other);
    }

    @Override
    public Integer activeId() {
        return this.objectData.activeId();
    }

    @Override
    public boolean isNull() {
        return this.objectData.isNull();
    }

    @Override
    public boolean isBlank() {
        return this.objectData.isBlank();
    }

    /*
	@Override
	public SeriesKey seriesKey() {
		return this.objectData.seriesKey();
	}
     */
    @Override
    public ActiveData get(ActiveType activeType) {
        return this.objectData.get(activeType);
    }

    @Override
    public boolean isIncomplete() {
        return this.objectData.isIncomplete();
    }

    @Override
    public Builder replicate() throws Exception {
        return this.objectData.replicate();
    }

    @Override
    public String toString() {
        return SupportString.format(this);
    }

    @Override
    public boolean addSibling(ObjectData siblingData) {
        return this.objectData.addSibling(siblingData);
    }

    @Override
    public boolean addChild(ObjectData childData) {
        return this.objectData.addChild(childData);
    }

    @Override
    public ActiveData get(int index) {
        return this.objectData.get(index);
    }

    @Override
    public List<ActiveType> missingTypes() {
        return this.missingTypes();
    }

    @Override
    public boolean appendChild(ObjectData childData) {
        // TODO Auto-generated method stub
        return false;
    }

}
