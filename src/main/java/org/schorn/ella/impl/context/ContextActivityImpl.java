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
package org.schorn.ella.impl.context;

import org.schorn.ella.context.ActiveContext.Activity;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.ActiveIO.ActivityRecord;
import org.schorn.ella.io.ActiveIO.ActivityRecovery;
import org.schorn.ella.io.EndPoint;
import org.schorn.ella.server.ActiveServer;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class ContextActivityImpl implements Activity {

    static private final Logger LGR = LoggerFactory.getLogger(ContextActivityImpl.class);

    private final AppContext context;
    private EndPoint<?> endPoint;
    private ActivityRecord<?, ?> activityRecord;

    ContextActivityImpl(AppContext context) {
        this.context = context;
        this.activityRecord = null;
    }

    /*
    @Override
    public void setEndPoint(EndPoint<?> endPoint) {
        this.endPoint = endPoint;
    }

    @Override
    public EndPoint<?> getEndPoint() {
        return this.endPoint;
    }
*/
    @Override
    public boolean hasActivity() {
        return this.endPoint.isReady();
    }

    @Override
    public void reloadActivity() {
        try {
            /**
             * Mechanism: LineReaderNode - how to read (read a file by line to
             * get a node) Source: EndPoints - where to read from (FilePoints is
             * a file) Transform: FromJsonString - translate from Json String
             * into Node Destination: AppRepo - repository
             *
             */
            ActivityRecovery.create(this.context).run();

        } catch (Exception ex) {
            LGR.error(String.format("%s.readRecoveryFile() - %s",
                    this.getClass().getSimpleName(),
                    Functions.getStackTraceAsString(ex)));
        }
    }

    @Override
    public void recordActivity() throws Exception {
        if (this.context.hasRepo()) {
            this.activityRecord = ActivityRecord.create(this.context);
            if (this.activityRecord.isReady() && this.activityRecord.isContextOpen()) {
                ActiveServer.executorService().execute(this.activityRecord);
            } else {
                throw new Exception(String.format("%s.recordActivity() - for %s context, the activity recorder was not ready or the context was closed.",
                        this.getClass().getSimpleName(), this.context.name()));
            }
        }
    }

}
