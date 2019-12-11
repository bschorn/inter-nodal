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
package org.schorn.ella.http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.FunctionalException;
import org.schorn.ella.Singleton;
import org.schorn.ella.io.EndPoint.URIPoint;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.parser.ActiveParser;

/**
 *
 * @author schorn
 *
 */
public interface ActiveHTTP {

    interface ContextServer extends Singleton {

        static final Logger LGR = LoggerFactory.getLogger(ContextServer.class);

        /**
         * Query using generic object
         *
         * @param openNode
         * @throws Exception
         */
        ActiveMsg<OpenNode> query(String context, String aggregate_type, OpenNode openNode);

        /**
         * Update using generic object
         *
         * @param openNode
         * @throws Exception
         */
        ActiveMsg<OpenNode> update(String context, String aggregate_type, OpenNode openNode);

        /**
         * Query using ActiveNode object
         *
         * @param openNode
         * @throws Exception
         */
        ActiveMsg<ActiveData> query(ObjectType aggregateType, ActiveData activeData);

        /**
         * Update using ActiveNode object
         *
         * @param openNode
         * @throws Exception
         */
        ActiveMsg<ActiveData> update(ObjectType aggregateType, ActiveData activeData);

        /**
         *
         * @param uriPoint
         * @return
         */
        static public ContextServer get(URIPoint uriPoint) throws Exception {
            return HTTPProvider.provider().getContextServer(uriPoint);
        }
    }

    /**
     * Interface for Parser the Response - the default returns response as a
     * String
     *
     * @param <T>
     */
    interface ResponseParser<E> extends Function<Object, E>, FunctionalException {

        E apply(Object response);

        /**
         *
         */
        static class StringResponse implements ResponseParser<String> {

            static StringResponse INSTANCE = new StringResponse();

            @Override
            public void throwException() throws Exception {
            }

            @Override
            public String apply(Object response) {
                return response.toString();
            }
        }

        /**
         *
         */
        static class OpenNodeResponse implements ResponseParser<OpenNode> {

            static OpenNodeResponse INSTANCE = new OpenNodeResponse();
            private final Map<Long, Exception> exceptions = new HashMap<>();

            @Override
            public void throwException() throws Exception {
                Exception exception = this.exceptions.remove(Thread.currentThread().getId());
                if (exception != null) {
                    throw exception;
                }
            }

            protected void setException(Exception exception) {
                this.exceptions.put(Thread.currentThread().getId(), exception);
            }

            @Override
            public OpenNode apply(Object response) {
                try {
                    return ActiveParser.ReadJson.get().parse(response.toString());
                } catch (Exception ex) {
                    this.setException(ex);
                }
                return null;
            }
        }

        /**
         *
         * @return
         */
        static public StringResponse StringResponse() {
            return StringResponse.INSTANCE;
        }

        /**
         *
         * @return
         */
        static public OpenNodeResponse OpenNodeResponse() {
            return OpenNodeResponse.INSTANCE;
        }

    }

    /**
     * ActiveMsg - Interface
     *
     */
    interface ActiveMsg<T> {

        /*
		 * Probably not a good idea to bury these text URLs here.
		 * 
         */
        public enum MsgAction {
            UPDATE("data/update/{context}/{object_type}"),
            QUERY("data/request/{context}/{object_type}"),
            CMD("");
            String path;

            MsgAction(String path) {
                this.path = path;
            }

            public String getPath() {
                return this.path;
            }
        }

        public enum MsgStatus {
            UNSUBMITTED, SUBMITTED, ACKED, ACCEPTED, PARTIAL, COMPLETE, REJECTED, ERROR;
        }

        /**
         * @return MsgAction
         */
        MsgAction msgAction();

        /**
         * @return MsgStatus
         */
        MsgStatus msgStatus();

        /**
         *
         * @return
         */
        <E> E getOutput(ResponseParser<E> responseParser) throws Exception;

        /**
         * If getOutput() returns null, then call throwException() to retrieve
         * any previously caught Exception.
         *
         * @throws Exception
         */
        void throwException() throws Exception;

        /**
         *
         * @param structData
         * @return
         */
        static ActiveMsg<ActiveData> createUpdate(ObjectType aggregateType, ActiveData structData) {
            return HTTPProvider.provider().createActiveMsg(MsgAction.UPDATE, aggregateType, structData);
        }

        /**
         *
         * @param structData
         * @return
         * @throws Exception
         */
        static ActiveMsg<ActiveData> createQuery(ObjectType aggregateType, ActiveData structData) {
            return HTTPProvider.provider().createActiveMsg(MsgAction.QUERY, aggregateType, structData);
        }

        /**
         *
         * @param context
         * @param openNode
         * @return
         * @throws Exception
         */
        static ActiveMsg<OpenNode> createUpdate(String context, String aggregate_type, OpenNode openNode) {
            return HTTPProvider.provider().createActiveMsg(MsgAction.UPDATE, aggregate_type, openNode, context);
        }

        /**
         *
         * @param context
         * @param openNode
         * @return
         * @throws Exception
         */
        static ActiveMsg<OpenNode> createQuery(String context, String aggregate_type, OpenNode openNode) {
            return HTTPProvider.provider().createActiveMsg(MsgAction.QUERY, aggregate_type, openNode, context);
        }
    }

    /**
     * Template Interface
     *
     */
    interface ActiveSubmit extends Consumer<ActiveMsg<?>>, Callable<ActiveMsg<?>> {
    }

}
