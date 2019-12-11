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
 * Relationship Attribute of a member NodeType
 *
 * Below are in order of priority (do not change below order willy-nilly)
 *
 *
 * @author bschorn
 */
public enum BondType {
    UNDEFINED(1 << 0, false),
    INSTANCE_KEY(1 << 1, true), // unique for each version of a sequence
    PRIMARY_KEY(1 << 2, true), // surrogate key - random generated non-human unique value
    NATURAL_KEY(1 << 3, true), // natural key human readable can be used for lookup
    FOREIGN_KEY(1 << 4, true), // has to be defined at creation time and is a key in another composite
    IMMUTABLE(1 << 5, true), // has to be defined at creation time and can not be changed
    MUTABLE(1 << 6, true), // has to be defined at creation time but can change later
    OPTIONAL(1 << 7, false), // does not have to be there and change to something later
    DYNAMIC(1 << 8, false), // the field was added at runtime
    INHERIT(1 << 9, false), // TBD
    DEFAULT(1 << 10, true), // if not defined a default will be used
    AUTOMATIC(1 << 11, true), // a value will be provided programaticaly
    SEQUENCE(1 << 12, true),
    NOBOND(1 << 13, false);

    private final boolean required;
    private final long flag;

    BondType(long flag, boolean required) {
        this.flag = flag;
        this.required = required;
    }

    public boolean isRequired() {
        return this.required;
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
    static public EnumSet<BondType> getFlagsForValue(long value) {
        EnumSet bondTypes = EnumSet.noneOf(BondType.class);
        for (BondType bondType : BondType.values()) {
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
    static public long getValueForFlags(Set<BondType> flags) {
        long value = 0;
        for (BondType bondType : BondType.values()) {
            value |= bondType.flag;
        }
        return value;
    }

    static public BondType fromString(String bond_type) {
        if (bond_type != null) {
            for (BondType bondType : BondType.values()) {
                if (bondType.name().compareToIgnoreCase(bond_type) == 0) {
                    return bondType;
                }
            }
        }
        return BondType.NOBOND;
    }

    static public final EnumSet KEYS = EnumSet.of(BondType.PRIMARY_KEY, BondType.NATURAL_KEY);
}
