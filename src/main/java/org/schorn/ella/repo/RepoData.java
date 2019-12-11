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
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.schorn.ella.Mingleton;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.context.ActiveContext.Contextual;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.repo.RepoCoordinators.Summary;

/**
 * The key to Event Sourcing is that we guarantee that all changes to the domain
 * objects are initiated by the event objects. This leads to a number of
 * facilities that can be built on top of the event log:
 * <p>
 * <ul>
 * <li>
 * Complete Rebuild: We can discard the application state completely and rebuild
 * it by re-running the events from the event log on an empty application.
 * </li>
 * </li>
 * Temporal Query: We can determine the application state at any point in time.
 * Notionally we do this by starting with a blank state and rerunning the events
 * up to a particular time or event. We can take this further by considering
 * multiple time-lines (analogous to branching in a version control system).
 * </li>
 * </li>
 * Event Replay: If we find a past event was incorrect, we can compute the
 * consequences by reversing it and later events and then replaying the new
 * event and later events. (Or indeed by throwing away the application state and
 * replaying all events with the correct event in sequence.) The same technique
 * can handle events received in the wrong sequence - a common problem with
 * systems that communicate with asynchronous messaging.
 * </li>
 * </ul>
 *
 *
 *
 * @author schorn
 *
 */
public interface RepoData {

    static ObjectData getEndOfQueueMarker() {
        return RepoProvider.provider().getEndOfQueueMarker();
    }

    /**
     *
     * Event Log
     *
     */
    interface EventLog extends Contextual, Mingleton {

        /**
         * Internal Subscription Template
         */
        interface InternalSubscription extends Supplier<ObjectData>, AutoCloseable {
            // Subscription
        }

        /**
         * Append event to EventLog
         *
         */
        boolean append(ObjectData objectData);

        /**
         * Stream of entire store.
         *
         */
        Stream<ObjectData> readEvents();

        /**
         * Filtered stream of entire store
         *
         */
        Stream<ObjectData> readEvents(Predicate<ObjectData> filter);

        /**
         *
         * @return 
         */
        InternalSubscription subscribeEvents();

        /**
         *
         */
        InternalSubscription subscribeEvents(Predicate<ObjectData> filter);
    }

    /**
     *
     * Event Log Agent
     *
     */
    interface EventLogBroker extends Contextual, Mingleton, Runnable, AutoCloseable {

        static EventLogBroker get(AppContext context) {
            return RepoProvider.provider().getMingleton(EventLogBroker.class, context);
        }

        /**
         *
         */
        interface Agent extends Consumer<ObjectData> {

            void accept(ObjectData objectData);

            Stream<ObjectData> read(ObjectType objectType, Predicate<ActiveData> filter);
        }

        /**
         *
         * @param agent
         */
        void join(Agent agent);

        /**
         * Accessor to the processing thread of ContextState
         *
         * @return
         */
        Thread thread();
    }

    /**
     *
     * Topics Log (Events by Topic)
     *
     */
    interface TopicsLog extends Contextual, Mingleton, EventLogBroker.Agent {

        static TopicsLog get(AppContext context) {
            return RepoProvider.provider().getMingleton(TopicsLog.class, context);
        }

    }

    /**
     *
     * Current State of every series in every type.
     *
     * A series is represented by the unique key within a type. Each instance
     * within a series has a version number that increments (+1) for each new
     * instance within the series.
     *
     */
    interface CurrentState extends Contextual, Mingleton, EventLogBroker.Agent {

        static CurrentState get(AppContext context) {
            return RepoProvider.provider().getMingleton(CurrentState.class, context);
        }

        /**
         *
         * @param objectType
         * @param seriesKey
         * @return
         */
        ObjectData get(ObjectType objectType, Integer seriesKey);

        /**
         * Returns the current count of series within a type
         *
         * @param objectType
         * @return
         */
        int count(ObjectType objectType);

        /**
         * Get the version of the current instance of the series.
         *
         * @param objectType
         * @param seriesKey
         * @return
         */
        Integer getCurrentVersion(ObjectType objectType, Integer seriesKey);
    }

    /**
     *
     * Context State (Current Event for each Series)
     *
     */
    interface RepoStat extends Contextual, Mingleton, EventLogBroker.Agent, Summary {

    }

}
