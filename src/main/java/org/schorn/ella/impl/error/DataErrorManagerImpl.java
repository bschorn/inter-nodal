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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.error.ActiveError.DataErrorManager;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
public class DataErrorManagerImpl implements DataErrorManager {

    private static final Logger LGR = LoggerFactory.getLogger(DataErrorManagerImpl.class);

    private final AppContext context;
    @SuppressWarnings("rawtypes")
    private final List<DataResponder> responders;

    DataErrorManagerImpl(AppContext context) {
        this.context = context;
        this.responders = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    @Override
    public AppContext context() {
        return this.context;
    }

    @Override
    public void fire(AppContext context, ErrorFlag flag, ActiveData data) {
        try {
            switch (data.role()) {
                case Value:
                    break;
                case Object:
                    this.fire(ObjectDataError.create(context, flag, (ObjectData) data));
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
    public void fire(DataError<?> error) {
        this.responders.forEach(responder -> {
            if (responder.test(error)) {
                responder.accept(error.data());
            }
        });
    }

    @Override
    public void register(DataResponder<?, ?> responder) {
        this.responders.add(responder);
    }

}
