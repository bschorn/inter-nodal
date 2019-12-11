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
package org.schorn.ella.repo;

import java.util.function.Supplier;

import org.schorn.ella.Provider;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.event.ActiveEvent;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.repo.ActiveRepo.EndOfQueue;
import org.schorn.ella.repo.RepoData.EventLogBroker;
import org.schorn.ella.repo.RepoSupport.ConditionStatementParser;

/**
 * ActiveNode Provider Interface
 *
 * @author schorn
 *
 */
public interface RepoProvider extends Provider {

    /**
     * Implementation for this interface is retrieved
     */
    static RepoProvider provider() {
        return Provider.Providers.REPO.getInstance(RepoProvider.class);
    }

    Supplier<?> getActivitySupplier(AppContext context);

    ActiveEvent.EventFlag getRepoUpdateEventFlag();

    /**
     * Each Context has one EventLogBroker (running in its own thread). An
     * EventLogBroker has a subscription to the EventLog for its context.
     *
     * @param context
     * @return
     */
    EventLogBroker getContextEventLogBroker(AppContext context);

    /**
     *
     *
     * @return
     */
    EndOfQueue getEndOfQueueMarker();

    /**
     *
     * @param context
     * @param operators
     * @return
     * @throws Exception
     */
    ConditionStatementParser createConditionStatementParser(AppContext context, ActiveNode.ActiveOperator[] operators) throws Exception;

}
