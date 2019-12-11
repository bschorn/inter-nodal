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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.repo.RepoActions.LogActivity;
import org.schorn.ella.util.Functions;

/**
 * This connects the Repo's Persist interface with the Supplier interface needed
 * by IO's ActivityRecord.
 *
 * @author schorn
 *
 */
public class LogActivityImpl extends AbstractContextual implements LogActivity {

    private static final Logger LGR = LoggerFactory.getLogger(LogActivityImpl.class);

    private final BlockingQueue<ObjectData> queue;

    LogActivityImpl(AppContext context) {
        super(context);
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void accept(ObjectData objectData) {
        this.queue.add(objectData);
    }

    @Override
    public ObjectData get() {
        while (true) {
            try {
                return this.queue.take();
            } catch (InterruptedException e) {
                LGR.error(String.format("%s.get() - queue.take() interrupted. %s",
                        this.getClass().getSimpleName(),
                        Functions.stackTraceToString(e)));
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e1) {
                    LGR.error(String.format("%s.get() - sleep() interrupted. %s",
                            this.getClass().getSimpleName(),
                            Functions.stackTraceToString(e1)));
                }
            }
        }
    }

}
