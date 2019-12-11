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
package org.schorn.ella.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.node.OpenNode.OpenArray;
import org.schorn.ella.node.OpenNode.OpenObject;
import org.schorn.ella.node.OpenNode.OpenStruct;
import org.schorn.ella.node.OpenNode.OpenValue;
import org.schorn.ella.parser.ActiveParser;
import org.schorn.ella.util.Functions;

/**
 * Extension to OpenNode
 *
 *
 *
 * @author schorn
 *
 */
public interface OpenNodeExt {

    default void buildToString(StringBuilder builder, int level) {
        if (this instanceof OpenNode) {
            Support.buildToString((OpenNode) this, builder, level);
        }
    }

    default Stream<OpenNode> stream() {
        if (this instanceof OpenNode) {
            return Support.stream((OpenNode) this);
        }
        return null;
    }

    default Stream<OpenValue> deepStream() {
        if (this instanceof OpenNode) {
            return Support.deepStream((OpenNode) this);
        }
        return null;
    }

    default List<OpenNode> find(String name) {
        if (this instanceof OpenNode) {
            return Support.find((OpenNode) this, name);
        }
        return null;
    }

    default OpenNode findFirst(String name) {
        if (this instanceof OpenNode) {
            return Support.findFirst((OpenNode) this, name);
        }
        return null;
    }

    /**
     *
     * @return
     */
    default String asJsonString() {
        if (this instanceof OpenNode) {
            OpenNode openNode = (OpenNode) this;
            try {
                return ActiveParser.WriteJson.get().produceOutput(openNode);
            } catch (Exception ex) {
                return Functions.getStackTraceAsString(ex);
            }
        }
        return null;
    }

    /**
     * Is there a child node with this name?
     *
     * @param name
     * @return
     */
    default boolean isMember(String name) {
        if (this instanceof OpenObject) {
            OpenObject openObject = (OpenObject) this;
            for (OpenNode childNode : openObject.nodes()) {
                if (childNode.name().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * In this instance find a Value member with name.
     *
     * @param name
     * @return
     */
    default OpenValue getValue(String name) {
        if (this instanceof OpenObject) {
            OpenObject openObject = (OpenObject) this;
            for (OpenNode childNode : openObject.nodes()) {
                if (childNode.role().equals(Role.Value) && childNode.name().equals(name)) {
                    return (OpenValue) childNode;
                }
            }
        }
        return null;
    }

    /**
     * In this instance find an Array member with name.
     *
     * @param name
     * @return
     */
    default OpenArray getArray(String name) {
        if (this instanceof OpenObject) {
            OpenObject openObject = (OpenObject) this;
            for (OpenNode childNode : openObject.nodes()) {
                if (childNode.role().equals(Role.Array) && childNode.name().equals(name)) {
                    return (OpenArray) childNode;
                }
            }
        }
        return null;
    }

    /**
     * This will traverse the entire structure replacing the values for all
     * OpenValues with nameVal in OpenObjects with nameObj.This will not add a
 OpenValue to a OpenObject with nameObj if there is no OpenValue with
 nameVal.
     *
     * @param name
     * @param value
     */
    default void setValue(String nameObj, String nameVal, Object value) {
        if (this instanceof OpenNode) {
            OpenNode openNode = (OpenNode) this;
            switch (openNode.role()) {
                case Value:
                    OpenValue openValue = (OpenValue) this;
                    if (openValue.parent().name().equals(nameObj) && openValue.name().equals(nameVal)) {
                        openValue.add(value);
                    }
                    break;
                case Object:
                    OpenObject openObject = (OpenObject) this;
                    for (OpenNode childNode : openObject.nodes()) {
                        childNode.setValue(nameObj, nameVal, value);
                    }
                    break;
                case Array:
                    OpenArray openArray = (OpenArray) this;
                    for (OpenNode childNode : openArray.nodes()) {
                        childNode.setValue(nameObj, nameVal, value);
                    }
                default:
                    break;
            }
        }
    }

    /**
     *
     * Implementation
     *
     */
    static class Support {

        static private final String INDENT = "  ";

        static String getIndent(int level) {
            String indent = "";
            for (int i = 0; i < level; i++) {
                indent += INDENT;
            }
            return indent;
        }

        static void buildToString(OpenNode openNode, StringBuilder builder, int level) {
            if (openNode instanceof OpenValue) {
                OpenValue openValue = (OpenValue) openNode;
                String indent = Support.getIndent(level);
                if (openValue.value() instanceof Number) {
                    Number number = (Number) openValue.value();
                    builder.append(String.format("%s\"%s\": %f\n", indent, openValue.name(), number.doubleValue()));
                } else {
                    builder.append(String.format("%s\"%s\": \"%s\"\n", indent, openValue.name(), openValue.value()));
                }
            } else if (openNode instanceof OpenObject) {
                OpenObject openObject = (OpenObject) openNode;
                String indent = Support.getIndent(level);
                if (openObject.parent().role().equals(Role.Array)) {
                    builder.append(String.format("%s{\n", indent));
                } else {
                    builder.append(String.format("%s\"%s\": {\n", indent, openObject.name()));
                }
                for (OpenNode on : openObject.nodes()) {
                    on.buildToString(builder, level + 1);
                }
                builder.append(String.format("%s}\n", indent));

            } else if (openNode instanceof OpenArray) {
                OpenArray openArray = (OpenArray) openNode;
                String indent = Support.getIndent(level);
                if (openArray.parent().role().equals(Role.Array)) {
                    builder.append(String.format("%s[\n", indent));
                } else {
                    builder.append(String.format("%s\"%s\": [\n", indent, openArray.name()));
                }
                for (OpenNode on : openArray.nodes()) {
                    on.buildToString(builder, level + 1);
                }
                builder.append(String.format("%s]\n", indent));
            }
        }

        static Stream<OpenNode> stream(OpenNode node) {
            if (node instanceof OpenStruct) {
                OpenStruct openStruct = (OpenStruct) node;
                return openStruct.stream();
            } else {
                Stream.Builder<OpenNode> builder = Stream.builder();
                builder.add(node);
                return builder.build();
            }
        }

        /*
		 * 
         */
        static Stream<OpenValue> deepStream(OpenNode node) {
            Stream.Builder<OpenValue> builder = Stream.builder();
            if (node instanceof OpenStruct) {
                Support.deepStream0((OpenStruct) node, builder);
            }
            return builder.build();
        }

        /*
		 * 
         */
        static void deepStream0(OpenStruct openStruct, Stream.Builder<OpenValue> builder) {
            openStruct.nodes().stream().forEach(node -> {
                if (node != null) {
                    switch (node.role()) {
                        case Value:
                            builder.add((OpenValue) node);
                            break;
                        case Object:
                        case Array:
                            deepStream0((OpenStruct) node, builder);
                            break;
                        default:
                            break;
                    }
                }
            });
        }

        /*
		 * 
         */
        static List<OpenNode> find(OpenNode openNode, String name) {
            List<OpenNode> list = new ArrayList<>();
            find(openNode, name, list);
            return list;
        }

        static void find(OpenNode openNode, String name, List<OpenNode> list) {
            if (openNode.name().equalsIgnoreCase(name)) {
                list.add(openNode);
            }
            switch (openNode.role()) {
                case Object:
                case Array:
                    OpenNode.OpenStruct openStruct = (OpenNode.OpenStruct) openNode;
                    for (OpenNode memberNode : openStruct.nodes()) {
                        find(memberNode, name, list);
                    }
                    break;
                default:
                    break;
            }
        }

        static OpenNode findFirst(OpenNode openNode, String name) {
            if (openNode.name().equalsIgnoreCase(name)) {
                return openNode;
            }
            switch (openNode.role()) {
                case Object:
                case Array:
                    OpenNode.OpenStruct openStruct = (OpenNode.OpenStruct) openNode;
                    for (OpenNode memberNode : openStruct.nodes()) {
                        OpenNode findResult = findFirst(memberNode, name);
                        if (findResult != null) {
                            return findResult;
                        }
                    }
                    break;
                default:
                    break;
            }
            return null;
        }

    }

}
