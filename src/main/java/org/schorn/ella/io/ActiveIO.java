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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.FunctionalException;
import org.schorn.ella.Mingleton;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.context.ActiveContext.Contextual;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.util.Functions;
import org.schorn.ella.repo.RepoProvider;

/**
 * Input/Output Interfaces
 *
 *
 * @author schorn
 *
 */
public interface ActiveIO {

    static final Logger LGR = LoggerFactory.getLogger(ActiveIO.class);

    interface ConsumeTransformedActivity<R> extends Consumer<R> {
    }

    interface RecordTransformedActivity<R> extends Consumer<R> {
    }

    interface ActivityLogWriter<R> extends Consumer<R>, AutoCloseable, Contextual, FunctionalException {

        boolean isReady();
    }

    /**
     * Recovering Activity is a single one-shot full read of the recovery source
     * as a stream directly into the Repo.
     *
     */
    interface ActivityRecovery<T, R> extends Contextual, Runnable, Mingleton {

        static ActivityRecovery<?, ?> create(AppContext context) throws Exception {
            return IOProvider.provider().createInstance(ActivityRecovery.class, context);
        }

        Supplier<T> getActivity();

        Transform<T, R> getTransform();

        Consumer<R> getConsumer();

        /**
         * The default logic of re-loading the activity after a process exit.
         */
        @Override
        default void run() {
            Supplier<T> activity = getActivity();
            Transform<T, R> transform = getTransform();
            Consumer<R> consumer = getConsumer();
            T t = activity.get();
            while (t != null) {
                consumer.accept(transform.apply(t));
                t = activity.get();
            }
        }

    }

    /**
     *
     * Recording Activity
     *
     */
    interface ActivityRecord<T, R> extends Contextual, Runnable, Mingleton {

        static public ActivityRecord<?, ?> create(AppContext context) throws Exception {
            return IOProvider.provider().createInstance(ActivityRecord.class, context);
        }

        /**
         * The Activity Supplier is implemented in the Repo Implementation
         * Library and connects the activity stream going into the Repo to the
         * ActivityRecord's getActivity() method.
         *
         * @param context
         * @return
         */
        static Supplier<?> getActivitySupplier(AppContext context) {
            return RepoProvider.provider().getActivitySupplier(context);
        }

        boolean isReady();

        boolean isContextOpen();

        Supplier<T> getActivity();

        Transform<T, R> getTransform();

        ActivityLogWriter<R> getWriter() throws Exception;

        /**
         * The default logic of recording activity for the purpose of recovery
         * if the process was to exit.
         */
        @SuppressWarnings({"rawtypes", "unchecked"})
        @Override
        default void run() {
            String origThreadName = Thread.currentThread().getName();
            Thread.currentThread().setName(String.format("Recording-%s", this.context().name()));
            /*
			 * Open up writer 
             */
            Transform<T, R> transform = getTransform();
            try (ActivityLogWriter writer = this.getWriter()) {
                while (isContextOpen()) {
                    T t = getActivity().get();
                    R r = transform.apply(t);
                    if (r != null) {
                        writer.accept(r);
                    } else {
                        transform.throwException();
                    }
                }

            } catch (Exception e) {
                LGR.error("{}.run() - Unable to re-load activity for context '{}'. Exception: {}",
                        this.getClass().getSimpleName(),
                        this.context().name(),
                        Functions.getStackTraceAsString(e));
                this.context().exit(Functions.stackTraceToString(e));
            } finally {
                Thread.currentThread().setName(origThreadName);
            }
        }
    }

    /**
     * Tabular - interface for processing tabular data.
     *
     * 1) Delimited Files 2) Relational Database Tables
     *
     */
    interface Tabular {

        static Tabular create(AppContext context, String rowName, List<Object> fieldNames) throws Exception {
            return IOProvider.provider().createInstance(Tabular.class, context, rowName, fieldNames);
        }

        String getRowName();

        /**
         *
         * @return
         */
        List<String> getFieldNames();

        List<Object> getFieldValues();

        default void setFieldValues(List<Object> fieldValues) {
        }

        default boolean allowDynamicFields() {
            return false;
        }

    }

    /**
     *
     *
     *
     */
    interface SynchronizeDB extends Runnable, Mingleton {

        static SynchronizeDB create(AppContext context, EndPoint.DatabasePoint databasePoint) {
            return IOProvider.provider().getMingleton(SynchronizeDB.class, context, databasePoint);
        }

    }
}
