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

import org.schorn.ella.context.AppContext;
import org.schorn.ella.http.ActiveHTTP.ActiveMsg;
import org.schorn.ella.http.ActiveHTTP.ActiveMsg.MsgAction;
import org.schorn.ella.http.ActiveHTTP.ContextServer;
import org.schorn.ella.http.HTTPProvider;
import org.schorn.ella.io.EndPoint.URIPoint;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.OpenNode;

/**
 *
 * @author schorn
 *
 */
public class HTTPProviderImpl extends AbstractProvider implements HTTPProvider {

    @Override
    public void init() throws Exception {
    }

    @Override
    public void registerContext(AppContext context) throws Exception {
    }

    @Override
    public ContextServer getContextServer(URIPoint uriPoint) throws Exception {
        ContextServerImpl.register(uriPoint);
        return ContextServerImpl.ContextServer;
    }

    @Override
    public ActiveMsg<ActiveData> createActiveMsg(MsgAction msgAction, ObjectType aggregateType, ActiveData activeData) {
        return new ActiveMsgImpl<ActiveData>(msgAction, aggregateType.name(), activeData);
    }

    /**
     *
     * @param msgAction
     * @param aggregate_type
     * @param openNode
     * @param context
     * @return
     */
    @Override
    public ActiveMsg<OpenNode> createActiveMsg(MsgAction msgAction, String aggregate_type, OpenNode openNode,
            String context) {
        ActiveMsgImpl<OpenNode> am = new ActiveMsgImpl<OpenNode>(msgAction, aggregate_type, openNode);
        am.setContext(context);
        return am;
    }

}
