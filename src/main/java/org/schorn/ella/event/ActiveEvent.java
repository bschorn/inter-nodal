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
package org.schorn.ella.event;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.schorn.ella.Mingleton;
import org.schorn.ella.context.ActiveContext.Contextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.repo.RepoData;

/**
 *
 *
 * @author schorn
 *
 */
public interface ActiveEvent {

    AppContext context();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 
	 * 										EVENT-POINT
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    interface EventPoint extends ActiveEvent {

        default AppContext context() {
            return AppContext.Common;
        }
    }

    public enum EventFlag implements EventPoint {
        REPO_TRIGGER_CHANGE_EVENT, // part of the ServicesRepo RepoActions
        REPO_CHANGE_EVENT, // part of the EventLog (low-level)
        DATA_LOAD_EVENT,;

    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 
	 * 										EVENT
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    interface Event<T, F> extends ActiveEvent {

        LocalDateTime timestamp();
    }

    interface DataEvent<T extends ActiveData> extends Event<T, EventFlag> {

        EventFlag flag();

        T data();
    }

    interface ObjectDataEvent extends DataEvent<ObjectData> {

        @Override
        ObjectData data();

        static public ObjectDataEvent create(AppContext context, EventFlag flag, ObjectData data) throws Exception {
            return EventProvider.provider().createInstance(ObjectDataEvent.class, context, flag, data);

        }
    }


    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 
	 * 										INJECTOR
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     * Injector - inject a segment of code when a criteria has been met.
     *
     * EventPoint has been encountered with
     *
     *
     * @param <E>
     * @param <T>
     */
    interface Injector<E, T> extends Predicate<E>, Consumer<T>, ActiveEvent {
    }

    /**
     * DataInjector - Injectors specifically for DataEvent(s)
     *
     *
     * @param <E>
     * @param <T>
     */
    interface DataInjector<E extends DataEvent<?>, T extends ActiveData> extends Injector<E, T> {

        @Override
        boolean test(E event);

        @Override
        void accept(T data);
    }

    /**
     * ObjectDataInjector - Injectors specifically for ObjectDataEvent(s)
     *
     *
     */
    interface ObjectDataInjector extends DataInjector<ObjectDataEvent, ObjectData> {

        @Override
        boolean test(ObjectDataEvent event);

        @Override
        void accept(ObjectData data);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 
	 * 										REACTOR
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    interface Reactor<E, T> extends Predicate<E>, Consumer<T>, ActiveEvent {
    }

    interface DataReactor<E extends DataEvent<?>, T extends ActiveData> extends Reactor<E, T> {

        @Override
        boolean test(E event);

        @Override
        void accept(T data);
    }

    /**
     *
     */
    interface ObjectDataReactor extends DataInjector<ObjectDataEvent, ObjectData> {

        @Override
        boolean test(ObjectDataEvent event);

        @Override
        void accept(ObjectData data);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 
	 * 										MANAGER
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    interface Manager<E, R> extends ActiveEvent {

        /**
         *
         * @param event
         */
        void fire(E event);

        void register(R reactor);
    }

    interface DataEventManager extends Manager<DataEvent<?>, DataInjector<?, ?>> {

        void fire(DataEvent<?> event);

        void fire(AppContext context, EventFlag flag, ActiveData data);

        /**
         *
         * @param reactor
         */
        void register(DataInjector<?, ?> reactor);

        void unregister(DataInjector<?, ?> reactor);

        static public DataEventManager get(AppContext context) {
            return EventProvider.provider().dataEventManager(context);
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 
	 * 							   REPO EVENT LISTENER
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    interface EventLogListener extends Contextual, Mingleton, RepoData.EventLogBroker.Agent {

        static EventLogListener get(AppContext context) {
            return EventProvider.provider().getMingleton(EventLogListener.class, context);
        }

    }

}
