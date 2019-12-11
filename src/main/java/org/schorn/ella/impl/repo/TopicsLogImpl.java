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
import org.schorn.ella.repo.RepoProvider;
import org.schorn.ella.repo.RepoData.EventLogBroker;
import org.schorn.ella.repo.RepoData.TopicsLog;

/**
 *
 * @author schorn
 *
 */
class TopicsLogImpl extends AbstractContextual implements TopicsLog {

    private final Map<ObjectType, List<ObjectData>> data;

    TopicsLogImpl(AppContext context) {
        super(context);
        this.data = new HashMap<>();
        EventLogBroker broker = RepoProvider.provider().getMingleton(EventLogBroker.class, context);
        broker.join(this);
    }

    @Override
    public void accept(ObjectData objectData) {
        if (objectData == null) {
            return;
        }
        List<ObjectData> topicLog = this.data.get(objectData.objectType());
        if (topicLog == null) {
            synchronized (this) {
                topicLog = this.data.get(objectData.objectType());
                if (topicLog == null) {
                    topicLog = new ArrayList<>(10000);
                    this.data.put(objectData.objectType(), topicLog);
                }
            }
        }
        topicLog.add(objectData);
    }

    @Override
    public Stream<ObjectData> read(ObjectType objectType, Predicate<ActiveData> filter) {
        List<ObjectData> topicLog = this.data.get(objectType);
        if (topicLog == null) {
            return new ArrayList<ObjectData>(0).stream();
        }
        return topicLog.stream().filter(filter);
    }

}
