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
package org.schorn.ella;

/**
 *
 * @author schorn
 *
 */
public interface Reusable extends AutoCloseable {

    boolean occupied();

    void setCurrentThread(Thread thread);

    Thread getCurrentThread();

    @SuppressWarnings("resource")
    default void open() {
        if (this instanceof Reusable) {
            Reusable reusable = (Reusable) this;
            reusable.setCurrentThread(Thread.currentThread());
        }
    }

    @SuppressWarnings("resource")
    default void close() throws Exception {
        if (this instanceof Reusable) {
            Reusable reusable = (Reusable) this;
            reusable.setCurrentThread(null);
        }
    }

    static public class Impl implements Reusable {

        private Thread thread = null;

        @Override
        public boolean occupied() {
            return this.thread != null;
        }

        @Override
        public void setCurrentThread(Thread thread) {
            this.thread = thread;
        }

        @Override
        public Thread getCurrentThread() {
            return this.thread;
        }

    }
}
