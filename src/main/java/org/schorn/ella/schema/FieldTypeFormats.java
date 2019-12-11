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
public class FieldTypeFormats {

    static final Logger LGR = LoggerFactory.getLogger(FieldTypeFormats.class);

    static public class FormatOne implements TypeFormatter<ActiveSchema.FieldType, String> {

        @Override
        public String apply(ActiveSchema.FieldType fieldType) {
            if (fieldType.constraints() == null) {
                return String.format("    %-20s %s", fieldType.name(), fieldType.dataTypeName());
            } else {
                return String.format("    %-20s %s(%s)",
                        fieldType.name(),
                        fieldType.dataTypeName(),
                        fieldType.constraints().toString());
            }
        }

        @Override
        public StringJoiner newJoiner() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    static class Reformat implements TypeFormatter<ActiveSchema.FieldType, String> {

        @Override
        public String apply(ActiveSchema.FieldType fieldType) {
            return String.format(".def(\"%s\", DataType.%s)",
                    fieldType.name(), fieldType.dataTypeName());
        }

        @Override
        public StringJoiner newJoiner() {
            return new StringJoiner("\n\t",
                    String.format("%ss\n\t", ActiveSchema.Roles.FieldType.name()),
                    ";\n\n");
        }
    }

    static class Meta implements TypeFormatter<ActiveSchema.FieldType, Map> {

        @Override
        public Map apply(ActiveSchema.FieldType fieldType) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", fieldType.name());
            map.put("data_type", fieldType.dataTypeName());
            ActiveSchema.Constraints constraints = fieldType.constraints();
            if (constraints != null && constraints.count() > 0) {
                map.put("constraints", constraints.metaMap());
            }
            return map;
        }

        @Override
        public StringJoiner newJoiner() {
            return new StringJoiner("\n", "\n", "");
        }
    }

}
