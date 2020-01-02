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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.repo.RepoData.CurrentState;
import org.schorn.ella.repo.RepoData.EventLogBroker;
import org.schorn.ella.repo.RepoProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public class CurrentStateImpl extends AbstractContextual implements CurrentState {

    @SuppressWarnings("unused")
    private static final Logger LGR = LoggerFactory.getLogger(CurrentStateImpl.class);

    private final Map<ObjectType, Map<Integer, ObjectData>> data;

    /**
     *
     * @param context
     */
    CurrentStateImpl(AppContext context) {
        super(context);
        this.data = new HashMap<>();
        EventLogBroker broker = RepoProvider.provider().getMingleton(EventLogBroker.class, context);
        broker.join(this);
    }

    @Override
    public void accept(ObjectData objectData) {
        if (objectData != null) {
            Map<Integer, ObjectData> typeData = this.data.get(objectData.objectType());
            if (typeData != null) {
                typeData.put(objectData.getSeriesKey(), objectData);
            } else {
                typeData = new HashMap<Integer, ObjectData>();
                typeData.put(objectData.getSeriesKey(), objectData);
                this.data.put(objectData.objectType(), typeData);
            }
        }
    }

    @Override
    public Stream<ObjectData> read(ObjectType objectType, Predicate<ActiveData> filter) {
        Map<Integer, ObjectData> typeData = this.data.get(objectType);
        if (typeData != null) {
            if (filter == null) {
                return typeData.values().stream();
            } else {
                List<ObjectData> filtered = new ArrayList<>();
                for (ObjectData objectData : typeData.values()) {
                    if (filter.test(objectData)) {
                        filtered.add(objectData);
                    }
                }
                return filtered.stream();
                //return typeData.values().stream().filter(filter);
            }
        }
        return (new ArrayList<ObjectData>(0)).stream();
    }

    @Override
    public ObjectData get(ObjectType objectType, Integer seriesKey) {
        Map<Integer, ObjectData> typeData = this.data.get(objectType);
        if (typeData != null) {
            return typeData.get(seriesKey);
        }
        return null;
    }

    @Override
    public int count(ObjectType objectType) {
        Map<Integer, ObjectData> typeData = this.data.get(objectType);
        if (typeData != null) {
            return typeData.size();
        }
        return 0;
    }

    @Override
    public Integer getCurrentVersion(ObjectType objectType, Integer seriesKey) {
        ObjectData latestData = this.get(objectType, seriesKey);
        if (latestData != null) {
            return latestData.getVersion();
        }
        return null;
    }

}
