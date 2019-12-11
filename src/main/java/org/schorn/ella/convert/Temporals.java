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
package org.schorn.ella.convert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Temporal => Importer + Implementation
 *
 *
 * @author bschorn
 */
public interface Temporals {

    /**
     *
     */
    static public final class TemporalImportExportFormat {

        Class<? extends Temporal> classOf;
        DateTimeFormatter formatter;
        String pattern;
        String message;

        public TemporalImportExportFormat(Class<? extends Temporal> classOf, String pattern, String message) {
            this.classOf = classOf;
            this.formatter = DateTimeFormatter.ofPattern(pattern);
            this.pattern = pattern;
            this.message = message;
        }

        public TemporalImportExportFormat(Class<? extends Temporal> classOf, DateTimeFormatter formatter, String pattern, String message) {
            this.classOf = classOf;
            this.formatter = formatter;
            this.pattern = pattern;
            this.message = message;
        }

        Class<?> temporalClass() {
            return this.classOf;
        }

        DateTimeFormatter formatter() {
            return this.formatter;
        }

        String pattern() {
            return this.pattern;
        }

        String message() {
            return this.message;
        }
    }

    /**
     *
     */
    static final public class Formatters {

        static final Formatters INSTANCE = new Formatters();

        static public Formatters instance() {
            return INSTANCE;
        }

        List<TemporalImportExportFormat> formatters = new ArrayList<>();
        Map<Class<?>, TemporalImportExportFormat> defaults = new HashMap<>();

        public Formatters() {
            this.formatters.add(new TemporalImportExportFormat(LocalDate.class, DateTimeFormatter.ISO_DATE, "ISO_DATE", "ISO_DATE"));
            add(LocalDate.class, "uuuuMMdd");
            add(LocalDate.class, "uuuu-MM-dd");
            add(LocalDate.class, "uuuu-MM-dd+");
            add(LocalDate.class, "MM/dd/uuuu");
            add(LocalDate.class, "MM-dd-uuuu");
            add(LocalDate.class, "MM.dd.uuuu");
            add(LocalDate.class, "MMM d, uuuu");
            this.formatters.add(new TemporalImportExportFormat(LocalDateTime.class, DateTimeFormatter.ISO_DATE_TIME, "ISO_DATE_TIME", "ISO_DATE_TIME"));
            add(LocalDateTime.class, "uuuu-MM-dd'T'HH:mm:ss.SSSZ");
            add(LocalDateTime.class, "uuuu-MM-dd'T'HH:mm:ss.SSS");
            add(LocalDateTime.class, "uuuu-MM-dd'T'HH:mm:ss");
            add(LocalDateTime.class, "uuuu-MM-dd HH:mm:ss.SSS");
            add(LocalDateTime.class, "uuuu-MM-dd HH:mm:ss");
            add(LocalDateTime.class, "uuuu-MM-dd HH:mm");
            add(LocalDateTime.class, "uuuuMMdd HH:mm:ss.SSSZ");
            add(LocalDateTime.class, "uuuuMMdd HH:mm:ss.SSS");
            add(LocalDateTime.class, "uuuuMMdd HH:mm:ss");
            add(LocalDateTime.class, "uuuuMMdd HH:mm");
            add(LocalDateTime.class, "MMM d, uuuu HH:mm:ss.SSS");
            add(LocalDateTime.class, "MMM d, uuuu HH:mm:ss");
            add(LocalDateTime.class, "MMM d, uuuu HH:mm");
            this.formatters.add(new TemporalImportExportFormat(LocalTime.class, DateTimeFormatter.ISO_TIME, "ISO_TIME", "ISO_TIME"));
            add(LocalTime.class, "HH:mm:ss.SSSZ");
            add(LocalTime.class, "HH:mm:ss.SSS");
            add(LocalTime.class, "HH:mm:ss");
            add(LocalTime.class, "HH:mm");
            setDefault(LocalTime.class, "HH:mm:ss.SSS");
            setDefault(LocalDate.class, "uuuu-MM-dd");
            setDefault(LocalDateTime.class, "uuuu-MM-dd'T'HH:mm:ss.SSS");

        }

        public TemporalImportExportFormat getDefault(Class<? extends Temporal> classOf) {
            return this.defaults.get(classOf);
        }

        public List<TemporalImportExportFormat> get() {
            return formatters;
        }

        public List<TemporalImportExportFormat> get(Class<? extends Temporal> classOf) {
            return formatters.stream().filter(f -> f.temporalClass().equals(classOf)).collect(Collectors.toList());
        }

        public Formatters add(Class<? extends Temporal> classOf, String pattern) {
            this.formatters.add(new TemporalImportExportFormat(classOf, pattern, pattern));
            return this;
        }

        public Formatters add(Class<? extends Temporal> classOf, String pattern, String message) {
            this.formatters.add(new TemporalImportExportFormat(classOf, pattern, message));
            return this;
        }

        public Formatters setDefault(Class<? extends Temporal> classOf, String pattern) {
            this.defaults.put(classOf, new TemporalImportExportFormat(classOf, pattern, pattern));
            return this;
        }
    }

    static public Formatters add(Class<? extends Temporal> classOf, String pattern) {
        return Formatters.instance().add(classOf, pattern);
    }

    /**
     *
     */
    public interface TemporalImporters {

        /**
         * String to Temporal - Importer
         *
         *
         * @param <T>
         */
        static public class StringToTemporal<T extends Temporal> implements Function<String, T> {

            Class<? extends Temporal> targetClass;
            List<TemporalImportExportFormat> formatters;
            public final String description;

            public StringToTemporal(Class<T> targetClass) {
                this(targetClass, new ArrayList<String>());
            }

            public StringToTemporal(Class<T> targetClass, String pattern) {
                this(targetClass, Arrays.asList(new String[]{pattern}));
            }

            public StringToTemporal(Class<T> targetClass, String... patterns) {
                this(targetClass, Arrays.asList(patterns));
            }

            public StringToTemporal(Class<T> targetClass, List<String> patterns) {
                this.targetClass = targetClass;
                if (patterns.isEmpty()) {
                    this.formatters = Formatters.instance().get();
                    this.description = String.format("%s: %s", this.getClass().getSimpleName(), "All Formatters");
                } else {
                    this.formatters = new ArrayList<>();
                    patterns.forEach((pattern) -> {
                        this.formatters.add(new TemporalImportExportFormat(targetClass, pattern, pattern));
                    });
                    StringJoiner joiner = new StringJoiner("\n", "[", "]");
                    patterns.forEach((pattern) -> {
                        joiner.add(pattern);
                    });
                    this.description = String.format("%s: %s", this.getClass().getSimpleName(), joiner.toString());
                }
            }

            @SuppressWarnings("unchecked")
            @Override
            public T apply(String value) {
                if (value.trim().length() == 0) {
                    return (T) null;
                }
                return (T) TemporalParser.parse(this.targetClass, value, this.formatters);
            }

        }

        /**
         * String to Temporal - Implementation
         *
         * Return a Temporal if success Return null on failed (instead of
         * throwing DateTimeParseException)
         */
        static public final class TemporalParser {

            static final Logger LGR = LoggerFactory.getLogger(TemporalParser.class);

            /* utility methods to catch Exception and return null instead */
            public static LocalTime parseToTime(String value, DateTimeFormatter formatter) {
                try {
                    return LocalTime.parse(value, formatter);
                } catch (Exception ex) {
                    //LGR.warn("Exception parsing '{}' into '{}': {}", value,
                    //LocalTime.class.getSimpleName(), ex.getMessage());
                }
                return null;
            }

            public static LocalDate parseToDate(String value, DateTimeFormatter formatter) {
                try {
                    return LocalDate.parse(value, formatter);
                } catch (Exception ex) {
                    //LGR.warn("Exception parsing '{}' into '{}': {}", value,
                    //LocalDate.class.getSimpleName(), ex.getMessage());
                }
                return null;
            }

            public static LocalDateTime parseToDateTime(String value, DateTimeFormatter formatter) {
                try {
                    return LocalDateTime.parse(value, formatter);
                } catch (Exception ex) {
                    //LGR.warn("Exception parsing '{}' into '{}': {}", value,
                    //LocalDateTime.class.getSimpleName(), ex.getMessage());
                }
                return null;
            }

            /**
             * Usage: when the incoming value differs from desired
             *
             * To parse the value into a LocalTime -> "2018-10-01T09:34:00" =>
             * LocalTime("09:34") LocalTime localTime =
             * TemporalParse(LocalTime.class, value);
             *
             * To parse the value into a LocalDate: "2018-10-01T12:00:00" =>
             * LocalDate("2018-10-01") LocalDate localDate =
             * TemporalParse(LocalDate.class, value);
             *
             * To parse the value into a LocalDateTime: "2018-10-01" =>
             * LocalDateTime("2018-10-01T00:00:00") LocalDateTime localDate =
             * TemporalParse(LocalDateTime.class, value);
             *
             *
             * @param classOfT
             * @param value
             * @param formatters
             * @return
             */
            static public Temporal parse(Class<?> classOfT, String value, List<TemporalImportExportFormat> formatters) {
                try {
                    Class<?>[] classOrder = new Class<?>[]{null, null, null};
                    if (classOfT.equals(LocalDate.class)) {
                        classOrder[0] = LocalDate.class;
                        classOrder[1] = LocalDateTime.class;
                    } else if (classOfT.equals(LocalDateTime.class)) {
                        classOrder[0] = LocalDateTime.class;
                        classOrder[1] = LocalDate.class;
                        classOrder[2] = LocalTime.class;
                    } else if (classOfT.equals(LocalTime.class)) {
                        classOrder[0] = LocalTime.class;
                        classOrder[1] = LocalDateTime.class;
                    }
                    for (Class<?> classOf : classOrder) {
                        if (classOf == null) {
                            break;
                        }
                        if (classOf.equals(LocalTime.class)) {
                            for (TemporalImportExportFormat formatter : formatters) {
                                LocalTime time = parseToTime(value, formatter.formatter());
                                if (time != null) {
                                    if (classOf.equals(classOfT)) {
                                        return time;
                                    } else if (LocalDateTime.class.equals(classOfT)) {
                                        LocalDate date = LocalDate.now();
                                        return LocalDateTime.of(
                                                date.getYear(), date.getMonth(),
                                                date.getDayOfMonth(), time.getHour(),
                                                time.getMinute(), time.getSecond(),
                                                time.getNano());
                                    }
                                }
                            }
                        } else if (classOf.equals(LocalDate.class)) {
                            for (TemporalImportExportFormat formatter : formatters) {
                                LocalDate date = parseToDate(value, formatter.formatter());
                                if (date != null) {
                                    if (classOf.equals(classOfT)) {
                                        return date;
                                    } else if (LocalDateTime.class.equals(classOfT)) {
                                        return LocalDateTime.of(
                                                date.getYear(), date.getMonth(),
                                                date.getDayOfMonth(), 0,
                                                0);
                                    }
                                }
                            }
                        } else if (classOf.equals(LocalDateTime.class)) {
                            for (TemporalImportExportFormat formatter : formatters) {
                                LocalDateTime dateTime = parseToDateTime(value, formatter.formatter());
                                if (dateTime != null) {
                                    if (classOf.equals(classOfT)) {
                                        return dateTime;
                                    } else if (LocalTime.class.equals(classOfT)) {
                                        return LocalTime.of(dateTime.getHour(),
                                                dateTime.getMinute(), dateTime.getSecond(),
                                                dateTime.getNano());
                                    } else if (LocalDate.class.equals(classOfT)) {
                                        return LocalDate.of(dateTime.getYear(),
                                                dateTime.getMonth(), dateTime.getDayOfMonth());
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    LGR.warn("Exception parsing '{}' into '{}': {}", value,
                            LocalTime.class.getSimpleName(), ex.getMessage());
                }
                return null;
            }

        }

    }

    public interface TemporalExporters {

        /**
         * String to Temporal - Importer
         *
         *
         * @param <T>
         */
        static public class TemporalToString<T extends Temporal> implements Function<T, String> {

            Class<? extends Temporal> targetClass;
            TemporalImportExportFormat formatter;
            public final String description;

            public TemporalToString(Class<T> targetClass) {
                this(targetClass, null);
            }

            public TemporalToString(Class<T> targetClass, String pattern) {
                this.targetClass = targetClass;
                if (pattern == null) {
                    this.formatter = Formatters.instance().getDefault(targetClass);
                } else {
                    this.formatter = new TemporalImportExportFormat(targetClass, pattern, pattern);
                }
                this.description = String.format("%s: %s", this.getClass().getSimpleName(), pattern);
            }

            @Override
            public String apply(T value) {
                return TemporalFormatter.format(this.targetClass, value, this.formatter);
            }

        }

        /*
        static public class TemporalToJson<T extends Temporal> implements Function<T, JsonString> {

            Class<? extends Temporal> targetClass;
            TemporalImportExportFormat formatter;
            public final String description;

            public TemporalToJson(Class<T> targetClass) {
                this(targetClass, null);
            }

            public TemporalToJson(Class<T> targetClass, String pattern) {
                this.targetClass = targetClass;
                if (pattern == null) {
                    this.formatter = Formatters.instance().getDefault(targetClass);
                } else {
                    this.formatter = new TemporalImportExportFormat(targetClass, pattern, pattern);
                }
                this.description = String.format("%s: %s", this.getClass().getSimpleName(), pattern);
            }

            @Override
            public JsonString apply(T value) {
                return TemporalFormatter.json(this.targetClass, value);
            }

        }
         */
        /**
         * String to Temporal - Implementation
         *
         * Return a Temporal if success Return null on failed (instead of
         * throwing DateTimeParseException)
         */
        static public final class TemporalFormatter {

            static final Logger LGR = LoggerFactory.getLogger(TemporalFormatter.class);

            /* utility methods to catch Exception and return null instead */
            public static String timeToString(LocalTime value, DateTimeFormatter formatter) {
                return formatter.format(value);
            }

            public static String dateToString(LocalDate value, DateTimeFormatter formatter) {
                return formatter.format(value);
            }

            public static String dateTimeToString(LocalDateTime value, DateTimeFormatter formatter) {
                return formatter.format(value);
            }

            public static Number timeToNumber(LocalTime value) {
                return Integer.valueOf(DateTimeFormatter.ofPattern("HHmm").format(value));
            }

            /*
            public static JsonString timeToJsonString(LocalTime value) {
                return JsonProvider.provider().createValue(DateTimeFormatter.ISO_LOCAL_TIME.format(value));
            }

            public static JsonString dateToJsonString(LocalDate value) {
                return JsonProvider.provider().createValue(DateTimeFormatter.ISO_LOCAL_DATE.format(value));
            }

            public static JsonString dateTimeToJsonString(LocalDateTime value) {
                return JsonProvider.provider().createValue(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value));
            }
             */
            /**
             * @param <T>
             * @param classOfT
             * @param temporal
             * @param formatter
             * @return
             */
            public static <T> String format(Class<?> classOfT, T temporal, TemporalImportExportFormat formatter) {
                try {
                    if (classOfT.equals(LocalTime.class)) {
                        return timeToString((LocalTime) temporal, formatter.formatter());
                    } else if (classOfT.equals(LocalDate.class)) {
                        return dateToString((LocalDate) temporal, formatter.formatter());
                    } else if (classOfT.equals(LocalDateTime.class)) {
                        return dateTimeToString((LocalDateTime) temporal, formatter.formatter());
                    }
                } catch (Exception ex) {
                    if (ex.getMessage() == null) {
                        LGR.error(Functions.getStackTraceAsString(ex));
                    } else {
                        LGR.warn("Exception formatting '{}' into '{}': {}", temporal,
                                String.class.getSimpleName(), ex.getMessage());
                    }
                }
                return null;
            }

            /**
             * @param <T>
             * @param classOfT
             * @param temporal
             * @param formatter
             * @return
             */
            /*
            public static <T> JsonString json(Class<?> classOfT, T temporal) {
                try {
                    if (classOfT.equals(LocalTime.class)) {
                        return timeToJsonString((LocalTime) temporal);
                    } else if (classOfT.equals(LocalDate.class)) {
                        return dateToJsonString((LocalDate) temporal);
                    } else if (classOfT.equals(LocalDateTime.class)) {
                        return dateTimeToJsonString((LocalDateTime) temporal);
                    }
                } catch (Exception ex) {
                    if (ex.getMessage() == null) {
                        LGR.error(Functions.getStackTraceAsString(ex));
                    } else {
                        LGR.warn("Exception formatting '{}' into '{}': {}", temporal,
                                String.class.getSimpleName(), ex.getMessage());
                    }
                }
                return null;
            }
             */
        }

    }
}
