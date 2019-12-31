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
package org.schorn.ella.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public interface ReaderWorkerWriter {

    public interface ReaderNode<I> extends Supplier<I>, Runnable {
    }

    public interface WriterNode<O> extends Consumer<O>, Runnable {
    }

    public interface ProcessNode<I, O> extends Consumer<I>, Supplier<O> {
    }

    public interface ReaderFrame<I> extends Supplier<I>, Runnable {
    }

    /**
     * Line Reader from EndPoint
     *
     * Streams to Consumer or Caches to be a Supplier
     *
     */
    public class LineReaderNode implements ReaderNode<String>, AutoCloseable {

        private static final Logger LGR = LoggerFactory.getLogger(LineReaderNode.class);

        @SuppressWarnings("rawtypes")
        EndPoint endPoint;
        ConcurrentLinkedQueue<String> queue;
        Consumer<String> consumer;

        @SuppressWarnings("rawtypes")
        public LineReaderNode(EndPoint endPoint, Consumer<String> consumer) {
            this.endPoint = endPoint;
            this.queue = null;
            this.consumer = consumer;
        }

        @SuppressWarnings("rawtypes")
        public LineReaderNode(EndPoint endPoint) {
            this.endPoint = endPoint;
            this.queue = new ConcurrentLinkedQueue<>();
            this.consumer = (s) -> this.queue.add(s);
        }

        @Override
        public String get() {
            return this.queue.poll();
        }

        @Override
        public void run() {
            switch (this.endPoint.type()) {
                case File:
                    read(((EndPoint.FilePoint) this.endPoint).get());
                    break;
                default:
            }
        }

        protected void read(Path path) {
            if (path == null) {
                LGR.error(String.format("%s.readFile() - path is null.", this.getClass().getSimpleName()));
                return;
            }
            /*
            if (Files.exists(path)) {
                try (Stream<String> stream = Files.lines(path)) {
                    stream.forEach(this.consumer);
                } catch (IOException e) {
                    LGR.error(Functions.getStackTraceAsString(e));
                }
            }
             */
            if (Files.exists(path)) {
                try (FastFileReader reader = new FastFileReader(path.toString())) {
                    this.consumer.accept(reader.readLine());
                } catch (Exception e) {
                    LGR.error(Functions.getStackTraceAsString(e));
                }
            }
        }

        @Override
        public void close() throws Exception {
        }
    }

    /**
     *
     *
     */
    public class FileReaderNode implements ReaderNode<String> {

        private static final Logger LGR = LoggerFactory.getLogger(FileReaderNode.class);

        @SuppressWarnings("rawtypes")
        EndPoint endPoint;
        //ConcurrentLinkedQueue<String> queue;
        Consumer<String> consumer;
        BufferedReader bufRdr;

        /**
         * Stream into supplied Consumer
         *
         * @param endPoint
         * @param consumer
         */
        @SuppressWarnings("rawtypes")
        public FileReaderNode(EndPoint endPoint, Consumer<String> consumer) {
            this.endPoint = endPoint;
            //this.queue = null;
            this.consumer = consumer;
        }

        /**
         * Store into internal Queue for Supplier
         *
         * @param endPoint
         */
        @SuppressWarnings("rawtypes")
        public FileReaderNode(EndPoint endPoint) {
            this.endPoint = endPoint;
            //this.queue = new ConcurrentLinkedQueue<>();
            //this.consumer = (s) -> this.queue.add(s);
            this.consumer = null;
        }

        @Override
        public String get() {
            try {
                return this.bufRdr.readLine();
            } catch (IOException e) {
                LGR.error(Functions.getStackTraceAsString(e));
                return null;
            }
        }

        @Override
        public void run() {
            switch (this.endPoint.type()) {
                case File:
                    read(((EndPoint.FilePoint) this.endPoint).get());
                    break;
                default:
            }
        }

        protected void read(Path path) {
            if (path == null) {
                LGR.error("%s.readFile() - path is null.", this.getClass().getSimpleName());
                return;
            }
            if (this.consumer == null) {
                try {
                    this.bufRdr = new BufferedReader(new FileReader(path.toString()));
                } catch (FileNotFoundException e) {
                    LGR.error(Functions.getStackTraceAsString(e));
                }
            } else {
                try (Stream<String> stream = Files.lines(path)) {
                    stream.forEach(this.consumer);
                } catch (IOException e) {
                    LGR.error(Functions.getStackTraceAsString(e));
                }
            }
        }
    }

}
