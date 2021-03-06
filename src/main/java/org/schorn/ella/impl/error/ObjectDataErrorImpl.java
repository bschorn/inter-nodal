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

import java.time.LocalDateTime;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.error.ActiveError.ObjectDataError;
import org.schorn.ella.node.ActiveNode.ObjectData;

/**
 *
 * @author schorn
 *
 */
public class ObjectDataErrorImpl implements ObjectDataError {

    private final AppContext context;
    private final ErrorFlag flag;
    private final LocalDateTime timestamp;
    private final ObjectData data;

    ObjectDataErrorImpl(AppContext context) {
        this.context = context;
        this.flag = ErrorFlag.OBJECT_DATA_BUILDER_ERROR;
        this.data = null;
        this.timestamp = LocalDateTime.now();
    }

    /**
     *
     * @param context
     * @param flag
     * @param timestamp
     * @param data
     */
    private ObjectDataErrorImpl(AppContext context, ErrorFlag flag, ObjectData data) {
        this.context = context;
        this.flag = flag;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    @Override
    public ErrorFlag flag() {
        return this.flag;
    }

    @Override
    public LocalDateTime timestamp() {
        return this.timestamp;
    }

    @Override
    public ObjectData data() {
        return this.data;
    }

    @Override
    public ObjectDataError renew(Object... params) {
        ErrorFlag flag = this.flag;
        ObjectData data = this.data;
        for (Object param : params) {
            if (param instanceof ErrorFlag) {
                flag = (ErrorFlag) param;
            } else if (param instanceof ObjectData) {
                data = (ObjectData) param;
            }
        }
        return new ObjectDataErrorImpl(this.context, flag, data);
    }

}
