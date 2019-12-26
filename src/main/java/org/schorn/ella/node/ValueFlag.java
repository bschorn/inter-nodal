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

import java.util.EnumSet;
import java.util.Set;

/**
 *
 * Flags for ValueTypes (none, one or some)
 *
 *
 * @author bschorn
 */
public enum ValueFlag {
    UNDEFINED(1),
    AMOUNT(1 << 1),
    APPLICATION(1 << 19),
    ATTRIBUTE(1 << 2),
    AUTOMATIC(1 << 3),
    CATEGORY(1 << 4),
    FACTOR(1 << 5),
    HIDDEN(1 << 18),
    IDENTITY(1 << 6),
    PII(1 << 7),
    PRICE(1 << 8),
    QUANTITY(1 << 9),
    REFERENCE(1 << 10),
    REPO(1 << 11),
    SYSTEM(1 << 12),
    SPATIAL(1 << 13),
    SEQUENCE(1 << 14),
    TEMPORAL(1 << 15),
    TRANSIENT(1 << 16),
    UNIT(1 << 17),
    UNUSED20(1 << 20),
    UNUSED21(1 << 21),
    UNUSED22(1 << 22),
    UNUSED23(1 << 23),
    UNUSED24(1 << 24),
    UNUSED25(1 << 25),
    UNUSED26(1 << 26),
    UNUSED27(1 << 27),
    UNUSED28(1 << 28),
    UNUSED29(1 << 29),
    UNUSED30(1 << 30),
    NOFLAG(1 << 31);

    private final long flag;

    ValueFlag(long flag) {
        this.flag = flag;
    }

    public long flag() {
        return this.flag;
    }

    public boolean hasFlag(long flag) {
        return (this.flag & flag) != 0;
    }

    static public EnumSet<ValueFlag> getEnumSetFromLong(long value) {
        EnumSet valueFlags = EnumSet.noneOf(ValueFlag.class);
        for (ValueFlag valueFlag : ValueFlag.values()) {
            if ((valueFlag.flag & value) == valueFlag.flag) {
                valueFlags.add(value);
            }
        }
        return valueFlags;
    }

    static public EnumSet<ValueFlag> getEnumSetFromSet(Set<ValueFlag> flags) {
        EnumSet enumSet = EnumSet.noneOf(ValueFlag.class);
        for (ValueFlag valueFlag : flags) {
            enumSet.add(valueFlag);
        }
        return enumSet;
    }

    static public long getLongFromSet(Set<ValueFlag> flags) {
        long value = 0;
        for (ValueFlag valueFlag : flags) {
            value |= valueFlag.flag;
        }
        return value;
    }

    static public long getLongFromEnumSet(EnumSet<ValueFlag> flags) {
        long value = 0;
        for (ValueFlag valueFlag : flags) {
            value |= valueFlag.flag;
        }
        return value;
    }

    /**
     *
     * @param value_flag
     * @return
     */
    static public ValueFlag fromString(String value_flag) {
        if (value_flag != null) {
            for (ValueFlag valueFlag : ValueFlag.values()) {
                if (valueFlag.name().compareToIgnoreCase(value_flag) == 0) {
                    return valueFlag;
                }
            }
        }
        return ValueFlag.NOFLAG;
    }

    static public final EnumSet ACTIVITY_FIELDS = EnumSet.of(ValueFlag.QUANTITY, ValueFlag.PRICE, ValueFlag.AMOUNT, ValueFlag.FACTOR);

}
