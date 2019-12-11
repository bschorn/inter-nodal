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

import org.schorn.ella.sql.RDBMS;
import org.schorn.ella.sql.RDBMS.SQLDialect;
import org.schorn.ella.sql.RDBMS.SQLDirective;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.sql.ActiveSQL;

/**
 *
 * @author schorn
 *
 */
class SynchronizeObjectsExperimentalImpl extends SynchronizeObjectsImpl implements ActiveSQL.SynchronizeObjects.Experimental {

    private final RDBMS.SQLDirective[] directives = new RDBMS.SQLDirective[]{
        RDBMS.PhrasalTemplates.CREATE_TEMP_TARGET_TABLE,
        RDBMS.Combine(
        RDBMS.PhrasalTemplates.INSERT_ROWS_TEMP_TARGET_TABLE_BY_SELECT,
        RDBMS.Custom.DATA_LEVEL,
        RDBMS.Commands.UNION,
        RDBMS.PhrasalTemplates.SELECT_PSEUSDO_ROW_FROM_DUAL
        ),
        RDBMS.Transaction(
        RDBMS.PhrasalTemplates.UPDATE_TARGET_TABLE_FROM_TEMP,
        RDBMS.PhrasalTemplates.DELETE_ROWS_TEMP_TARGET_TABLE_BY_JOIN_TARGET_TABLE,
        RDBMS.PhrasalTemplates.INSERT_ROWS_TARGET_TABLE_FROM_TEMP
        )
    };

    protected SynchronizeObjectsExperimentalImpl(AppContext context) {
        super(context, RDBMS.Dialects.DB2);
    }

    protected SynchronizeObjectsExperimentalImpl(AppContext context, SQLDialect dialect) {
        super(context, dialect);
    }

    @Override
    public SynchronizeObjectsExperimentalImpl renew(Object... params) {
        AppContext context = this.context();
        RDBMS.SQLDialect dialect = this.dialect();
        for (Object param : params) {
            if (param instanceof AppContext) {
                context = (AppContext) param;
            } else if (param instanceof RDBMS.SQLDialect) {
                dialect = (RDBMS.SQLDialect) param;
            }
        }
        return new SynchronizeObjectsExperimentalImpl(context, dialect);
    }

    @Override
    public SQLDirective[] directives() {
        return this.directives;
    }

}
