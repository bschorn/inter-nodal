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
package org.schorn.ella.util;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.StringJoiner;

public class MemoryManagement {

    static public final MemoryManagement instance = new MemoryManagement();

    static public class InstrumentationAgent {

        private static volatile Instrumentation globalInstrumentation;

        public static void premain(final String agentArgs, final Instrumentation inst) {
            globalInstrumentation = inst;
        }

        public static long getObjectSize(final Object object) {
            if (globalInstrumentation == null) {
                throw new IllegalStateException("Agent not initialized.");
            }
            return globalInstrumentation.getObjectSize(object);
        }
    }

    public long getRuntimeTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public long getRuntimeFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public long getHeapMemory() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted();
    }

    public long getNonHeapMemory() {
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getCommitted();
    }

    public long getTotalMemory() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getCommitted()
                + ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getCommitted();
    }

    /**
     *
     * @return
     */
    public MemoryUsage getHeapMemoryUsage() {
        return ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
    }

    public MemoryUsage getNonHeapMemoryUsage() {
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n", "", "");

        return joiner.toString();
    }
}
