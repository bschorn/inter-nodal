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
package org.schorn.ella.impl.error;

import java.util.EnumSet;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.error.ActiveError.ObjectDataResponder;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public abstract class ObjectDataResponderImpl implements ObjectDataResponder {

    private static final Logger LGR = LoggerFactory.getLogger(ObjectDataResponderImpl.class);

    private final AppContext context;
    private final EnumSet<ErrorFlag> enumSet;

    ObjectDataResponderImpl(AppContext context, EnumSet<ErrorFlag> enumSet) {
        this.context = context;
        this.enumSet = enumSet;
        try {
            DataErrorManager.get(context).register(this);
        } catch (Exception ex) {
            LGR.error(Functions.stackTraceToString(ex));
        }
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    @Override
    public boolean test(ObjectDataError error) {
        return this.enumSet.contains(error.flag());
    }

}
