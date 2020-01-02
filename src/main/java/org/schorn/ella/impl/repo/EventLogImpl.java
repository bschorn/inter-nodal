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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.repo.RepoData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class EventLogImpl extends AbstractContextual implements RepoData.EventLog {

    @SuppressWarnings("unused")
    private static final Logger LGR = LoggerFactory.getLogger(EventLogImpl.class);

    private static final Integer INITIAL_SIZE = 50000;

    /**
     * Members
     */
    private final List<ObjectData> eventSource;
    private final List<InternalSubscriptionImpl> subscriptions;

    EventLogImpl(AppContext context) {
        super(context);
        this.eventSource = new ArrayList<>(INITIAL_SIZE);
        this.subscriptions = new CopyOnWriteArrayList<>();
    }

    @Override
    public boolean append(ObjectData objectData) {
        if (objectData == null) {
            return false;
        }
        this.eventSource.add(objectData);
        this.subscriptions.stream().forEach(s -> s.accept(objectData));
        //LGR.debug("{}.append() - {}", this.getClass().getSimpleName(), this.transform.apply(objectData));
        return true;
    }

    @Override
    public Stream<ObjectData> readEvents() {
        return this.eventSource.stream();
    }

    @Override
    public Stream<ObjectData> readEvents(Predicate<ObjectData> filter) {
        return this.eventSource.stream().filter(filter);
    }

    /**
     * Ask the EventLog to new create new Subscription object with 'this'
     * EventLog being its publisher.
     */
    @Override
    public InternalSubscription subscribeEvents() {
        InternalSubscriptionImpl subscription = new InternalSubscriptionImpl(this);
        this.subscriptions.add(subscription);
        return subscription;
    }

    @Override
    public InternalSubscription subscribeEvents(Predicate<ObjectData> filter) {
        InternalSubscriptionImpl subscription = new InternalSubscriptionImpl(this, filter);
        this.subscriptions.add(subscription);
        return subscription;
    }

}
