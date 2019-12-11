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
package org.schorn.ella.load;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.schorn.ella.context.AppContext;
//import org.schorn.ella.impl.load.LoadManagerImpl;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;
import org.slf4j.LoggerFactory;

/**
 *
 * The loading of inputs that are delivered as objects with or without hiearchy.
 *
 * JSON, XML, ...
 *
 *
 * @author schorn
 *
 */
public interface ActiveObjectLoad {

    static final org.slf4j.Logger LGR = LoggerFactory.getLogger(ActiveObjectLoad.class);

    /**
     * BulkProcessor<R>
     *
     * The bulk data will be accessed through the 'Reader reader()' The
     * implementor will create units of T to pass into 'void process(T)' which
     * has a default implementation that converts T into R using 'Function<T,R>
     * function()' and will be filtered by 'Predicate<R> predicate()' to
     * determine whether to pass to 'Consumer<R> consumer'.
     *
     *
     */
    interface BulkProcessor<T, R> extends Runnable {

        Reader reader();

        Function<T, R> function();

        Predicate<R> predicate();

        Consumer<R> consumer();

        Consumer<R> monitor();

        boolean monitoring();

        void setLimit(int limit);

        void setMonitoring(Consumer<R> monitor);

        default void process(T t) {
            R r = function().apply(t);
            if (predicate().test(r)) {
                consumer().accept(r);
                if (monitoring()) {
                    monitor().accept(r);
                }
            }
        }
    }

    /**
     * JsonBulkProcessor
     *
     *
     */
    interface JsonBulkProcessor extends BulkProcessor<String, OpenNode> {

        static public JsonBulkProcessor create(AppContext context, Reader reader, Predicate<OpenNode> predicate, Consumer<OpenNode> consumer) throws Exception {
            @SuppressWarnings("unchecked")
            Transform<String, OpenNode> transform = (Transform<String, OpenNode>) TransformProvider.provider().getTransform(context, Format.JSON, Format.OpenNode);
            return LoadProvider.provider().createInstance(JsonBulkProcessor.class, reader, transform, predicate, consumer);
        }
    }

    /**
     *
     * @param context
     * @param fileOrContent
     * @return
     * @throws IOException
     */
    static public Reader createBulkReader(AppContext context, String fileOrContent) throws IOException {
        if (fileOrContent.length() < 1000) {
            Path path = Paths.get(fileOrContent);
            if (Files.exists(path)) {
                return Files.newBufferedReader(path);
            }
        }
        return new StringReader(fileOrContent);
    }

    interface LoadManager<T> extends Consumer<T> {

        void setThreadCount(int threadCount);

        void start();

        void stop();

        void kill();

        int accepted();

        int rejected();

        int submitted();
    }

    /**
     *
     * @param classOfT
     * @return
     */
    static public LoadManager<OpenNode> getLoadManager(AppContext context) {
        @SuppressWarnings("unchecked")
        Transform<OpenNode, StructData> transform = (Transform<OpenNode, StructData>) TransformProvider.provider().getTransform(context, Format.OpenNode, Format.ActiveNode);
        try {
            return LoadProvider.provider().createInstance(LoadManager.class, context, transform);
        } catch (Exception ex) {
            LGR.error(ex.getMessage());
        }
        return null;
    }
}
