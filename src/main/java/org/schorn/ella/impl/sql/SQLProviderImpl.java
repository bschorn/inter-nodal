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

import org.schorn.ella.AbstractProvider;
import org.schorn.ella.sql.ActiveSQL;
import org.schorn.ella.sql.RDBMS;
import org.schorn.ella.sql.SQLProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class SQLProviderImpl extends AbstractProvider implements SQLProvider {

    private static final Logger LGR = LoggerFactory.getLogger(SQLProviderImpl.class);

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                METHODS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void init() throws Exception {
        this.mapInterfaceToImpl(ActiveSQL.SynchronizeObjects.class, SynchronizeObjectsExperimentalImpl.class);
        //this.mapInterfaceToImpl(ActiveSQL.SynchronizeObjects.Stable.class, SynchronizeObjectsStableImpl.class);
        //this.mapInterfaceToImpl(ActiveSQL.SynchronizeObjects.Experimental.class, SynchronizeObjectsExperimentalImpl.class);
        this.mapInterfaceToImpl(RDBMS.TemplateTagsTransducer.class, TemplateTagsTransducerImpl.class);
        this.mapInterfaceToImpl(RDBMS.SQLInterpreter.class, SQLInterpreterImpl.class);
        this.mapInterfaceToImpl(RDBMS.JDBCExecuteStep.class, JDBCExecuteStepImpl.class);
        this.mapInterfaceToImpl(RDBMS.SQLProgramOutput.class, SQLInterpreterImpl.SQLProgramOutputImpl.class);

    }

}
