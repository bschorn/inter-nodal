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
package org.schorn.ella.impl.node;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.schorn.ella.node.ActiveNode.Constraints.ConstraintType;
import org.schorn.ella.node.DataGroup;

/**
 *
 *
 *
 * @author schorn
 *
 * @param <T>
 */
abstract class StandardConstraintType<T> implements ConstraintType<T> {

    private final String name;
    private final DataGroup dataGroup;
    private final Class<T> dataClass;

    StandardConstraintType(String name, DataGroup dataGroup, Class<T> dataClass) {
        this.name = name;
        this.dataGroup = dataGroup;
        this.dataClass = dataClass;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public DataGroup dataGroup() {
        return this.dataGroup;
    }

    @Override
    public Class<T> dataClass() {
        return this.dataClass;
    }

    @Override
    public String toString() {
        return String.format("%s {%s - %s}",
                this.name == null ? "name?" : this.name,
                this.dataGroup == null ? "dataGroup?" : this.dataGroup.name(),
                this.dataClass == null ? "dataClass?" : this.dataClass.getSimpleName()
        );
    }

    static class MinInteger extends StandardConstraintType<Integer> {

        MinInteger() {
            super(ConstraintType.StandardTypes.min_integer.name(), DataGroup.NUMBER, Integer.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return minNumber(constraintValues).test((Number) value);
        }
    }

    static class MaxInteger extends StandardConstraintType<Integer> {

        MaxInteger() {
            super(ConstraintType.StandardTypes.max_integer.name(), DataGroup.NUMBER, Integer.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return maxNumber(constraintValues).test((Number) value);
        }
    }

    static class IncInteger extends StandardConstraintType<Integer> {

        IncInteger() {
            super(ConstraintType.StandardTypes.inc_integer.name(), DataGroup.NUMBER, Integer.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return minNumber(constraintValues).test((Number) value);
        }
    }

    static class MinDecimal extends StandardConstraintType<BigDecimal> {

        MinDecimal() {
            super(ConstraintType.StandardTypes.min_decimal.name(), DataGroup.DECIMAL, BigDecimal.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return minNumber(constraintValues).test((Number) value);
        }
    }

    static class MaxDecimal extends StandardConstraintType<BigDecimal> {

        MaxDecimal() {
            super(ConstraintType.StandardTypes.max_decimal.name(), DataGroup.DECIMAL, BigDecimal.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return maxNumber(constraintValues).test((Number) value);
        }
    }

    static class MinDate extends StandardConstraintType<LocalDate> {

        MinDate() {
            super(ConstraintType.StandardTypes.min_date.name(), DataGroup.DECIMAL, LocalDate.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return minDate(constraintValues).test((LocalDate) value);
        }
    }

    static class MaxDate extends StandardConstraintType<LocalDate> {

        MaxDate() {
            super(ConstraintType.StandardTypes.max_date.name(), DataGroup.DECIMAL, LocalDate.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return maxDate(constraintValues).test((LocalDate) value);
        }
    }

    static class MinDateTime extends StandardConstraintType<LocalDateTime> {

        MinDateTime() {
            super(ConstraintType.StandardTypes.min_datetime.name(), DataGroup.TIMESTAMP, LocalDateTime.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return minDateTime(constraintValues).test((LocalDateTime) value);
        }
    }

    static class MaxDateTime extends StandardConstraintType<LocalDateTime> {

        MaxDateTime() {
            super(ConstraintType.StandardTypes.max_datetime.name(), DataGroup.TIMESTAMP, LocalDateTime.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return maxDateTime(constraintValues).test((LocalDateTime) value);
        }
    }

    static class MinTime extends StandardConstraintType<LocalTime> {

        MinTime() {
            super(ConstraintType.StandardTypes.min_time.name(), DataGroup.TIME, LocalTime.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return minTime(constraintValues).test((LocalTime) value);
        }
    }

    static class MaxTime extends StandardConstraintType<LocalTime> {

        MaxTime() {
            super(ConstraintType.StandardTypes.max_time.name(), DataGroup.TIME, LocalTime.class
            );
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return maxTime(constraintValues).test((LocalTime) value);
        }
    }

    static class Holidays extends StandardConstraintType<LocalDate> {

        Holidays() {
            super(ConstraintType.StandardTypes.holidays.name(), DataGroup.DATE, LocalDate.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return holidays(constraintValues).test((LocalDate) value);
        }
    }

    static class DayOfWeek extends StandardConstraintType<DayOfWeek> {

        DayOfWeek() {
            super(ConstraintType.StandardTypes.day_of_week.name(), DataGroup.DATE, DayOfWeek.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return dayOfWeek(constraintValues).test((LocalDate) value);
        }
    }

    static class StringPattern extends StandardConstraintType<String> {

        StringPattern() {
            super(ConstraintType.StandardTypes.pattern.name(), DataGroup.TEXT, String.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return pattern(constraintValues).test((String) value);
        }
    }

    static class EnumeratedList extends StandardConstraintType<String> {

        EnumeratedList() {
            super(ConstraintType.StandardTypes.list.name(), DataGroup.ENUM, String.class);
        }

        @Override
        public boolean test(List<Object> constraintValues, Object value) {
            return enumeratedList(constraintValues).test((String) value);
        }
    }

    static Predicate<Number> minNumber(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof Number)
                .map(cv -> (Number) cv)
                .filter(cv -> cv.doubleValue() <= pv.doubleValue()).count() > 0;
    }

    static Predicate<Number> maxNumber(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof Number)
                .map(cv -> (Number) cv)
                .filter(cv -> cv.doubleValue() >= pv.doubleValue()).count() > 0;
    }

    static Predicate<LocalDate> minDate(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof LocalDate)
                .map(cv -> (LocalDate) cv)
                .filter(cv -> cv.compareTo(pv) >= 0).count() > 0;
    }

    static Predicate<LocalDate> maxDate(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof LocalDate)
                .map(cv -> (LocalDate) cv)
                .filter(cv -> cv.compareTo(pv) <= 0).count() > 0;
    }

    static Predicate<LocalDateTime> minDateTime(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof LocalDateTime)
                .map(cv -> (LocalDateTime) cv)
                .filter(cv -> cv.compareTo(pv) >= 0).count() > 0;
    }

    static Predicate<LocalDateTime> maxDateTime(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof LocalDateTime)
                .map(cv -> (LocalDateTime) cv)
                .filter(cv -> cv.compareTo(pv) <= 0).count() > 0;
    }

    static Predicate<LocalTime> minTime(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof LocalTime)
                .map(cv -> (LocalTime) cv)
                .filter(cv -> cv.compareTo(pv) >= 0).count() > 0;
    }

    static Predicate<LocalTime> maxTime(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof LocalTime)
                .map(cv -> (LocalTime) cv)
                .filter(cv -> cv.compareTo(pv) <= 0).count() > 0;
    }

    static Predicate<LocalDate> holidays(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof LocalDate)
                .map(cv -> (LocalDate) cv)
                .filter(cv -> cv.compareTo(pv) == 0).count() > 0;
    }

    static Predicate<LocalDate> dayOfWeek(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof DayOfWeek)
                .map(cv -> (DayOfWeek) cv)
                .filter(cv -> pv.getDayOfWeek().equals(cv)).count() > 0;
    }

    static Predicate<String> enumeratedList(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof String)
                .map(cv -> (String) cv)
                .filter(cv -> cv.compareTo(pv) == 0).count() > 0;
    }

    static Predicate<String> pattern(List<Object> constraintValues) {
        return pv -> constraintValues.stream()
                .filter(cv -> cv instanceof Pattern)
                .map(cv -> (Pattern) cv)
                .filter(pattern -> pattern.matcher(pv).matches()).count() > 0;
    }

}
