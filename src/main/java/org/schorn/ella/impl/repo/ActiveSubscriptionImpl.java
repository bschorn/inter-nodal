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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.repo.ActiveRepo;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.ActiveSubscription;
import org.schorn.ella.util.StringCached;

/**
 *
 *
 * @author schorn
 *
 */
class ActiveSubscriptionImpl extends AbstractContextual implements ActiveSubscription {

    private static final Logger LGR = LoggerFactory.getLogger(ActiveSubscriptionImpl.class);

    private final BlockingQueue<ObjectData> blockingQueue = new LinkedBlockingDeque<>(10000);
    private final ObjectType objectType;
    private ActiveQuery activeQuery = null;
    private boolean closed = false;

    ActiveSubscriptionImpl(AppContext context, ObjectType objectType) {
        super(context);
        this.objectType = objectType;
    }

    @Override
    public ActiveSubscription renew(Object... params) {
        ObjectType objectType = this.objectType;
        for (Object param : params) {
            if (param instanceof ObjectType) {
                objectType = (ObjectType) param;
            }
        }
        return new ActiveSubscriptionImpl(this.context(), objectType);
    }

    @Override
    public ActiveQuery getQuery() {
        return this.activeQuery;
    }

    @Override
    public void accept(ObjectData objectData) {
        if (objectData.objectType().equals(this.objectType)) {
            try {
                if (!this.closed) {
                    if (this.test(objectData)) {
                        this.blockingQueue.put(objectData);
                    }
                }
            } catch (InterruptedException e) {
                LGR.error("{}.accept() - caught InterruptedException while accepting -> {} Key[{}]: {}",
                        this.getClass().getSimpleName(),
                        this.objectType.name(),
                        objectData.getSeriesKey(),
                        StringCached.fromId(objectData.getSeriesKey())
                );
            }
        }
    }

    /**
     *
     *
     */
    @Override
    public boolean test(ObjectData objectData) {
        if (this.activeQuery != null) {
            for (ObjectType fromType : this.activeQuery.from()) {
                if (fromType.equals(objectData.objectType())) {
                    return this.activeQuery.where().test(objectData);
                }
            }
        }
        return false;
    }

    @Override
    public ObjectData get() {
        if (this.closed && this.blockingQueue.isEmpty()) {
            return null;
        }
        ObjectData objectData;
        try {
            objectData = this.blockingQueue.take();
            if (objectData instanceof ActiveRepo.EndOfQueue) {
                this.closed = true;
            } else {
                return objectData;
            }
        } catch (InterruptedException e) {
            LGR.error("{}.get() - caught InterruptedException while taking {}.",
                    this.getClass().getSimpleName(),
                    this.objectType.name());
        }
        return null;
    }

    @Override
    public void unsubscribe() {
        this.closed = true;
        this.blockingQueue.offer(ActiveRepo.EndOfQueue.get());
    }

}
