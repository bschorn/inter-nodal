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

import java.util.concurrent.CopyOnWriteArrayList;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.event.ActiveEvent.DataEventManager;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class DataEventManagerImpl implements DataEventManager {

    private static final Logger LGR = LoggerFactory.getLogger(DataEventManagerImpl.class);

    private final AppContext context;
    @SuppressWarnings("rawtypes")
    private final CopyOnWriteArrayList<DataInjector> injectors;

    public DataEventManagerImpl(AppContext context) {
        this.context = context;
        this.injectors = new CopyOnWriteArrayList<>();
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    @Override
    public void fire(AppContext context, EventFlag flag, ActiveData data) {
        try {
            switch (data.role()) {
                case Value:
                    break;
                case Object:
                    this.fire(ObjectDataEvent.create(context, flag, (ObjectData) data));
                    break;
                case Array:
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            LGR.error(Functions.stackTraceToString(ex));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void fire(DataEvent<?> event) {
        this.injectors.forEach(injector -> {
            if (injector != null) {
                if (injector.test(event)) {
                    injector.accept(event.data());
                }
            }
        });
    }

    @Override
    public void register(DataInjector<?, ?> injector) {
        this.injectors.add(injector);
    }

    @Override
    public void unregister(DataInjector<?, ?> injector) {
        this.injectors.remove(injector);
    }

}
