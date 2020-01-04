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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.repo.ActiveRepo;
import org.schorn.ella.repo.RepoData.EventLog.InternalSubscription;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class InternalSubscriptionImpl implements InternalSubscription, Consumer<ObjectData> {

    private static final Logger LGR = LoggerFactory.getLogger(InternalSubscriptionImpl.class);

    private final BlockingQueue<ObjectData> queue;
    private final Predicate<ObjectData> filter;

    public InternalSubscriptionImpl(EventLogImpl supplier) {
        this.filter = null;
        this.queue = new LinkedBlockingQueue<>();
    }

    public InternalSubscriptionImpl(EventLogImpl supplier, Predicate<ObjectData> filter) {
        this.filter = filter;
        this.queue = new LinkedBlockingQueue<>();
    }

    /*
	 * Inbound (EventLog)
	 * 
     */
    @Override
    public void accept(ObjectData objectData) {
        if (objectData == null) {
            return;
        }
        if (this.filter != null && !this.filter.test(objectData)) {
            return;
        }
        this.queue.add(objectData);
    }

    /*
	 * Outbound (EventLogBroker)
	 * 
     */
    @Override
    public ObjectData get() {
        while (true) {
            try {
                return this.queue.take();
            } catch (InterruptedException e) {
                LGR.error("{}.get() - Exception: {}",
                        this.getClass().getSimpleName(),
                        Functions.getStackTraceAsString(e));
            }
        }
    }

    @Override
    public void close() throws Exception {
        this.queue.add(ActiveRepo.EndOfQueue.get());
    }

}
