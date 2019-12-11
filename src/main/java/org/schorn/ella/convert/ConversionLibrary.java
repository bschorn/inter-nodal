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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.function.Function;

/**
 *
 * @author schorn
 *
 */
public class ConversionLibrary {

    static void load(TypeConverter library) {
        // Enum -> String
        {
            @SuppressWarnings("rawtypes")
            Function<Enum, String> function = e -> e.toString();
            library.add(Enum.class, String.class, function, "Enum -> String");
        }
        // String -> LocalDateTime
        {
            Temporals.TemporalImporters.StringToTemporal<LocalDateTime> function
                    = new Temporals.TemporalImporters.StringToTemporal<>(LocalDateTime.class);
            library.add(String.class, LocalDateTime.class, function, function.description);
        }
        // String -> LocalDate
        {
            Temporals.TemporalImporters.StringToTemporal<LocalDate> function
                    = new Temporals.TemporalImporters.StringToTemporal<>(LocalDate.class);
            library.add(String.class, LocalDate.class, function, function.description);
        }
        // String -> LocalTime
        {
            Temporals.TemporalImporters.StringToTemporal<LocalTime> function
                    = new Temporals.TemporalImporters.StringToTemporal<>(LocalTime.class);
            library.add(String.class, LocalTime.class, function, function.description);
        }
        // String -> BigInteger
        {
            Numbers.NumberImporters.StringToNumber<BigInteger> function
                    = new Numbers.NumberImporters.StringToNumber<>(BigInteger.class);
            library.add(String.class, BigInteger.class, function, function.description);
        }
        // String -> BigDecimal
        {
            Numbers.NumberImporters.StringToNumber<BigDecimal> function
                    = new Numbers.NumberImporters.StringToNumber<>(BigDecimal.class);
            library.add(String.class, BigDecimal.class, function, function.description);
        }
        // String -> Double
        {
            Numbers.NumberImporters.StringToNumber<Double> function
                    = new Numbers.NumberImporters.StringToNumber<>(Double.class);
            library.add(String.class, Double.class, function, function.description);
        }
        // String -> Float
        {
            Numbers.NumberImporters.StringToNumber<Float> function
                    = new Numbers.NumberImporters.StringToNumber<>(Float.class);
            library.add(String.class, Float.class, function, function.description);
        }
        // String -> Long
        {
            Numbers.NumberImporters.StringToNumber<Long> function
                    = new Numbers.NumberImporters.StringToNumber<>(Long.class);
            library.add(String.class, Long.class, function, function.description);
        }
        // String -> Integer
        {
            Numbers.NumberImporters.StringToNumber<Integer> function
                    = new Numbers.NumberImporters.StringToNumber<>(Integer.class);
            library.add(String.class, Integer.class, function, function.description);
        }
        // String -> Short
        {
            Numbers.NumberImporters.StringToNumber<Short> function
                    = new Numbers.NumberImporters.StringToNumber<>(Short.class);
            library.add(String.class, Short.class, function, function.description);
        }
        // String -> Boolean
        {
            Function<String, Boolean> function = s -> Boolean.valueOf(Arrays.asList(new char[]{'T', 't', 'Y', 'y', '1', '2', '3', '4', '5', '6', '7', '8', '9'}).stream().filter(c -> c[0] == s.charAt(0)).findAny().isPresent());
            library.add(String.class, Boolean.class, function, "String -> Boolean");
        }
        // String -> DayOfWeek
        {
            Function<String, DayOfWeek> function = s -> DayOfWeek.valueOf(s.toUpperCase());
            library.add(String.class, DayOfWeek.class, function, "String -> DayOfWeek");
        }
        // String -> UUID
        {
            Function<String, UUID> function = s -> s.isEmpty() ? UUID.randomUUID() : UUID.fromString(s.toUpperCase());
            library.add(String.class, UUID.class, function, "String -> UUID");
        }
        // LocalDateTime -> String 
        {
            Temporals.TemporalExporters.TemporalToString<LocalDateTime> function
                    = new Temporals.TemporalExporters.TemporalToString<>(LocalDateTime.class);
            library.add(LocalDateTime.class, String.class, function, function.description);
        }
        // LocalDate -> String
        {
            Temporals.TemporalExporters.TemporalToString<LocalDate> function
                    = new Temporals.TemporalExporters.TemporalToString<>(LocalDate.class);
            library.add(LocalDate.class, String.class, function, function.description);
        }
        // LocalTime -> String
        {
            Temporals.TemporalExporters.TemporalToString<LocalTime> function
                    = new Temporals.TemporalExporters.TemporalToString<>(LocalTime.class);
            library.add(LocalTime.class, String.class, function, function.description);
        }
        // Boolean -> String
        {
            Function<Boolean, String> function = b -> b ? "T" : "F";
            library.add(Boolean.class, String.class, function, "Boolean -> String");
        }
        // BigDecimal -> String
        {
            //Function<BigDecimal, String> function = d -> d.longValue() != 0 && d.divide(BigDecimal.valueOf(d.longValue())) == BigDecimal.ZERO ? String.format("%d", d.longValue()) : d.toString();
            Function<BigDecimal, String> function = d -> d.toPlainString();
            library.add(BigDecimal.class, String.class, function, "BigDecimal -> String");
        }
        // BigDecimal -> BigInteger
        {
            Function<BigDecimal, BigInteger> function = d -> d.toBigInteger();
            library.add(BigDecimal.class, BigInteger.class, function, "BigDecimal -> BigInteger");
        }
        // BigDecimal -> Double
        {
            Function<BigDecimal, Double> function = d -> d.doubleValue();
            library.add(BigDecimal.class, Double.class, function, "BigDecimal -> Double");
        }
        // BigDecimal -> Float
        {
            Function<BigDecimal, Float> function = d -> d.floatValue();
            library.add(BigDecimal.class, Float.class, function, "BigDecimal -> Float");
        }
        // BigDecimal -> Long
        {
            Function<BigDecimal, Long> function = d -> d.longValue();
            library.add(BigDecimal.class, Long.class, function, "BigDecimal -> Long");
        }
        // BigDecimal -> Integer
        {
            Function<BigDecimal, Integer> function = d -> d.intValue();
            library.add(BigDecimal.class, Integer.class, function, "BigDecimal -> Integer");
        }
        // BigDecimal -> Short
        {
            Function<BigDecimal, Short> function = d -> d.shortValue();
            library.add(BigDecimal.class, Short.class, function, "BigDecimal -> Short");
        }
        // BigInteger -> String
        {
            Function<BigInteger, String> function = d -> d.toString();
            library.add(BigInteger.class, String.class, function, "BigInteger -> String");
        }
        // BigInteger -> BigDecimal
        {
            Function<BigInteger, BigDecimal> function = d -> BigDecimal.valueOf(d.doubleValue());
            library.add(BigInteger.class, BigDecimal.class, function, "BigInteger -> BigDecimal");
        }
        // BigInteger -> Double
        {
            Function<BigInteger, Double> function = bi -> bi.doubleValue();
            library.add(BigInteger.class, Double.class, function, "BigInteger -> Double");
        }
        // BigInteger -> Float
        {
            Function<BigInteger, Float> function = bi -> bi.floatValue();
            library.add(BigInteger.class, Float.class, function, "BigInteger -> Float");
        }
        // BigInteger -> Long
        {
            Function<BigInteger, Long> function = bi -> bi.longValue();
            library.add(BigInteger.class, Long.class, function, "BigInteger -> Long");
        }
        // BigInteger -> Integer
        {
            Function<BigInteger, Integer> function = bi -> bi.intValue();
            library.add(BigInteger.class, Integer.class, function, "BigInteger -> Integer");
        }
        // BigInteger -> Short
        {
            Function<BigInteger, Short> function = bi -> bi.shortValue();
            library.add(BigInteger.class, Short.class, function, "BigInteger -> Short");
        }
        // Double -> String
        {
            Function<Double, String> function = d -> d.toString();
            library.add(Double.class, String.class, function, "Double -> String");
        }
        // Double -> BigDecimal
        {
            Function<Double, BigDecimal> function = d -> BigDecimal.valueOf(d.doubleValue());
            library.add(Double.class, BigDecimal.class, function, "Double -> BigDecimal");
        }
        // Double -> BigInteger
        {
            Function<Double, BigInteger> function = d -> BigInteger.valueOf(d.longValue());
            library.add(Double.class, BigInteger.class, function, "Double -> BigInteger");
        }
        // Double -> Float
        {
            Function<Double, Float> function = d -> d.floatValue();
            library.add(Double.class, Float.class, function, "Double -> Float");
        }
        // Double -> Long
        {
            Function<Double, Long> function = d -> d.longValue();
            library.add(Double.class, Long.class, function, "Double -> Long");
        }
        // Double -> Integer
        {
            Function<Double, Integer> function = d -> d.intValue();
            library.add(Double.class, Integer.class, function, "Double -> Integer");
        }
        // Double -> Short
        {
            Function<Double, Short> function = d -> d.shortValue();
            library.add(Double.class, Short.class, function, "Double -> Short");
        }
        // Float -> String
        {
            Function<Float, String> function = f -> f.toString();
            library.add(Float.class, String.class, function, "Float -> String");
        }
        // Float -> BigDecimal
        {
            Function<Float, BigDecimal> function = f -> BigDecimal.valueOf(f.doubleValue());
            library.add(Float.class, BigDecimal.class, function, "Float -> BigDecimal");
        }
        // Float -> BigInteger
        {
            Function<Float, BigInteger> function = f -> BigInteger.valueOf(f.longValue());
            library.add(Float.class, BigInteger.class, function, "Float -> BigInteger");
        }
        // Float -> Double
        {
            Function<Float, Double> function = f -> f.doubleValue();
            library.add(Float.class, Double.class, function, "Float -> Double");
        }
        // Float -> Long
        {
            Function<Float, Long> function = f -> f.longValue();
            library.add(Float.class, Long.class, function, "Float -> Long");
        }
        // Float -> Integer
        {
            Function<Float, Integer> function = f -> f.intValue();
            library.add(Float.class, Integer.class, function, "Float -> Integer");
        }
        // Float -> Short
        {
            Function<Float, Short> function = f -> f.shortValue();
            library.add(Float.class, Short.class, function, "Float -> Short");
        }

        // Long -> String
        {
            Function<Long, String> function = l -> l.toString();
            library.add(Long.class, String.class, function, "Long -> String");
        }
        // Long -> BigDecimal
        {
            Function<Long, BigDecimal> function = l -> BigDecimal.valueOf(l);
            library.add(Long.class, BigDecimal.class, function, "Long -> BigDecimal");
        }
        // Long -> BigInteger
        {
            Function<Long, BigInteger> function = l -> BigInteger.valueOf(l);
            library.add(Long.class, BigInteger.class, function, "Long -> BigInteger");
        }
        // Long -> Integer
        {
            Function<Long, Integer> function = l -> l.intValue();
            library.add(Long.class, Integer.class, function, "Short -> Integer");
        }
        // Long -> Short
        {
            Function<Long, Short> function = l -> l.shortValue();
            library.add(Long.class, Short.class, function, "Long -> Short");
        }
        // Integer -> String
        {
            Function<Integer, String> function = i -> i.toString();
            library.add(Integer.class, String.class, function, "Integer -> String");
        }
        // Integer -> BigDecimal
        {
            Function<Integer, BigDecimal> function = i -> BigDecimal.valueOf(i);
            library.add(Integer.class, BigDecimal.class, function, "Integer -> BigDecimal");
        }
        // Integer -> BigInteger
        {
            Function<Integer, BigInteger> function = i -> BigInteger.valueOf(i);
            library.add(Integer.class, BigInteger.class, function, "Integer -> BigInteger");
        }
        // Integer -> Long
        {
            Function<Integer, Long> function = i -> i.longValue();
            library.add(Integer.class, Long.class, function, "Integer -> Long");
        }
        // Integer -> Short
        {
            Function<Integer, Short> function = i -> i.shortValue();
            library.add(Integer.class, Short.class, function, "Integer -> Short");
        }
        // Short -> String
        {
            Function<Short, String> function = s -> s.toString();
            library.add(Short.class, String.class, function, "Short -> String");
        }
        // Short -> BigDecimal
        {
            Function<Short, BigDecimal> function = s -> BigDecimal.valueOf(s);
            library.add(Short.class, BigDecimal.class, function, "Short -> BigDecimal");
        }
        // Short -> BigInteger
        {
            Function<Short, BigInteger> function = s -> BigInteger.valueOf(s);
            library.add(Short.class, BigInteger.class, function, "Short -> BigInteger");
        }
        // Short -> Long
        {
            Function<Short, Long> function = s -> s.longValue();
            library.add(Short.class, Long.class, function, "Short -> Long");
        }
        // Short -> Integer
        {
            Function<Short, Integer> function = s -> s.intValue();
            library.add(Short.class, Integer.class, function, "Short -> Integer");
        }
        // Number -> String
        {
            Function<Number, String> function = n -> n.toString();
            library.add(Number.class, String.class, function, "Number -> String");
        }
        // Number -> BigDecimal
        {
            Function<Number, BigDecimal> function = n -> BigDecimal.valueOf(n.doubleValue());
            library.add(Number.class, BigDecimal.class, function, "Number -> BigDecimal");
        }
        // Number -> BigInteger
        {
            Function<Number, BigInteger> function = n -> BigInteger.valueOf(n.longValue());
            library.add(Number.class, BigInteger.class, function, "Number -> BigInteger");
        }
        // Number -> Long
        {
            Function<Number, Long> function = n -> n.longValue();
            library.add(Number.class, Long.class, function, "Number -> Long");
        }
        // Number -> Integer
        {
            Function<Number, Integer> function = n -> n.intValue();
            library.add(Number.class, Integer.class, function, "Number -> Integer");
        }
        // Number -> Short
        {
            Function<Number, Short> function = n -> n.shortValue();
            library.add(Number.class, Short.class, function, "Number -> Short");
        }
        // UUID -> String
        {
            Function<UUID, String> function = u -> u.toString();
            library.add(UUID.class, String.class, function, "UUID -> String");
        }
    }
}
