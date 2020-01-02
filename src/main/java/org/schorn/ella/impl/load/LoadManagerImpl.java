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
package org.schorn.ella.impl.load;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.load.ActiveObjectLoad.LoadManager;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.repo.ActiveRepo.ServicesRepo;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 * @param <T>
 */
public class LoadManagerImpl extends AbstractContextual implements LoadManager<OpenNode> {

    private static final Logger LGR = LoggerFactory.getLogger(LoadManagerImpl.class);

    private AtomicBoolean started = new AtomicBoolean(false);
    private int threadCount = 1;
    private AtomicInteger threadNumber = new AtomicInteger(1);
    private AtomicInteger accepted = new AtomicInteger(0);
    private AtomicInteger rejected = new AtomicInteger(0);
    private AtomicInteger submitted = new AtomicInteger(0);
    //private AtomicInteger submitted = new AtomicInteger(0);
    private final Transform<OpenNode, StructData> transform;
    private final BlockingQueue<OpenNode> queue = new LinkedBlockingQueue<>();
    private final List<Thread> threads = new ArrayList<>();
    private final int emptyCycleLimit = 100;
    private final ValueType keyType;

    public LoadManagerImpl(AppContext context, Transform<OpenNode, StructData> transform) {
        super(context);
        this.transform = transform;
        this.keyType = ValueType.get(context, "dealName");
    }

    @Override
    public void accept(OpenNode openNode) {
        this.queue.add(openNode);
        this.accepted.incrementAndGet();
    }

    @Override
    public void setThreadCount(int threadCount) {
        if (threadCount <= 0 || threadCount > 20) {
            return;
        }
        this.threadCount = threadCount;
    }

    @Override
    public void start() {
        if (this.started.get()) {
            return;
        }
        this.started.set(true);

        Runnable task = () -> {
            Thread.currentThread().setName(String.format("Loader #%d", this.threadNumber.getAndIncrement()));

            ServicesRepo repo = ServicesRepo.get(this.context());
            int emptyHanded = 0;
            while (this.started.get()) {
                try {
                    OpenNode openNode = this.queue.poll(1500, TimeUnit.MILLISECONDS);
                    if (openNode != null) {
                        try {
                            if (openNode.name().equals(this.getClass().getSimpleName())) {
                                return;
                            }
                            ObjectData objectData = (ObjectData) this.transform.apply(openNode);
                            if (objectData != null) {
                                //ActiveEvent.DataEventManager.get(this.context()).fire(this.context(), ActiveEvent.EventFlag.DATA_LOAD_EVENT, objectData);
                                LGR.debug("{}.start() - repo.submit()", this.getClass().getSimpleName(), objectData.name());
                                repo.submit(objectData);
                            } else {
                                this.transform.throwException();
                            }
                        } catch (Exception ex) {
                            this.rejected.incrementAndGet();
                            LGR.error(Functions.stackTraceToString(ex));
                        }
                    } else {
                        if (++emptyHanded > this.emptyCycleLimit) {
                            LGR.info(this.toString());
                            LGR.info("{} quitting. Nothing to do.", this.getClass().getSimpleName());
                            return;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        for (int i = 0; i < this.threadCount; i += 1) {
            this.threads.add(new Thread(task));
        }
        this.threads.stream().forEach(thr -> thr.start());
    }

    @Override
    public void stop() {
        /*
		 * Stops the outer loop of Task
         */
        this.started.set(false);
        /*
		 * How many threads still alive?
         */
        while (this.threads.stream().filter(thr -> thr.isAlive()).count() > 0) {
            /*
			 * If queue is empty, then we are blocking on queue.take()
             */
            if (this.queue.isEmpty()) {
                /*
				 * Create and send a stop indicator object into the queue
                 */
                this.queue.add(OpenNode.createObject(this.getClass().getSimpleName()));
                try {
                    /*
					 * Wait a half sec before going onto the next thread
                     */
                    Thread.sleep(500);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void kill() {
        /*
		 * Drain the queue then stop
         */
        this.queue.clear();
        stop();
    }

    @Override
    public int accepted() {
        return this.accepted.get();
    }

    @Override
    public int rejected() {
        return this.rejected.get();
    }

    @Override
    public int submitted() {
        return this.submitted.get();
    }

    @Override
    public String toString() {
        return String.format("Load SOD: processed: %d, submitted: %d, rejected: %d",
                accepted(), submitted(), rejected());

    }
}
