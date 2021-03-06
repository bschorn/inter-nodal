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
package org.schorn.ella.node;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author schorn
 *
 */
public enum DataGroup {
    TEXT,
    BOOL,
    BYTE,
    DATE,
    TIME,
    TIMESTAMP,
    NUMBER,
    DECIMAL,
    ENUM,
    UUID,
    URL,
    URI,
    ARRAY,
    MAP,
    CUSTOM,;

    boolean isQuoted = false;
    boolean isBinary = false;
    boolean isNumeric = false;
    boolean isIntegral = false;
    boolean isDecimal = false;
    boolean isTemporal = false;
    boolean isEnumerable = false;
    boolean isVector = false;
    boolean isDictionary = false;
    boolean isCustom = false;

    Class<?> acceptables[];
    DataGroup() {
        switch (this.name()) {
            case "TEXT":
                this.isQuoted = true;
                break;
            case "BOOL":
            case "BYTE":
                this.isBinary = true;
                break;
            case "DATE":
            case "TIME":
            case "TIMESTAMP":
                this.isQuoted = true;
                this.isTemporal = true;
                break;
            case "DECIMAL":
                this.isNumeric = true;
                this.isDecimal = true;
                break;
            case "NUMBER":
                this.isNumeric = true;
                this.isIntegral = true;
                break;
            case "ENUM":
                this.isEnumerable = true;
                break;
            case "UUID":
                this.isQuoted = true;
                this.isBinary = true;
                break;
            case "URL":
            case "URI":
                this.isQuoted = true;
                this.isBinary = false;
                break;
            case "ARRAY":
                this.isVector = true;
                this.isBinary = false;
                break;
            case "MAP":
                this.isDictionary = true;
                this.isBinary = false;
                break;
            case "CUSTOM":
                this.isCustom = true;
                break;
            default:
                break;
        }
    }

    public boolean isNumeric() {
        return this.isNumeric;
    }

    public boolean isIntegral() {
        return this.isIntegral;
    }

    public boolean isDecimal() {
        return this.isDecimal;
    }

    /**
     *
     * @return
     */
    public boolean isTemporal() {
        return this.isTemporal;
    }

    public boolean isEnumerable() {
        return this.isEnumerable;
    }

    public boolean isBinary() {
        return this.isBinary;
    }

    public boolean isCustom() {
        return this.isCustom;
    }

    public boolean isQuoted() {
        return this.isQuoted;
    }

    public Object toNaturalType(Object value) throws Exception {
        switch (this) {
            case TEXT:
                return NodeProvider.provider().typeConvert(value.getClass(), String.class, value);
            case BOOL:
                return NodeProvider.provider().typeConvert(value.getClass(), Boolean.class, value);
            case BYTE:
                return NodeProvider.provider().typeConvert(value.getClass(), Byte.class, value);
            case DATE:
                return NodeProvider.provider().typeConvert(value.getClass(), LocalDate.class, value);
            case TIME:
                return NodeProvider.provider().typeConvert(value.getClass(), LocalTime.class, value);
            case TIMESTAMP:
                return NodeProvider.provider().typeConvert(value.getClass(), LocalDateTime.class, value);
            case DECIMAL:
                return NodeProvider.provider().typeConvert(value.getClass(), BigDecimal.class, value);
            case NUMBER:
                return NodeProvider.provider().typeConvert(value.getClass(), Integer.class, value);
            case ENUM:
                return NodeProvider.provider().typeConvert(value.getClass(), String.class, value);
            case UUID:
                return NodeProvider.provider().typeConvert(value.getClass(), UUID.class, value);
            case URL:
                return NodeProvider.provider().typeConvert(value.getClass(), URL.class, value);
            case URI:
                return NodeProvider.provider().typeConvert(value.getClass(), URI.class, value);
            case MAP:
                return NodeProvider.provider().typeConvert(value.getClass(), Map.class, value);
            case CUSTOM:
                return NodeProvider.provider().typeConvert(value.getClass(), String.class, value);
            default:
                return value;
        }
    }

    @Override
    public String toString() {
        return String.format("%s", this.name());
    }

    /**
     * DECIMAL,TIMESTAMP,TEXT
     *
     * @param value
     * @return
     */
    static public DataGroup quickFit(Object value) {
        if (value instanceof Number) {
            return DataGroup.DECIMAL;
        } else if (value instanceof Temporal || value instanceof Date) {
            return DataGroup.TIMESTAMP;
        } else {
            return DataGroup.TEXT;
        }
    }

    static public DataGroup bestFit(Object value) {
        if (value instanceof Number) {
            if (value instanceof BigInteger
                    || value instanceof Long
                    || value instanceof Integer
                    || value instanceof Short
                    || value instanceof Byte) {
                return DataGroup.NUMBER;
            }
            return DataGroup.DECIMAL;
        } else if (value instanceof Temporal || value instanceof Date) {
            if (value instanceof LocalDate) {
                return DataGroup.DATE;
            } else if (value instanceof LocalTime) {
                return DataGroup.TIME;
            } else {
                return DataGroup.TIMESTAMP;
            }
        } else if (value instanceof URL || value instanceof URI) {
            return DataGroup.URL;
        } else {
            return DataGroup.TEXT;
        }
    }

    static public DataGroup parsedFit(Object value) {
        if (value == null) {
            return DataGroup.TEXT;
        }
        DataGroup bestFit = bestFit(value);
        if (bestFit == DataGroup.TEXT) {
            String str_value = value.toString();
            try {
                if (NodeProvider.provider().typeConvert(String.class, LocalTime.class, str_value) != null) {
                    return DataGroup.TIME;
                }
            } catch (Exception ex) {
            }
            try {
                if (NodeProvider.provider().typeConvert(String.class, LocalDate.class, str_value) != null) {
                    return DataGroup.DATE;
                }
            } catch (Exception ex) {
            }
            try {
                if (NodeProvider.provider().typeConvert(String.class, LocalDateTime.class, str_value) != null) {
                    return DataGroup.TIMESTAMP;
                }
            } catch (Exception ex) {
            }
            try {
                Long longValue = NodeProvider.provider().typeConvert(String.class, Long.class, str_value);
                BigDecimal decimalValue = NodeProvider.provider().typeConvert(String.class, BigDecimal.class, str_value);
                if (longValue.doubleValue() != decimalValue.doubleValue()) {
                    return DataGroup.DECIMAL;
                } else if (longValue == decimalValue.longValue()) {
                    return DataGroup.NUMBER;
                }
            } catch (Exception ex) {
            }
            try {
                if (NodeProvider.provider().typeConvert(String.class, Boolean.class, str_value) != null) {
                    return DataGroup.BOOL;
                }
            } catch (Exception ex) {
            }
            return DataGroup.TEXT;
        } else {
            return DataGroup.TEXT;
        }
    }
}
