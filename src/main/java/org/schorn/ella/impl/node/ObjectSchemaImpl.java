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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.schorn.ella.format.SupportString;
import org.schorn.ella.node.ActiveNode.ObjectType.ObjectSchema;
import org.schorn.ella.node.BondType;

/**
 *
 * @author schorn
 *
 */
public class ObjectSchemaImpl implements ObjectSchema {

    final private List<MemberDef> memberTypes;
    final private int bytes;
    final private boolean hasKeys;
    final private List<MemberDef> values;
    final private List<Integer> valPos;
    final private List<MemberDef> keys;
    final private List<Integer> keyPos;
    final private MemberDef specialKey;
    final private List<MemberDef> skeys;
    final private List<Integer> skeyPos;
    final private List<MemberDef> nkeys;
    final private List<Integer> nkeyPos;
    final private List<MemberDef> fkeys;
    final private Map<ActiveType, Integer> mapPos;

    /*
	 * 
     */
    ObjectSchemaImpl(List<MemberDef> memberTypes) {
        this.memberTypes = memberTypes;
        int bytes = 0;
        for (MemberDef memberItem : memberTypes) {
            bytes += memberItem.activeType().bytes();
        }
        this.bytes = bytes;
        this.values = Collections.unmodifiableList(
                this.memberTypes.stream()
                        .filter(mit -> mit.activeType().role() == Role.Value)
                        .collect(Collectors.toList())
        );
        this.valPos = Collections.unmodifiableList(
                this.values.stream().map(mi -> mi.index()).collect(Collectors.toList())
        );
        /*
	 * Unique Key(s) - those that above all else are unique at the expense of readability (BondType.UNIQUE_KEY)
         */
        List<MemberDef> ukeys = Collections.unmodifiableList(
                this.values.stream()
                        .filter(mit -> mit.bondType() == BondType.INSTANCE_KEY)
                        .collect(Collectors.toList())
        );
        if (!ukeys.isEmpty()) {
            this.specialKey = ukeys.get(0);
        } else {
            this.specialKey = null;
        }
        /*
	 * Surrogate Key(s) - those that above all else are unique at the expense of readability (BondType.SURROGATE_KEY + SPECIAL_KEY)
         */
        this.skeys = Collections.unmodifiableList(
                this.values.stream()
                        .filter(mit -> mit.bondType() == BondType.PRIMARY_KEY || mit.bondType() == BondType.INSTANCE_KEY)
                        .collect(Collectors.toList())
        );
        if (this.skeys.size() > 0) {
            this.skeyPos = Collections.unmodifiableList(
                    this.skeys.stream().map(mi -> mi.index()).collect(Collectors.toList())
            );
        } else {
            this.skeyPos = new ArrayList<>();
        }
        /*
	 * Natural Key(s) - those with a human-readability (BondType.KEY)
         */
        this.nkeys = Collections.unmodifiableList(
                this.values.stream()
                        .filter(mit -> mit.bondType() == BondType.NATURAL_KEY)
                        .collect(Collectors.toList())
        );
        if (this.nkeys.size() > 0) {
            this.nkeyPos = Collections.unmodifiableList(
                    this.nkeys.stream().map(mi -> mi.index()).collect(Collectors.toList())
            );
        } else {
            this.nkeyPos = new ArrayList<>();
        }
        if (!this.skeys.isEmpty()) {
            this.keys = this.skeys;
            this.keyPos = this.skeyPos;
        } else if (!this.nkeys.isEmpty()) {
            this.keys = this.nkeys;
            this.keyPos = this.nkeyPos;
        } else {
            this.keys = new ArrayList<>();
            this.keyPos = new ArrayList<>();
        }
        /*
	 * Foreign Key(s)
         */
        this.fkeys = Collections.unmodifiableList(
                this.values.stream()
                        .filter(mit -> mit.bondType() == BondType.FOREIGN_KEY)
                        .collect(Collectors.toList())
        );

        this.hasKeys = !(this.skeys.isEmpty() && !this.nkeys.isEmpty());
        this.mapPos = new HashMap<>();
        this.memberTypes.stream().forEach(mi -> this.mapPos.put(mi.activeType(), mi.index()));
    }

    @Override
    public String name() {
        StringJoiner joiner = new StringJoiner(", ", "[ ", " ]");
        for (MemberDef memberType : this.memberTypes) {
            if (memberType != null) {
                joiner.add(memberType.activeType().name());
            } else {
                joiner.add("null");
            }
        }
        return joiner.toString();
    }

    @Override
    public int bytes() {
        return this.bytes;
    }

    @Override
    public List<MemberDef> memberDefs() {
        return this.memberTypes;
    }

    @Override
    public boolean hasKeys() {
        return this.hasKeys;
    }

    @Override
    public List<MemberDef> valueTypes() {
        return this.values;
    }

    @Override
    public List<Integer> valueIndexes() {
        return this.valPos;
    }

    @Override
    public MemberDef specialKey() {
        return this.specialKey;
    }

    @Override
    public List<MemberDef> uniqueKeys() {
        return this.skeys;
    }

    @Override
    public List<MemberDef> naturalKeys() {
        return this.nkeys;
    }

    @Override
    public List<MemberDef> keys() {
        return this.keys;
    }

    @Override
    public List<Integer> keyIndexes() {
        return this.keyPos;
    }

    @Override
    public List<MemberDef> foreignKeys() {
        return this.fkeys;
    }

    @Override
    public MemberDef get(Integer activeId) {
        Integer index = this.mapPos.get(activeId);
        if (index != null) {
            return this.memberTypes.get(index);
        }
        return null;
    }

    @Override
    public MemberDef get(ActiveType activeType) {
        Integer index = this.mapPos.get(activeType);
        if (index != null) {
            return this.memberTypes.get(index);
        }
        return null;
    }

    @Override
    public Integer getIndex(ActiveType activeType) {
        return this.mapPos.get(activeType);
    }

    @Override
    public String toString() {
        return SupportString.format(this);
    }

}
