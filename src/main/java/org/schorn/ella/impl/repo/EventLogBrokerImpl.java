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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.repo.ActiveRepo;
import org.schorn.ella.repo.RepoData;
import org.schorn.ella.repo.RepoData.EventLogBroker;
import org.schorn.ella.repo.RepoProvider;

/**
 *
 * @author schorn
 *
 */
public class EventLogBrokerImpl extends AbstractContextual implements EventLogBroker {

    static private final Map<AppContext, EventLogBroker> CONTEXT_BROKER = new HashMap<>();

    static public EventLogBroker getContextEventLogBroker(AppContext context) {
        return CONTEXT_BROKER.get(context);
    }

    private final RepoData.EventLog.InternalSubscription subscription;
    private final Thread thread;
    private final List<Agent> agents;

    public EventLogBrokerImpl(AppContext context) {
        super(context);
        this.agents = new CopyOnWriteArrayList<>();
        RepoData.EventLog eventLog = RepoProvider.provider().getMingleton(RepoData.EventLog.class, context);
        this.subscription = eventLog.subscribeEvents();
        String name = String.format("Repo.State.%s", context.name());
        this.thread = new Thread(this, name);
        this.thread.start();
        CONTEXT_BROKER.put(context, this);
    }

    @Override
    public Thread thread() {
        return this.thread;
    }

    @Override
    public void join(Agent brokerAgent) {
        this.agents.add(brokerAgent);
    }

    @Override
    public void run() {
        while (true) {
            ObjectData objectData = this.subscription.get();
            if (objectData == ActiveRepo.EndOfQueue.get()) {
                return;
            }
            this.agents.stream().forEach(ba -> ba.accept(objectData));
        }
    }

    @Override
    public void close() throws Exception {
        // pumps a null event through to wake up the thread
        // this will close the subscription (no new events)
        this.subscription.close();
    }

}
