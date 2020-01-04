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

import java.util.HashMap;
import java.util.Map;
import org.schorn.ella.AbstractProvider;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.event.ActiveEvent;
import org.schorn.ella.event.ActiveEvent.DataEventManager;
import org.schorn.ella.event.EventProvider;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public class EventProviderImpl extends AbstractProvider implements EventProvider {

    private static final Logger LGR = LoggerFactory.getLogger(EventProviderImpl.class);

    private final Map<AppContext, DataEventManager> dataEventManagers = new HashMap<>();

    @Override
    public void init() {
        this.mapInterfaceToImpl(ActiveEvent.DataEventManager.class, DataEventManagerImpl.class);
        this.mapInterfaceToImpl(ActiveEvent.ObjectDataEvent.class, ObjectDataEventImpl.class);
        this.mapInterfaceToImpl(ActiveEvent.EventLogListener.class, EventLogListenerImpl.class);

    }

    @Override
    public DataEventManager dataEventManager(AppContext nodeContext) {
        return this.dataEventManagers.get(nodeContext);
    }

    @Override
    public void registerContext(AppContext context) throws Exception {
        super.registerContext(context);
        try {
            DataEventManager mgr = this.createInstance(DataEventManager.class, context);
            this.dataEventManagers.put(context, mgr);
            LGR.info(String.format("%s.registerContext('%s') - createInstance of %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    DataEventManager.class.getSimpleName()
            ));
        } catch (Exception ex) {
            LGR.error(Functions.stackTraceToString(ex));
        }
    }

}
