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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;
import org.schorn.ella.extension.OpenNodeExt;
import org.schorn.ella.node.ActiveNode.Role;

/**
 * Open Node is a simplistic Nodes imitator.
 *
 * Same structure as Nodes (Value,Object,Array) but there are no separate types
 * or contexts to deal with.
 *
 * It's main usage is (but not limited to) the parsing incoming JSON strings.
 * The JSON.ImportReaderToOpenNode will do a JSON -> OpenNode translation. Then
 * there will be a OpenNode->Node translation
 *
 *
 * @author bschorn
 */
public interface OpenNode extends OpenNodeExt {

    Role role();

    String name();

    Object value();

    OpenNode add(Object value);

    OpenNode parent();

    void parent(OpenNode openNode);

    public enum OpenDataType {
        DECIMAL(BigDecimal.class, Double.class, Float.class),
        INTEGER(Long.class, Integer.class, Short.class, Byte.class),
        STRING(String.class, LocalDate.class, LocalTime.class, LocalDateTime.class),
        BOOL(Boolean.class),
        UNKNOWN(Object.class),
        NULL(Object.class),;

        Set<Class<?>> dataClasses;

        OpenDataType(Class<?>... dataClasses) {
            this.dataClasses = new HashSet<>();
            this.dataClasses.addAll(Arrays.asList(dataClasses));
        }

        static public OpenDataType valueOf(Object value) {
            if (value == null) {
                return OpenDataType.NULL;
            }
            for (OpenDataType openDataType : OpenDataType.values()) {
                if (openDataType.dataClasses.contains(value.getClass())) {
                    return openDataType;
                }
            }
            return OpenDataType.UNKNOWN;
        }

        public Object safe_cast(Object value) {
            switch (this) {
                case DECIMAL:
                    if (value instanceof Double) {
                        return BigDecimal.valueOf(((Double) value).doubleValue());
                    } else if (value instanceof Float) {
                        return BigDecimal.valueOf(((Float) value).doubleValue());
                    }
                    break;
                case INTEGER:
                    if (value instanceof Integer) {
                        return Long.valueOf(((Integer) value).longValue());
                    } else if (value instanceof Short) {
                        return Long.valueOf(((Short) value).longValue());
                    } else if (value instanceof Byte) {
                        return Long.valueOf(((Byte) value).longValue());
                    }
                    break;
                case STRING:
                    if (value instanceof LocalDate) {
                        return ((LocalDate) value).toString();
                    } else if (value instanceof LocalTime) {
                        return ((LocalTime) value).toString();
                    } else if (value instanceof LocalDateTime) {
                        return ((LocalDateTime) value).toString();
                    }
                    break;
                case BOOL:
                    if (value instanceof Number) {
                        if (((Number) value).doubleValue() == 0.0) {
                            return Boolean.FALSE;
                        } else {
                            return Boolean.TRUE;
                        }
                    } else if (value instanceof String) {
                        if (((String) value).toUpperCase().startsWith("T") || ((String) value).toUpperCase().startsWith("Y")) {
                            return Boolean.TRUE;
                        } else if (((String) value).toUpperCase().startsWith("F") || ((String) value).toUpperCase().startsWith("N")) {
                            return Boolean.FALSE;
                        }
                    }
                    break;
                case NULL:
                    return "null";
                default:
                    break;
            }
            return value;
        }

    }

    /**
     * OpenValue
     *
     */
    public class OpenValue implements OpenNode {

        String name;
        Object value;
        OpenNode parent;
        OpenDataType type;

        OpenValue(String name) {
            this.name = name;
        }

        @Override
        public Role role() {
            return Role.Value;
        }

        public OpenDataType type() {
            return this.type;
        }

        @Override
        public Object value() {
            return this.type.safe_cast(value);
        }

        @Override
        public String name() {
            return this.name;
        }

        @Override
        public OpenNode add(Object value) {
            this.value = value;
            this.type = OpenDataType.valueOf(this.value);
            return this;
        }

        @Override
        public OpenNode parent() {
            return parent;
        }

        @Override
        public void parent(OpenNode openNode) {
            this.parent = openNode;
        }

        @Override
        public String toString() {
            if (this.parent.role().equals(Role.Array)) {
                return String.format("%s", this.value);
            } else {
                return String.format("%s : %s", this.name, this.value);
            }
        }

    }

    /**
     *
     * Abstract OpenStruct
     *
     */
    static public abstract class OpenStruct implements OpenNode {

        String name;
        List<OpenNode> nodes = new ArrayList<>();
        OpenNode parent;

        OpenStruct(String name) {
            this.name = name;
        }

        public List<OpenNode> nodes() {
            return this.nodes;
        }

        @Override
        public String name() {
            return this.name;
        }

        public boolean changeRoot(String name) {
            if (this.name.equals("root")) {
                this.name = name;
                this.nodes().stream()
                        .filter(on -> !on.role().equals(Role.Value))
                        .map(on -> OpenStruct.class.cast(on))
                        .forEach(on -> on.changeRoot(name));
                return true;
            }
            return false;
        }

        @Override
        public OpenNode add(Object value) {
            if (value instanceof OpenNode) {
                OpenNode openNode = (OpenNode) value;
                openNode.parent(this);
                switch (this.role()) {
                    case Object:
                        for (int i = 0; i < this.nodes.size(); i += 1) {
                            if (this.nodes.get(i).name().equals(openNode.name())) {
                                this.nodes.set(i, openNode);
                                return this;
                            }
                        }
                    case Array:
                        this.nodes.add(openNode);
                        return this;
                    default:
                        return this;
                }
            } else {
                OpenNode.OpenValue openValue = (OpenValue) OpenNode.createValue(this.name);
                openValue.add(value);
                this.add(openValue);
            }
            return this;
        }

        @Override
        public OpenNode parent() {
            return parent;
        }

        @Override
        public void parent(OpenNode openNode) {
            this.parent = openNode;
        }
    }

    /**
     * OpenObject
     *
     */
    static public class OpenObject extends OpenStruct {

        @Override
        public Role role() {
            return Role.Object;
        }

        OpenObject(String name) {
            super(name);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("\n\"%s\": {\n", this.name));
            for (OpenNode openNode : this.nodes()) {
                if (openNode instanceof OpenNodeExt) {
                    openNode.buildToString(builder, 1);
                } else {
                    openNode.toString();
                }
            }
            builder.append(String.format("}\n", this.name));
            return builder.toString();
        }

        @Override
        public Object value() {
            StringJoiner joiner = new StringJoiner("\t", "", "");
            for (OpenNode openNode : this.nodes()) {
                joiner.add(openNode.value().toString());
            }
            return joiner.toString();
        }

    }

    /**
     * OpenArray
     *
     */
    static public class OpenArray extends OpenStruct {

        OpenArray(String name) {
            super(name);
        }

        @Override
        public Role role() {
            return Role.Array;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(String.format("\n\"%s\": [\n", this.name));
            for (OpenNode openNode : this.nodes()) {
                if (openNode instanceof OpenNodeExt) {
                    openNode.buildToString(builder, 1);
                } else {
                    openNode.toString();
                }
            }
            builder.append(String.format("]\n", this.name));
            return builder.toString();
        }

        @Override
        public Object value() {
            StringJoiner joiner = new StringJoiner("\t", "", "");
            for (OpenNode openNode : this.nodes()) {
                joiner.add(openNode.value().toString());
            }
            return joiner.toString();
        }

    }

    /**
     * Factory Method (Value)
     *
     * @param name
     * @return
     */
    static public OpenNode createValue(String name) {
        return (OpenNode) new OpenValue(name);
    }

    /**
     * Factory Method (Object)
     *
     * @param name
     * @return
     */
    static public OpenNode createObject(String name) {
        return (OpenNode) new OpenObject(name);
    }

    /**
     * Factory Method (Array)
     *
     * @param name
     * @return
     */
    static public OpenNode createArray(String name) {
        return (OpenNode) new OpenArray(name);
    }

    /**
     * Node traversal is a recursive utility pattern for traversing through a
     * node calling lambda (Consumer) every time an OpenNode.OpenValue is
     * encountered. The caller will have to provide the implementation for
     * Consumer. Consumer is being used as a callback for the traversal.
     *
     * @param openNode
     * @param consumer
     */
    static public void nodeTraversal(OpenNode openNode, Consumer<OpenNode> consumer) {
        switch (openNode.role()) {
            case Value:
                consumer.accept(openNode);
                break;
            case Object:
                OpenObject oobj = (OpenObject) openNode;
                for (OpenNode onode : oobj.nodes()) {
                    nodeTraversal(onode, consumer);
                }
                break;
            case Array:
                OpenArray oary = (OpenArray) openNode;
                for (OpenNode onode : oary.nodes()) {
                    nodeTraversal(onode, consumer);
                }
                break;
            default:
                break;
        }
    }

    /*
	 * TEST
     */
    static public void main(String[] args) {
        try {
            OpenNode O1A = OpenNode.createObject("Obj01A");
            OpenNode O2A = OpenNode.createObject("Obj02A");
            OpenNode O2B = OpenNode.createObject("Obj02B");
            OpenNode O2C = OpenNode.createObject("Obj02C");
            OpenNode O3A = OpenNode.createObject("Obj03A");
            OpenNode O3B = OpenNode.createObject("Obj03B");
            OpenNode O3C = OpenNode.createObject("Obj03C");

            OpenNode V1A = OpenNode.createValue("Val01A").add("1A");
            OpenNode V2A = OpenNode.createValue("Val02A").add("2A");
            OpenNode V2B = OpenNode.createValue("Val02B").add("2B");
            OpenNode V2C = OpenNode.createValue("Val02C").add("2C");
            OpenNode V3A = OpenNode.createValue("Val03A").add("3A");
            OpenNode V3B = OpenNode.createValue("Val03B").add("3B");
            OpenNode V3C = OpenNode.createValue("Val03C").add("3C");

            O1A.add(V1A);
            O1A.add(O2A).add(O2B).add(O2C);
            O2A.add(V2A).add(V2B).add(V2C);
            System.out.println(O1A.toString());
            System.out.println(O2A.toString());

            //O3A.add(V3A).add(V3B).add(V3C);
            //O2A.add(O3A).add(O3B).add(O3C);
            System.out.println(O1A.toString());

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
