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
package org.schorn.ella.impl.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.sql.RDBMS.JDBCExecuteStep;

/**
 *
 * @author schorn
 *
 */
public class JDBCExecuteStepImpl extends AbstractContextual implements JDBCExecuteStep {

    private final StepType stepType;
    private final Object[] parameters;

    public JDBCExecuteStepImpl(AppContext context) {
        super(context);
        this.stepType = null;
        this.parameters = null;
    }

    public JDBCExecuteStepImpl(AppContext context, StepType stepType, Object[] parameters) {
        super(context);
        this.stepType = stepType;
        this.parameters = parameters;
    }

    @Override
    public JDBCExecuteStep renew(Object... params) {
        StepType stepType = this.stepType;
        List<Object> objects = new ArrayList<>();
        for (Object param : params) {
            if (param instanceof StepType) {
                stepType = (StepType) param;
            } else {
                objects.add(param);
            }
        }
        return new JDBCExecuteStepImpl(this.context(), stepType, objects.toArray(new Object[0]));
    }

    @Override
    public StepType stepType() {
        return this.stepType;
    }

    @Override
    public Object[] getParameters() {
        return this.parameters;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("", this.stepType.name(), "");
        if (this.parameters != null) {
            for (Object param : this.parameters) {
                joiner.add("\n-> " + param.toString());
            }
        }
        return joiner.toString();
    }

}
