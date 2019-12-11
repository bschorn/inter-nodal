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

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.EndPoint;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Identity;

/**
 *
 * The loading of inputs that are delivered as tabular data without hiearchy.
 *
 * CSV, TXT, RDBMS, XLS, ...
 *
 *
 * @author schorn
 *
 */
public interface ActiveTabularLoad {

    /**
     * Template
     *
     * @param <T>
     * @param <V>
     */
    interface TypeValue<T> {

        T type();

        Object value();
    }

    /**
     *
     *
     */
    public interface ActiveTypeValue extends TypeValue<ActiveType> {

        ActiveType type();

        Object value();

        static public ActiveTypeValue create(ActiveType activeType, Object value) throws Exception {
            return LoadProvider.provider().createInstance(ActiveTypeValue.class, activeType, value);
        }
    }

    /**
     * RowProcessor
     *
     *
     * @param <R> - array of data fields, row
     * @param <T> - type that determines how the row should be interpreted
     * @param <V> - value that represents the row after transforming
     */
    public interface RowProcessor<R, T, V> {
        // the field definitions (header or schema) by position

        List<T> types();
        // takes a single set (R) of data for an object and parses into field values
        // and passes back a list of field types and values by position

        Function<R, List<V>> parser();
        // validates a single field-value pair skipping by returning false

        Predicate<V> validate();
        // submitting the validated results to an object assembler

        Consumer<List<V>> consumer();
        // process a row

        default void process(R r) {
            // parse row to create a list of fields
            List<V> tv = parser().apply(r);
            // validate the list of fields
            List<V> ftv = tv.stream().filter(validate()).collect(Collectors.toList());
            if (!ftv.isEmpty()) {
                consumer().accept(ftv);
            }
        }
    }

    /**
     *
     *
     *
     * @param <R>
     */
    public interface DbRowProcessor extends RowProcessor<ResultSet, ActiveType, ActiveTypeValue> {

        List<ActiveType> types();

        Function<ResultSet, List<ActiveTypeValue>> parser();

        Predicate<ActiveTypeValue> validate();

        Consumer<List<ActiveTypeValue>> consumer();

        static public DbRowProcessor create(List<ActiveType> types, Function<ResultSet, List<ActiveTypeValue>> parser,
                Predicate<ActiveTypeValue> validate, Consumer<List<ActiveTypeValue>> consumer) throws Exception {
            return LoadProvider.provider().createInstance(DbRowProcessor.class, types, parser, validate, consumer);
        }
    }

    /**
     *
     *
     * @param <V>
     * @param <D>
     * @param <O>
     */
    interface ObjectAssembler<V, D, O> extends Consumer<List<V>> {

        // converts type + value into single field object
        Function<V, D> fieldCreator();
        // validate the field objects

        Predicate<D> dataChecker();
        // converts a list of field objects into a single object of fields

        Function<List<D>, O> objectCreator();
        // validate the object

        Predicate<O> objectChecker();
        // submit to a consumer

        /**
         *
         * @return
         */
        Consumer<O> submitter();

        default void accept(List<V> ftv) {
            List<D> fd = ftv.stream().map(tv -> fieldCreator().apply(tv)).collect(Collectors.toList());
            List<D> ofd = fd.stream().filter(dataChecker()).collect(Collectors.toList());
            O od = objectCreator().apply(ofd);
            if (objectChecker().test(od)) {
                submitter().accept(od);
            }
        }
    }

    /**
     *
     *
     *
     */
    interface ActiveObjectAssembler extends ObjectAssembler<ActiveTypeValue, ActiveData, ObjectData> {

        Function<ActiveTypeValue, ActiveData> fieldCreator();

        Predicate<ActiveData> dataChecker();

        Function<List<ActiveData>, ObjectData> objectCreator();

        Predicate<ObjectData> objectChecker();

        Consumer<ObjectData> submitter();

        static public ActiveObjectAssembler create(Function<ActiveTypeValue, ActiveData> fieldCreator,
                Predicate<ActiveData> dataChecker,
                Function<List<ActiveData>, ObjectData> objectCreator,
                Predicate<ObjectData> objectChecker,
                Consumer<ObjectData> submitter) throws Exception {
            return LoadProvider.provider().createInstance(ActiveObjectAssembler.class, fieldCreator, dataChecker, objectCreator, objectChecker, submitter);
        }
    }

    /**
     *
     *
     */
    interface LoadFromQuery extends Renewable<LoadFromQuery>, Callable<LoadFromQuery.Result> {

        void to(ObjectType objectType) throws Exception;

        void use(EndPoint.DatabasePoint dbEndPoint, String sql, Identity identity);

        void stop();

        /**
         *
         *
         */
        interface Result {

            String sql();

            int rows();

            ObjectType objectType();

            LocalDateTime startTime();

            LocalDateTime endTime();

            long elapsedTime(TimeUnit timeUnit);

            Result result(int rows);

            Result exception(int rows, Exception exception);

            Result killed(int rows);

            boolean wasKilled();

            void throwException() throws Exception;

            default String displayResult() {
                if (this instanceof Result) {
                    Result result = (Result) this;
                    if (result.wasKilled()) {
                        return String.format("%s for '%s' was killed after %d rows in %d seconds",
                                LoadFromQuery.class.getSimpleName(),
                                this.objectType().name(),
                                this.rows(),
                                this.elapsedTime(TimeUnit.SECONDS)
                        );
                    } else {
                        return String.format("%s for '%s' completed after %d rows in %d seconds",
                                LoadFromQuery.class.getSimpleName(),
                                this.objectType().name(),
                                this.rows(),
                                this.elapsedTime(TimeUnit.SECONDS)
                        );

                    }
                }
                return "";
            }
        }

        /**
         *
         * @param context
         * @return
         */
        static public LoadFromQuery create(AppContext context) {
            return LoadProvider.provider().renew(LoadFromQuery.class, context);
        }
    }
}
