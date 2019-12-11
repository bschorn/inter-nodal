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
package org.schorn.ella.impl.http;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.http.ActiveHTTP.ActiveMsg;
import org.schorn.ella.http.ActiveHTTP.ActiveSubmit;
import org.schorn.ella.http.ActiveHTTP.ContextServer;
import org.schorn.ella.io.EndPoint.URIPoint;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.util.Functions;
import org.schorn.ella.node.OpenNode;

/**
 *
 * @author schorn
 *
 */
class ContextServerImpl implements ContextServer {

    static private final Logger LGR = LoggerFactory.getLogger(ContextServerImpl.class);

    /**
     * There is only one instance of ContextServer. Each tIt can be used for
     * multiple contexts but per user there is a single primary context
     */
    static final ContextServer ContextServer = new ContextServerImpl();

    /**
     *
     */
    static private Map<Long, ActiveSubmit> CALLER_STATE = new HashMap<>();

    /**
     *
     * @param uriPoint
     * @throws MalformedURLException
     */
    static public void register(URIPoint uriPoint) throws MalformedURLException {
        CALLER_STATE.put(Long.valueOf(Thread.currentThread().getId()), new ActiveSubmitImpl(uriPoint));
    }

    /**
     *
     * @return
     */
    private ActiveSubmit activeSubmit() {
        return CALLER_STATE.get(Thread.currentThread().getId());
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public ActiveMsg<ActiveData> query(ObjectType aggregateType, ActiveData activeData) {
        ActiveMsg<ActiveData> activeMsg = ActiveMsg.createQuery(aggregateType, activeData);
        this.activeSubmit().accept(activeMsg);
        try {
            ActiveMsg<ActiveData> activeMsgResponse = (ActiveMsg<ActiveData>) this.activeSubmit().call();
            activeMsgResponse.throwException();
        } catch (Exception e) {
            LGR.error("{}.query() - Exception: {}", Functions.getStackTraceAsString(e));
        }
        return activeMsg;
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public ActiveMsg<ActiveData> update(ObjectType aggregateType, ActiveData activeData) {
        ActiveMsg<ActiveData> activeMsg = ActiveMsg.createUpdate(aggregateType, activeData);
        this.activeSubmit().accept(activeMsg);
        try {
            /*
			 * Asynchronous Update (if there was a need)
             */
            //AppServer.executorService().submit(this.activeSubmit);
            ActiveMsg<ActiveData> activeMsgResponse = (ActiveMsg<ActiveData>) this.activeSubmit().call();
            activeMsgResponse.throwException();
        } catch (Exception e) {
            LGR.error("{}.submit() - Exception: {}", Functions.getStackTraceAsString(e));
        }
        return activeMsg;
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public ActiveMsg<OpenNode> query(String context, String aggregate_type, OpenNode openNode) {
        ActiveMsg<OpenNode> activeMsg = ActiveMsg.createQuery(context, aggregate_type, openNode);
        this.activeSubmit().accept(activeMsg);
        try {
            ActiveMsg<OpenNode> activeMsgResponse = (ActiveMsg<OpenNode>) this.activeSubmit().call();
            activeMsgResponse.throwException();
        } catch (Exception e) {
            LGR.error("{}.query() - Exception: {}", Functions.getStackTraceAsString(e));
        }
        return activeMsg;
    }

    /**
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public ActiveMsg<OpenNode> update(String context, String aggregate_type, OpenNode openNode) {
        ActiveMsg<OpenNode> activeMsg = ActiveMsg.createUpdate(context, aggregate_type, openNode);
        this.activeSubmit().accept(activeMsg);
        try {
            /*
			 * Asynchronous Update (if there was a need)
             */
            //AppServer.executorService().submit(this.activeSubmit);
            ActiveMsg<OpenNode> activeMsgResponse = (ActiveMsg<OpenNode>) this.activeSubmit().call();
            activeMsgResponse.throwException();
        } catch (Exception e) {
            LGR.error("{}.submit() - Exception: {}", Functions.getStackTraceAsString(e));
        }
        return activeMsg;
    }

}
