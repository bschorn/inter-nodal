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
package org.schorn.ella.impl.repo;

import java.util.function.Predicate;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.repo.RepoSupport.ActiveFilter;

/**
 * This implementation uses the Renewable interface.
 *
 * The default constructor creates a template instance (not for real use).
 *
 * Only the private constructor with parameter creates a usable instance. The
 * private constructor is called by the renew() factory method on the template
 * instance.
 *
 * @author schorn
 *
 */
public class ActiveFilterImpl extends AbstractContextual implements ActiveFilter {

    private final Predicate<ActiveData> predicate;

    public ActiveFilterImpl(AppContext context) {
        super(context);
        this.predicate = null;
    }

    public ActiveFilterImpl(AppContext context, Predicate<ActiveData> predicate) {
        super(context);
        this.predicate = predicate;
    }

    @Override
    public boolean test(ActiveData activeData) {
        if (this.predicate == null) {
            return false;
        }
        return this.predicate.test(activeData);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ActiveFilter renew(Object... params) {
        AppContext context = this.context();
        Predicate<ActiveData> predicate = this.predicate;
        for (Object param : params) {
            if (param instanceof Predicate<?>) {
                predicate = (Predicate<ActiveData>) param;
            } else if (param instanceof AppContext) {
                context = (AppContext) param;
            }
        }
        return new ActiveFilterImpl(context, predicate);
    }

}
