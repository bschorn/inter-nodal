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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import org.schorn.ella.node.ActiveNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bschorn
 */
public class ActiveSchemaFormats {

    static final Logger LGR = LoggerFactory.getLogger(ActiveSchemaFormats.class);

    static public class FormatOne implements Formatter<ActiveSchema, String> {

        @Override
        public String apply(ActiveSchema schema) {
            StringBuilder builder = new StringBuilder();
            for (ActiveSchema.Roles role : ActiveSchema.Roles.values()) {
                if (role.display()) {
                    builder.append(toOutput(role));
                }
            }
            return builder.toString();
        }

        private String toOutput(ActiveSchema.Roles role) {
            switch (role) {
                case ValueType:

            }
            /*
            final StringJoiner joiner = role.
            "\n",
                String.format("%ss:\n", role.name())
            ,
                "\n\n");
            return joiner.toString();
             */
            return null;

        }

    }
    static public class Reformat implements Formatter<ActiveSchema, String> {

        @Override
        public String apply(ActiveSchema schema) {
            StringBuilder builder = new StringBuilder();
            for (ActiveSchema.Roles role : ActiveSchema.Roles.values()) {
                if (role.display()) {
                    final StringJoiner joiner = role.reformatter().newJoiner();
                    for (ActiveSchema.BranchType branchType : schema.getBranchTypes(role)) {
                        TypeFormatter<ActiveSchema.BranchType, String> formatter = (TypeFormatter<ActiveSchema.BranchType, String>) role.reformatter();
                        joiner.add(formatter.apply(branchType));
                    }
                    for (ActiveSchema.LeafType leafType : schema.getLeafTypes(role)) {
                        TypeFormatter<ActiveSchema.LeafType, String> formatter = (TypeFormatter<ActiveSchema.LeafType, String>) role.reformatter();
                        joiner.add(formatter.apply(leafType));
                    }
                    builder.append(joiner.toString());
                }
            }
            return builder.toString();
        }

    }

    static public class Meta implements Formatter<ActiveSchema, Map> {

        @Override
        public Map apply(ActiveSchema schema) {
            Map<String, Object> metaMap = new HashMap<>();
            metaMap.put("context", schema.getContext());
            Map<ActiveSchema.Roles, String> metaTags = new HashMap<>();
            metaTags.put(ActiveSchema.Roles.FieldType, ActiveNode.ValueType.FieldType.TYPE_TAG);
            metaTags.put(ActiveSchema.Roles.ValueType, ActiveNode.ValueType.TYPE_TAG);
            for (ActiveSchema.Roles role : new ActiveSchema.Roles[]{ActiveSchema.Roles.FieldType, ActiveSchema.Roles.ValueType}) {
                List<Map> list = new ArrayList<>();
                for (ActiveSchema.LeafType leafType : schema.getLeafTypes(role)) {
                    list.add(leafType.metaMap());
                }
                metaMap.put(metaTags.get(role), list);
            }
            metaTags.put(ActiveSchema.Roles.ObjectType, ActiveNode.ObjectType.TYPE_TAG);
            metaTags.put(ActiveSchema.Roles.ArrayType, ActiveNode.ArrayType.TYPE_TAG);
            for (ActiveSchema.Roles role : new ActiveSchema.Roles[]{ActiveSchema.Roles.ObjectType, ActiveSchema.Roles.ArrayType}) {
                List<Map> list = new ArrayList<>();
                for (ActiveSchema.BranchType branchType : schema.getBranchTypes(role)) {
                    list.add(branchType.metaMap());
                }
                metaMap.put(metaTags.get(role), list);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("meta", metaMap);
            return map;
        }

    }
}
