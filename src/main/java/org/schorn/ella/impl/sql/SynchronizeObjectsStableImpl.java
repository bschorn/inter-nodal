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

import org.schorn.ella.context.AppContext;
import org.schorn.ella.sql.ActiveSQL;
import org.schorn.ella.sql.RDBMS;
import org.schorn.ella.sql.RDBMS.SQLDirective;

/**
 *
 * @author schorn
 *
 */
public class SynchronizeObjectsStableImpl extends SynchronizeObjectsImpl implements ActiveSQL.SynchronizeObjects.Stable {

    private final RDBMS.SQLDirective[] directives = new RDBMS.SQLDirective[]{
        RDBMS.Transaction(
        RDBMS.PhrasalTemplates.DELETE_ROW_TARGET_TABLE,
        RDBMS.PhrasalTemplates.INSERT_ROW_TARGET_TABLE
        )
    };

    protected SynchronizeObjectsStableImpl(AppContext context) {
        super(context, RDBMS.Dialects.DB2);
    }

    protected SynchronizeObjectsStableImpl(AppContext context, RDBMS.SQLDialect dialect) {
        super(context, dialect);
    }

    @Override
    public SynchronizeObjectsStableImpl renew(Object... params) {
        AppContext context = this.context();
        RDBMS.SQLDialect dialect = this.dialect();
        for (Object param : params) {
            if (param instanceof AppContext) {
                context = (AppContext) param;
            } else if (param instanceof RDBMS.SQLDialect) {
                dialect = (RDBMS.SQLDialect) param;
            }
        }
        return new SynchronizeObjectsStableImpl(context, dialect);
    }

    @Override
    public SQLDirective[] directives() {
        return this.directives;
    }

}
