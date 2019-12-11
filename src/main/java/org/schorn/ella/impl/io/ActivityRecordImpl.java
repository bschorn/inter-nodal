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
package org.schorn.ella.impl.io;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.ActiveIO.ActivityLogWriter;
import org.schorn.ella.io.ActiveIO.ActivityRecord;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
class ActivityRecordImpl extends AbstractContextual implements ActivityRecord<ObjectData, String> {

    private static final Logger LGR = LoggerFactory.getLogger(ActivityRecordImpl.class);

    private final Supplier<ObjectData> supplier;
    private final Transform<ObjectData, String> transform;
    private ActivityLogWriter<?> writer;

    @SuppressWarnings("unchecked")
    ActivityRecordImpl(AppContext context) {
        super(context);
        this.supplier = (Supplier<ObjectData>) ActivityRecord.getActivitySupplier(this.context());
        this.transform = (Transform<ObjectData, String>) TransformProvider.provider().getTransform(this.context(), Format.ActiveNode, Format.JsonRecord);
    }

    @Override
    public boolean isReady() {
        return (this.getWriter() != null && this.writer.isReady());
    }

    @Override
    public boolean isContextOpen() {
        return this.context().isOpen();
    }

    @Override
    public Supplier<ObjectData> getActivity() {
        if (this.supplier == null) {
            LGR.error(String.format("%s.getActivity() - there is no activity for context '%s'",
                    this.getClass().getSimpleName(),
                    this.context().name()));
        }
        return this.supplier;
    }

    @Override
    public Transform<ObjectData, String> getTransform() {
        if (this.transform == null) {
            LGR.error(String.format("%s.getTransform() - there is no transformer for context '%s'",
                    this.getClass().getSimpleName(),
                    this.context().name()));
        }
        return this.transform;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public ActivityLogWriter getWriter() {
        if (this.writer == null) {
            this.writer = new ActivityLogWriterImpl(this.context());
            if (this.writer == null) {
                LGR.error(String.format("%s.getWriter() - there is no writer for context '%s'",
                        this.getClass().getSimpleName(),
                        this.context().name()));
                System.exit(1);
            } else if (!this.writer.isReady()) {
                try {
                    this.writer.throwException();
                } catch (Exception ex) {
                    LGR.error(String.format("%s.getWriter() - there is no writer for context '%s'. Exception: %s",
                            this.getClass().getSimpleName(),
                            this.context().name(),
                            Functions.getStackTraceAsString(ex)));
                    System.exit(1);
                }
            }
        }
        return writer;
    }

}
