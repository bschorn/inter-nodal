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
package org.schorn.ella.impl.node;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.util.Functions;
import org.schorn.ella.util.StringCached;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to create an 'error_data' object
 *
 *
 * @author schorn
 *
 */
public class ErrorData {

    static final Logger LGR = LoggerFactory.getLogger(ErrorData.class);

    static public ErrorData create(ActiveNode.ActiveType activeType, Class<?> classOf, Method methodOf) {
        return new ErrorData(activeType, classOf, methodOf);
    }

    private ActiveNode.ActiveType activeType;
    private Class<?> classOf;
    private Method methodOf;
    private StringCached message = null;
    private LocalDateTime timestamp = LocalDateTime.now();
    private ActiveNode.ActiveData parentData = null;
    private ActiveNode.ActiveData childData = null;

    public ErrorData(ActiveNode.ActiveType activeType, Class<?> classOf, Method methodOf) {
        this.activeType = activeType;
        this.classOf = classOf;
        this.methodOf = methodOf;
    }

    public void setDetail(String message) {
        this.message = new StringCached(message);
    }

    public void setDetail(String message, ActiveNode.ActiveData parentData) {
        this.message = new StringCached(message);
        this.parentData = parentData;
    }

    public void setDetail(String message, ActiveNode.ActiveData parentData, ActiveNode.ActiveData childData) {
        this.message = new StringCached(message);
        this.parentData = parentData;
        this.childData = childData;
    }

    public ActiveNode.ObjectData asActiveData(ActiveData... activeDatas) {
        try {
            ActiveNode.ObjectType errorType = ActiveNode.ObjectType.get(AppContext.Common, "error_data");
            ActiveNode.ObjectData.Builder errorData = errorType.builder();
            errorData.add(ActiveNode.ValueType.get(AppContext.Common, "context").create(this.activeType.context().name()));
            errorData.add(ActiveNode.ValueType.get(AppContext.Common, "active_role").create(this.activeType.role().name()));
            errorData.add(ActiveNode.ValueType.get(AppContext.Common, "active_type").create(this.activeType.name()));
            errorData.add(ActiveNode.ValueType.get(AppContext.Common, "error_ts").create(timestamp));
            errorData.add(ActiveNode.ValueType.get(AppContext.Common, "class_name").create(this.classOf.getName()));
            errorData.add(ActiveNode.ValueType.get(AppContext.Common, "method_name").create(this.methodOf.getName()));
            if (this.message != null) {
                errorData.add(ActiveNode.ValueType.get(AppContext.Common, "error_message").create(this.message.toString()));
            }
            if (this.parentData != null) {
                errorData.add(ActiveNode.ValueType.get(AppContext.Common, "parent_data").create(this.parentData.toString()));
            }
            if (this.childData != null) {
                errorData.add(ActiveNode.ValueType.get(AppContext.Common, "child_data").create(this.childData.toString()));
            }
            return errorData.build();
        } catch (Exception ex) {
            LGR.error(Functions.getStackTraceAsString(ex));
        }
        return null;
    }

    @Override
    public String toString() {
        return this.message.toString();
    }
}
