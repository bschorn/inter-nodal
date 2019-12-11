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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.schorn.ella.schema.ActiveSchema.ObjectType;

/**
 *
 * @author bschorn
 */
public class FragmentFormats {

    static public class FormatOne implements TypeFormatter<ObjectType, String> {

        @Override
        public String apply(ObjectType objectType) {
            StringBuilder builder = new StringBuilder(String.format("    %s", objectType.name() + (objectType.isResolved() ? "" : "*")));
            List<ActiveSchema.Member> membersExt = new ArrayList<>();
            if (!objectType.parents().isEmpty()) {
                String relates = " : ";
                List<ActiveSchema.Parent> objectTypeParents = objectType.parents().stream()
                        .filter(p -> p.parentRole().equals(ActiveSchema.Roles.ObjectType))
                        .collect(Collectors.toList());
                if (!objectTypeParents.isEmpty()) {
                    StringJoiner parentJoiner = new StringJoiner(", ", relates, "");
                    objectTypeParents.forEach(p -> parentJoiner.add(p.parentName()));
                    builder.append(parentJoiner.toString());
                }
                objectType.parents().stream().forEach(parent -> {
                    try {
                        if (parent == null || parent.toString() == null) {
                            System.err.println("parent is null or results in null.");
                        }
                        ObjectType parentType = (ObjectType) objectType.schema().getType(parent.toString());
                        if (parentType == null) {
                            System.err.println(String.format("%s was not found", parent.toString()));
                        }
                        for (ActiveSchema.Member member : parentType.members()) {
                            if (member == null) {
                                System.err.println("Member is null");
                            }
                            if (membersExt == null) {
                                System.err.println("MemberExt is null");
                            }
                        }
                        //parentType.members().stream().forEach(m -> membersExt.add(m));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
            objectType.members().stream().forEach(m -> membersExt.add(m));
            StringJoiner fields = new StringJoiner("\n        ", "\n        ", "\n");
            Set<String> memberNames = new HashSet<>();
            membersExt.stream().forEach(m -> {
                if (!memberNames.contains(m.name())) {
                    fields.add(m.toString());
                    memberNames.add(m.name());
                }
            });
            builder.append(fields.toString());
            return builder.toString();
        }

        @Override
        public StringJoiner newJoiner() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    static class Reformat extends ObjectTypeFormats.Reformat {

        @Override
        public StringJoiner newJoiner() {
            return new StringJoiner("\n\t", String.format("\n%ss\n\t", ActiveSchema.Roles.Fragment.name()), ";");
        }

    }
}
