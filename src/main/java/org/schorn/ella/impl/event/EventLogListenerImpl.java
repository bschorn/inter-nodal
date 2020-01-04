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
package org.schorn.ella.impl.event;

import java.util.function.Predicate;
import java.util.stream.Stream;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.event.ActiveEvent;
import org.schorn.ella.event.ActiveEvent.EventLogListener;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.repo.RepoData.EventLogBroker;
import org.schorn.ella.repo.RepoProvider;

/**
 *
 * @author schorn
 *
 */
public class EventLogListenerImpl extends AbstractContextual implements EventLogListener {

    private ActiveEvent.DataEventManager manager;
    private final ActiveEvent.EventFlag flag = ActiveEvent.EventFlag.REPO_CHANGE_EVENT;

    public EventLogListenerImpl(AppContext context) {
        super(context);
        EventLogBroker broker = RepoProvider.provider().getMingleton(EventLogBroker.class, context);
        broker.join(this);
        this.manager = ActiveEvent.DataEventManager.get(context);
    }

    @Override
    public void accept(ObjectData objectData) {
        if (this.manager == null) {
            this.manager = ActiveEvent.DataEventManager.get(this.context());
        }
        if (this.manager != null) {
            this.manager.fire(this.context(), this.flag, objectData);
        }
    }

    /**
     * Not neccessary
     */
    @Override
    public Stream<ObjectData> read(ObjectType objectType, Predicate<ActiveData> filter) {
        return null;
    }

}
