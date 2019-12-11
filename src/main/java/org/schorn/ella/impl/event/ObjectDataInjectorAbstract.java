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

import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.event.ActiveEvent.ObjectDataInjector;
import org.schorn.ella.util.Functions;

/**
 * ObjectDataReactor Implementations
 *
 * Auto-register upon construction.
 *
 *
 * @author schorn
 *
 */
public abstract class ObjectDataInjectorAbstract implements ObjectDataInjector {

    private static final Logger LGR = LoggerFactory.getLogger(ObjectDataInjectorAbstract.class);

    private final AppContext context;
    private final EnumSet<EventFlag> enumSet;

    public ObjectDataInjectorAbstract(AppContext context, EnumSet<EventFlag> enumSet) {
        this.context = context;
        this.enumSet = enumSet;
        try {
            DataEventManager.get(context).register(this);
        } catch (Exception ex) {
            LGR.error(Functions.stackTraceToString(ex));
        }
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    @Override
    public boolean test(ObjectDataEvent event) {
        return this.enumSet.contains(event.flag());
    }

}
