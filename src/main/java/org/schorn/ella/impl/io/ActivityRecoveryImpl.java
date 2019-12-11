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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.EndPoint;
import org.schorn.ella.io.EndPoint.URIPoint;
import org.schorn.ella.io.ActiveIO.ActivityRecovery;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.repo.ActiveRepo.LoaderRepo;
import org.schorn.ella.util.Functions;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;

/**
 * Activity Recovery Implementation
 *
 * @author schorn
 *
 */
public class ActivityRecoveryImpl extends AbstractContextual implements ActivityRecovery<String, StructData> {

    static private final Logger LGR = LoggerFactory.getLogger(ActivityRecoveryImpl.class);

    /**
     * Activity Supplier
     *
     */
    static class ActivityFileStreamable implements Supplier<String> {

        AppContext context;
        BufferedReader reader;

        ActivityFileStreamable(AppContext context) {
            this.context = context;
            EndPoint<?> endPoint = context.getEndPoint();
            if (endPoint == null) {
                LGR.error("{}.ctor() - EndPoint for context {} was not found.",
                        ActivityFileStreamable.class.getSimpleName(), context.name());
                return;
            }
            switch (endPoint.type()) {
                case Database:
                    break;
                case URI:
                    Path path = Paths.get(((EndPoint.URIPoint) endPoint).get());
                    if (Files.exists(path)) {
                        try {
                            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toString()), "UTF-8"));
                        } catch (Exception ex) {
                            LGR.error("{}.ctor('{}') - {}\n{}",
                                    this.getClass().getSimpleName(),
                                    context.name(),
                                    ex.getMessage(),
                                    Functions.stackTraceToString(ex));
                        }
                    } else {
                        LGR.info("{}.ctor('{}') - file {} does not exist.",
                                this.getClass().getSimpleName(),
                                context.name(),
                                path.toString());
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public String get() {
            try {
                String line = this.reader.readLine();
                if (line == null) {
                    this.reader.close();
                }
                return line;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }
    }

    // members
    private final Supplier<String> activity;
    private final Transform<String, StructData> transform;
    private final Consumer<StructData> consumer;

    // ctor
    ActivityRecoveryImpl(AppContext context) {
        super(context);
        this.activity = new ActivityFileStreamable(this.context());
        this.transform = (Transform<String, StructData>) TransformProvider.provider().getTransform(this.context(), Format.JSON, Format.ActiveNode);
        this.consumer = LoaderRepo.get(this.context());
    }

    @Override
    public Supplier<String> getActivity() {
        return this.activity;
    }

    @Override
    public Transform<String, StructData> getTransform() {
        return this.transform;
    }

    @Override
    public Consumer<StructData> getConsumer() {
        return this.consumer;
    }

}
