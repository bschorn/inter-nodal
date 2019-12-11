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

import java.util.function.Consumer;

import org.schorn.ella.Mingleton;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.context.ActiveContext.Contextual;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.repo.RepoCoordinators.Dispatcher;
import org.schorn.ella.repo.RepoCoordinators.Inspector;
import org.schorn.ella.repo.RepoCoordinators.Receiver;
import org.schorn.ella.repo.RepoCoordinators.Responder;
import org.schorn.ella.repo.RepoCoordinators.Summary;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.ActiveSubscription;
import org.schorn.ella.repo.RepoSupport.QueryData;

/**
 * Active Repo - Family of Interfaces
 *
 * @author schorn
 *
 */
public interface ActiveRepo {

    static public ActiveRepo get(AppContext context) {
        return RepoProvider.provider().getMingleton(ServicesRepo.class, context);
    }

    void submit(ObjectData objectData);

    QueryData query(ActiveQuery activeQuery);

    QueryData queryEvents(ActiveQuery activeQuery);

    QueryData subscribe(ActiveSubscription activeSubscription);

    ArrayData summary();

    /**
     *
     * Services Repo: Interface for the Services Layer
     *
     */
    public interface ServicesRepo extends Contextual, ActiveRepo, Mingleton {

        static public ServicesRepo get(AppContext context) {
            return RepoProvider.provider().getMingleton(ServicesRepo.class, context);
        }

        Inspector getInspector();

        Receiver getReceiver();

        Dispatcher getDispatcher();

        Responder getResponder();

        Summary getSummary();

        /**
         * The default flow for a data submission through the services layer is
         * 1) approve, 2) store, 3) distribute
         *
         * @param objectData
         */
        @Override
        default void submit(ObjectData objectData) {
            if (this.getInspector().approve(objectData)) {
                this.getReceiver().receive(objectData);
                this.getDispatcher().distribute(objectData);
            }
        }

        /**
         * Query Execution
         *
         * @param objectQuery
         * @return
         */
        @Override
        default QueryData query(ActiveQuery activeQuery) {
            return getResponder().query(activeQuery);
        }

        /**
         * Query Execution
         *
         * @param activeQuery
         * @param objectQuery
         * @return
         */
        @Override
        default QueryData queryEvents(ActiveQuery activeQuery) {
            return getResponder().queryEvents(activeQuery);
        }

        /**
         * Subscribe (and Query)
         *
         */
        @Override
        default QueryData subscribe(ActiveSubscription activeSubscription) {
            // TODO: the ActiveSubscription needs to be in place 
            // before the query is executed, but we don't have 
            // an implementation currently.
            return getResponder().query(activeSubscription.getQuery());
        }

        /**
         * Repo Summary (current contents)
         *
         * @return
         */
        @Override
        default ArrayData summary() {
            return getSummary().summary();
        }
    }

    /**
     *
     * LoaderRepo: Interface for the In-Process Tasks (Recovery, Loaders)
     *
     */
    interface LoaderRepo extends Contextual, Consumer<StructData>, Mingleton {

        static public LoaderRepo get(AppContext context) {
            return RepoProvider.provider().getMingleton(LoaderRepo.class, context);
        }

        Receiver getReceiver();

        /**
         *
         *
         */
        default void accept(StructData structData) {
            this.getReceiver().receive((ObjectData) structData);
        }
    }

    /**
     * An empty object that is used to signal the end of the EventLog
     */
    interface EndOfQueue extends ObjectData {

        /**
         *
         * @return
         */
        static EndOfQueue get() {
            return RepoProvider.provider().getEndOfQueueMarker();
        }
    }

}
