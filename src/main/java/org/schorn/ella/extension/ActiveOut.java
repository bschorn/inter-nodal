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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ValueData;

/**
 * NodeOut
 *
 * Formatting for screen/file or Object(s)
 *
 * Implementor implements NodeOut.OutFormat and passes an instance of the
 * implementation to the Node.nodeOut(OutFormat)
 *
 * Example Windows CSV (tabular) > ArrayData data = getSomeData(); >
 * data.nodeOut(NodeOut.OutFormats.WINCSV).toString();
 *
 * @author schorn
 *
 */
public interface ActiveOut {

    static final Logger LGR = LoggerFactory.getLogger(ActiveOut.class);

    public interface OutFormat {

        Object valueOut(ValueData activeValue);

        Object objectOut(ObjectData nodeObject);

        Object arrayOut(ArrayData nodeArray);
        static public final OutFormat CSV = OutFormats.LINCSV;
    }

    default Object activeOut(OutFormat format) {
        if (this instanceof ActiveData) {
            return Impl.activeOut((ActiveData) this, format);
        }
        return this;
    }

    static class Impl {

        static Object activeOut(ActiveData node, OutFormat out) {
            if (node instanceof ValueData) {
                return out.valueOut((ValueData) node);
            } else if (node instanceof ObjectData) {
                return out.objectOut((ObjectData) node);
            } else if (node instanceof ArrayData) {
                return out.arrayOut((ArrayData) node);
            }
            return node;
        }
    }

    public enum OutFormats implements OutFormat {
        WINCSV(DateTimeFormatter.ISO_DATE, DateTimeFormatter.ISO_TIME, DateTimeFormatter.ISO_DATE_TIME, ",", "\n"),
        LINCSV(DateTimeFormatter.ISO_DATE, DateTimeFormatter.ISO_TIME, DateTimeFormatter.ISO_DATE_TIME, ",", "\n"),;

        String objDelim;
        String aryDelim;
        String objDelimESC;
        String aryDelimESC;
        DateTimeFormatter dateFmt;
        DateTimeFormatter timeFmt;
        DateTimeFormatter timestampFmt;

        OutFormats(DateTimeFormatter dateFmt, DateTimeFormatter timeFmt, DateTimeFormatter timestampFmt, String objDelim, String aryDelim) {
            this.dateFmt = dateFmt;
            this.timeFmt = timeFmt;
            this.timestampFmt = timestampFmt;
            this.objDelim = objDelim;
            this.aryDelim = aryDelim;
            this.objDelimESC = String.format("%s%s", "\\", this.objDelim);
            this.aryDelimESC = String.format("%s%s", "\\", this.aryDelim);
        }

        @Override
        public Object valueOut(ValueData activeValue) {
            try {
                if (activeValue.isNull() || activeValue.isBlank()) {
                    return "";
                }
                switch (activeValue.valueType().fieldType().dataType().primitiveType().dataGroup()) {
                    case BOOL:
                        if ((boolean) activeValue.as(Boolean.class)) {
                            return "true";
                        } else {
                            return "false";
                        }
                    case NUMBER:
                    case DECIMAL:
                        return activeValue.activeValue().toString();
                    case DATE:
                        if (this == WINCSV) {
                            return String.format("\"%s\"", this.dateFmt.format(activeValue.as(LocalDate.class)));
                        } else if (this == LINCSV) {
                            return this.dateFmt.format(activeValue.as(LocalDate.class));
                        }
                    case TIME:
                        if (this == WINCSV) {
                            return String.format("\"%s\"", this.timeFmt.format(activeValue.as(LocalTime.class)));
                        } else if (this == LINCSV) {
                            return this.timeFmt.format(activeValue.as(LocalTime.class));
                        }
                    case TIMESTAMP:
                        if (this == WINCSV) {
                            return String.format("\"%s\"", this.timestampFmt.format(activeValue.as(LocalDateTime.class)));
                        } else if (this == LINCSV) {
                            return this.timestampFmt.format(activeValue.as(LocalDateTime.class));
                        }
                    case ENUM:
                    case CUSTOM:
                    case TEXT:
                    default:
                        if (this == WINCSV) {
                            return String.format("\"%s\"", activeValue.as(String.class));
                        } else if (this == LINCSV) {
                            return activeValue.as(String.class).replaceAll(this.objDelim, this.objDelimESC).replaceAll(this.aryDelim, this.aryDelimESC);
                        } else {
                            return activeValue.as(String.class);
                        }
                }
            } catch (Exception ex) {
                LGR.error("{}.valueOut - {} [exception] {}",
                        this.getClass().getSimpleName(),
                        activeValue.activeValue().toString(),
                        ex.getMessage());
            }
            if (activeValue.valueType().fieldType().dataType().primitiveType().dataGroup().isNumeric()) {
                return -9999999;
            }
            if (activeValue.valueType().fieldType().dataType().primitiveType().dataGroup().isTemporal()) {
                return "9999-09-09";
            }
            return "-9999999";
        }

        @Override
        public Object objectOut(ObjectData objectData) {
            StringJoiner joiner = new StringJoiner(this.objDelim, "", "");
            for (ActiveData activeData : objectData.nodes()) {
                joiner.add(activeData.activeOut(this).toString());
            }
            return joiner.toString();
        }

        public Object headerOut(ObjectData objectData) {
            StringJoiner joiner = new StringJoiner(this.objDelim, "", "");
            for (ActiveData activeData : objectData.nodes()) {
                joiner.add(activeData.name());
            }
            return joiner.toString();
        }

        @Override
        public Object arrayOut(ArrayData nodeArray) {
            StringJoiner joiner = new StringJoiner(this.aryDelim, "", "");
            if (nodeArray.size() > 0) {
                ActiveData firstRow = nodeArray.nodes().get(0);
                if (firstRow instanceof ObjectData) {
                    joiner.add(headerOut((ObjectData) firstRow).toString());
                }
            }
            for (ActiveData n : nodeArray.nodes()) {
                joiner.add(n.activeOut(this).toString());
            }
            return joiner.toString();
        }
    }
}
