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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Temporal => Importer + Implementation
 *
 *
 * @author bschorn
 */
public interface Numbers {

    public interface NumberImporters {

        static final public class DecimalFormatters implements Iterable<DecimalFormat> {

            static final DecimalFormatters INSTANCE = new DecimalFormatters();

            static public DecimalFormatters instance() {
                return INSTANCE;
            }
            List<DecimalFormat> formatters = new ArrayList<>();

            DecimalFormatters() {
                formatters.add(new DecimalFormat("", new DecimalFormatSymbols(Locale.ENGLISH)));
            }

            public List<DecimalFormat> get() {
                return formatters;
            }

            public DecimalFormatters add(DecimalFormat formatter) {
                this.formatters.add(formatter);
                return this;
            }

            @Override
            public Iterator<DecimalFormat> iterator() {
                return this.formatters.iterator();
            }
        }

        static public DecimalFormatters add(DecimalFormat formatter) {
            return DecimalFormatters.instance().add(formatter);
        }

        /**
         * String to Temporal - Importer
         *
         *
         * @param <T>
         */
        static public class StringToNumber<T extends Number> implements Function<String, T> {

            Class<? extends Number> targetClass;
            List<DecimalFormat> formatters;
            public final String description;

            public StringToNumber(Class<T> targetClass) {
                this(targetClass, DecimalFormatters.instance().get());
            }

            public StringToNumber(Class<T> targetClass, DecimalFormat decimalFormat) {
                this(targetClass, Arrays.asList(new DecimalFormat[]{decimalFormat}));
            }

            public StringToNumber(Class<T> targetClass, DecimalFormat... decimalFormatters) {
                this(targetClass, Arrays.asList(decimalFormatters));
            }

            public StringToNumber(Class<T> targetClass, List<DecimalFormat> decimalFormatters) {
                this.targetClass = targetClass;
                this.formatters = decimalFormatters;
                StringJoiner joiner = new StringJoiner("\n", "", "");
                this.formatters.forEach((formatter) -> {
                    joiner.add(formatter.toString());
                });
                this.description = String.format("%s: [\n%s\n]", this.getClass().getSimpleName(), joiner.toString());
            }

            @SuppressWarnings("unchecked")
            @Override
            public T apply(String value) {
                return (T) NumberParser.parse(this.targetClass, value, this.formatters);
            }

        }

        /**
         * String to Temporal - Implementation
         *
         * Return a Temporal if success Return null on failed (instead of
         * throwing DateTimeParseException)
         */
        static final class NumberParser {

            static final Logger LGR = LoggerFactory.getLogger(NumberParser.class);

            /* utility methods to catch Exception and return null instead */

            /**
             *
             * @param value
             * @param formatter
             * @return
             */

            public static Long parseToLong(String value, DecimalFormat formatter) {
                try {
                    return formatter.parse(value).longValue();
                } catch (Exception ex) {
                }
                return null;
            }

            public static Integer parseToInteger(String value, DecimalFormat formatter) {
                try {
                    return formatter.parse(value).intValue();
                } catch (Exception ex) {
                }
                return null;
            }

            public static Short parseToShort(String value, DecimalFormat formatter) {
                try {
                    return formatter.parse(value).shortValue();
                } catch (Exception ex) {
                }
                return null;
            }

            public static Double parseToDouble(String value, DecimalFormat formatter) {
                try {
                    return formatter.parse(value).doubleValue();
                } catch (Exception ex) {
                    return null;
                }
            }

            public static Float parseToFloat(String value, DecimalFormat formatter) {
                try {
                    return formatter.parse(value).floatValue();
                } catch (Exception ex) {
                    return null;
                }
            }

            public static BigInteger parseToBigInteger(String value, DecimalFormat formatter) {
                try {
                    return BigInteger.valueOf(formatter.parse(value).longValue());
                } catch (Exception ex) {
                    return null;
                }
            }

            public static BigDecimal parseToBigDecimal(String value, DecimalFormat formatter) {
                try {
                    return BigDecimal.valueOf(formatter.parse(value).doubleValue());
                } catch (Exception ex) {
                    return null;
                }
            }

            /**
             * @param classOfT
             * @param value
             * @param decimalFormatters
             * @return
             */
            public static Number parse(Class<?> classOfT, String value, List<DecimalFormat> decimalFormatters) {
                try {
                    if (classOfT.equals(Long.class)) {
                        for (DecimalFormat formatter : decimalFormatters) {
                            Long number = parseToLong(value, formatter);
                            if (number != null) {
                                return number;
                            }
                        }
                    } else if (classOfT.equals(Integer.class)) {
                        for (DecimalFormat formatter : decimalFormatters) {
                            Integer number = parseToInteger(value, formatter);
                            if (number != null) {
                                return number;
                            }
                        }
                    } else if (classOfT.equals(Short.class)) {
                        for (DecimalFormat formatter : decimalFormatters) {
                            Short number = parseToShort(value, formatter);
                            if (number != null) {
                                return number;
                            }
                        }
                    } else if (classOfT.equals(Double.class)) {
                        for (DecimalFormat formatter : decimalFormatters) {
                            Double number = parseToDouble(value, formatter);
                            if (number != null) {
                                return number;
                            }
                        }
                    } else if (classOfT.equals(BigInteger.class)) {
                        for (DecimalFormat formatter : decimalFormatters) {
                            BigInteger number = parseToBigInteger(value, formatter);
                            if (number != null) {
                                return number;
                            }
                        }
                    } else if (classOfT.equals(BigDecimal.class)) {
                        for (DecimalFormat formatter : decimalFormatters) {
                            BigDecimal number = parseToBigDecimal(value, formatter);
                            if (number != null) {
                                return number;
                            }
                        }
                    }
                } catch (Exception ex) {
                    LGR.warn("Exception parsing '{}' into '{}': {}", value,
                            classOfT.getSimpleName(), ex.getMessage());
                }
                return null;
            }

        }

        /**
         * String to Temporal - Importer
         *
         *
         * @param <T>
         */
        static class NumberToNumber implements Function<Number, Number> {

            Class<? extends Number> targetClass;
            public final String description;

            <T extends Number> NumberToNumber(Class<? extends Number> targetClass) {
                this.targetClass = targetClass;
                this.description = String.format("%s: Number to %s",
                        this.getClass().getSimpleName(),
                        this.targetClass.getSimpleName());
            }

            @Override
            public Number apply(Number value) {
                if (targetClass.equals(Integer.class)) {
                    return value.intValue();
                } else if (targetClass.equals(Short.class)) {
                    return value.shortValue();
                } else if (targetClass.equals(Long.class)) {
                    return value.longValue();
                } else if (targetClass.equals(Float.class)) {
                    return value.floatValue();
                } else if (targetClass.equals(Double.class)) {
                    return value.doubleValue();
                } else if (targetClass.equals(BigDecimal.class)) {
                    return BigDecimal.valueOf(value.doubleValue());
                }
                return value;
            }

        }

    }

    public interface NumberExporters {

        static final public DecimalFormat DEFAULT = new DecimalFormat("", new DecimalFormatSymbols(Locale.ENGLISH));

        /**
         * @param <T>
         */
        static class NumberToString<T extends Number> implements Function<T, String> {

            Class<? extends Number> targetClass;
            DecimalFormat formatter;
            public final String description;

            NumberToString(Class<T> targetClass) {
                this(targetClass, DEFAULT);
            }

            NumberToString(Class<T> targetClass, DecimalFormat decimalFormat) {
                this.targetClass = targetClass;
                this.formatter = decimalFormat;
                this.description = String.format("%s: [ %s ]", this.getClass().getSimpleName(), decimalFormat.toPattern());
            }

            @Override
            public String apply(T value) {
                return NumberFormatter.format(this.targetClass, value, this.formatter);
            }

        }

        /*
        static class NumberToJson<T extends Number> implements Function<T, JsonNumber> {

            Class<? extends Number> targetClass;
            DecimalFormat formatter;
            public final String description;

            NumberToJson(Class<T> targetClass) {
                this(targetClass, DEFAULT);
            }

            NumberToJson(Class<T> targetClass, DecimalFormat decimalFormat) {
                this.targetClass = targetClass;
                this.formatter = decimalFormat;
                this.description = String.format("%s: [ %s ]", this.getClass().getSimpleName(), decimalFormat.toPattern());
            }

            @Override
            public JsonNumber apply(T value) {
                return NumberFormatter.json(this.targetClass, value);
            }

        }
         */
        /**
         * String to Temporal - Implementation
         *
         * Return a Temporal if success Return null on failed (instead of
         * throwing DateTimeParseException)
         */
        static final class NumberFormatter {

            static final Logger LGR = LoggerFactory.getLogger(NumberFormatter.class);

            /* utility methods to catch Exception and return null instead */
            public static String longToString(Long value, DecimalFormat formatter) {
                try {
                    return formatter.format(value);
                } catch (Exception ex) {
                    return null;
                }
            }

            public static String doubleToString(Double value, DecimalFormat formatter) {
                try {
                    return formatter.format(value);
                } catch (Exception ex) {
                    return null;
                }
            }

            public static String decimalToString(BigDecimal value, DecimalFormat formatter) {
                try {
                    return formatter.format(value);
                } catch (Exception ex) {
                    return null;
                }
            }

            /**
             * @param <T>
             * @param classOfT
             * @param number
             * @param formatter
             * @return
             */
            public static <T> String format(Class<?> classOfT, T number, DecimalFormat formatter) {
                try {
                    if (classOfT.equals(Long.class)) {
                        return longToString((Long) number, formatter);
                    } else if (classOfT.equals(Double.class)) {
                        return doubleToString((Double) number, formatter);
                    } else if (classOfT.equals(BigDecimal.class)) {
                        return decimalToString((BigDecimal) number, formatter);
                    }
                } catch (Exception ex) {
                    LGR.warn("Exception parsing '{}' into '{}': {}", number,
                            classOfT.getSimpleName(), ex.getMessage());
                }
                return null;
            }

            /*
            public static JsonNumber longToJson(Long value) {
                try {
                    return JsonProvider.provider().createValue(value);
                } catch (Exception ex) {
                    return null;
                }
            }

            public static JsonNumber doubleToJson(Double value) {
                try {
                    return JsonProvider.provider().createValue(value);
                } catch (Exception ex) {
                    return null;
                }
            }

            public static JsonNumber doubleToJson(BigDecimal value) {
                try {
                    return JsonProvider.provider().createValue(value);
                } catch (Exception ex) {
                    return null;
                }
            }

            public static <T> JsonNumber json(Class<?> classOfT, T number) {
                try {
                    if (classOfT.equals(Long.class)) {
                        return longToJson((Long) number);
                    } else if (classOfT.equals(Double.class)) {
                        return doubleToJson((Double) number);
                    } else if (classOfT.equals(BigDecimal.class)) {
                        return doubleToJson((BigDecimal) number);
                    }
                } catch (Exception ex) {
                    LGR.warn("Exception parsing '{}' into '{}': {}", number,
                            classOfT.getSimpleName(), ex.getMessage());
                }
                return null;
            }
             */
        }
    }
}
