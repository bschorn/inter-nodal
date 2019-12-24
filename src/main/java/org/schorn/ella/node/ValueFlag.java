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
    ATTRIBUTE(1 << 2),
    AUTOMATIC(1 << 3),
    CATEGORY(1 << 4),
    FACTOR(1 << 5),
    IDENTITY(1 << 6),
    PII(1 << 7),
    PRICE(1 << 8),
    QUANTITY(1 << 9),
    REFERENCE(1 << 10),
    SYSTEM(1 << 11),
    SPATIAL(1 << 12),
    SEQUENCE(1 << 13),
    TEMPORAL(1 << 14),
    TRANSIENT(1 << 15),
    UNIT(1 << 16),
    UNUSED17(1 << 17),
    UNUSED18(1 << 18),
    UNUSED19(1 << 19),
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

    /**
     * Convert a single value into a set of flags.
     *
     * @param value
     * @return
     */
    static public EnumSet<ValueFlag> getFlagsForValue(long value) {
        EnumSet bondTypes = EnumSet.noneOf(ValueFlag.class);
        for (ValueFlag bondType : ValueFlag.values()) {
            if ((bondType.flag & value) == bondType.flag) {
                bondTypes.add(value);
            }
        }
        return bondTypes;
    }

    /**
     * Convert a set of flags into a single value.
     *
     * @param flags
     * @return
     */
    static public long getValueForFlags(Set<ValueFlag> flags) {
        long value = 0;
        for (ValueFlag bondType : ValueFlag.values()) {
            value |= bondType.flag;
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
