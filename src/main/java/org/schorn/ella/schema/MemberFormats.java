/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
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
package org.schorn.ella.schema;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bschorn
 */
public class MemberFormats {

    static final Logger LGR = LoggerFactory.getLogger(MemberFormats.class);

    static public class FormatOne implements TypeFormatter<ActiveSchema.Member, String> {

        @Override
        public String apply(ActiveSchema.Member member) {
            if (!member.isResolved()) {
                try {
                    member.resolve();
                } catch (Exception ex) {
                    LGR.error(ex.getMessage());
                }
            }
            String indicator = member.isResolved() ? "" : "*";
            if (member.getForeignKeyObjectTypeName() != null
                    && member.getForeignKeyValueTypeName() != null) {
                return String.format("%-20s %s(%s.%s)",
                        member.shortName() + indicator,
                        member.getBondType().name(),
                        member.getForeignKeyObjectTypeName(),
                        member.getForeignKeyValueTypeName());
            } else {
                return String.format("%-20s %s",
                        member.shortName() + indicator,
                        member.getBondType().name());
            }
        }

        @Override
        public StringJoiner newJoiner() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    static class Reformat implements TypeFormatter<ActiveSchema.Member, String> {

        @Override
        public StringJoiner newJoiner() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String apply(ActiveSchema.Member t) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    static class Meta implements TypeFormatter<ActiveSchema.Member, Map> {

        @Override
        public Map apply(ActiveSchema.Member member) {
            Map<String, Object> map = new HashMap<>();
            switch (member.memberRole()) {
                case ValueType:
                    map.put("value_type", member.memberName());
                    break;
                case ObjectType:
                    map.put("object_type", member.memberName());
                    break;
                case ArrayType:
                    map.put("array_type", member.memberName());
                    break;
            }
            map.put("bond_type", member.getBondType().name());
            return map;
        }

        @Override
        public StringJoiner newJoiner() {
            return new StringJoiner("\n", "\n", "");
        }
    }
}
