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
public class ValueTypeFormats {

    static final Logger LGR = LoggerFactory.getLogger(ValueTypeFormats.class);

    static public class FormatOne implements TypeFormatter<ActiveSchema.ValueType, String> {

        @Override
        public String apply(ActiveSchema.ValueType valueType) {
            return String.format("    %-20s %s", valueType.name(), valueType.fieldTypeName());
        }

        @Override
        public StringJoiner newJoiner() {
            return new StringJoiner("\n",
                    String.format("%ss:\n", ActiveSchema.Roles.ValueType.name()),
                    "\n\n");
        }

    }

    static public class Reformat implements TypeFormatter<ActiveSchema.ValueType, String> {

        @Override
        public String apply(ActiveSchema.ValueType valueType) {
            return String.format(".def(\"%s\", FieldType.%s)",
                    valueType.name(), valueType.fieldTypeName());
        }
        @Override
        public StringJoiner newJoiner() {
            return new StringJoiner("\n\t",
                    String.format("%ss\n\t", ActiveSchema.Roles.ValueType.name()),
                    ";\n\n");
        }

    }
    static class Meta implements TypeFormatter<ActiveSchema.ValueType, Map> {

        @Override
        public Map apply(ActiveSchema.ValueType valueType) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", valueType.name());
            map.put("field_type", valueType.fieldTypeName());
            return map;
        }

        @Override
        public StringJoiner newJoiner() {
            return new StringJoiner("\n", "\n", "");
        }
    }
}
