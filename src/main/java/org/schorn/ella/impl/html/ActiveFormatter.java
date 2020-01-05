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
package org.schorn.ella.impl.html;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.DataGroup;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is incomplete but functional.
 *
 * @author schorn
 *
 */
public class ActiveFormatter {

    private static final Logger LGR = LoggerFactory.getLogger(ActiveFormatter.class);

    static String DECIMAL_FORMAT_PATTERN = "#,###.##";
    static Map<Long, DecimalFormat> MAP_DECIMAL_FORMAT = new HashMap<>();

    static public DecimalFormat getDecimalFormat() {
        DecimalFormat decimalFormat = MAP_DECIMAL_FORMAT.get(Thread.currentThread().getId());
        if (decimalFormat == null) {
            synchronized (MAP_DECIMAL_FORMAT) {
                decimalFormat = new DecimalFormat(DECIMAL_FORMAT_PATTERN);
                MAP_DECIMAL_FORMAT.put(Thread.currentThread().getId(), decimalFormat);
            }
        }
        return decimalFormat;
    }

    static String INTEGER_FORMAT_PATTERN = "#,###";
    static Map<Long, DecimalFormat> MAP_INTEGER_FORMAT = new HashMap<>();

    static public NumberFormat getNumberFormat() {
        DecimalFormat decimalFormat = MAP_INTEGER_FORMAT.get(Thread.currentThread().getId());
        if (decimalFormat == null) {
            synchronized (MAP_INTEGER_FORMAT) {
                decimalFormat = new DecimalFormat(INTEGER_FORMAT_PATTERN);
                MAP_INTEGER_FORMAT.put(Thread.currentThread().getId(), decimalFormat);
            }
        }
        return decimalFormat;
    }

    /**
     * Format Function
     *
     * @param node
     * @return
     */
    static public Object format(ActiveData node) {
        try {
            switch (node.role()) {
                case Value:
                    return formatValue((ValueData) node);
                case Object:
                    return formatObject((ObjectData) node);
                case Array:
                    return formatArray((ArrayData) node);
                default:
                    break;
            }
        } catch (Exception ex) {
            LGR.error(Functions.getStackTraceAsString(ex));
        }
        return node.activeValue();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	 * 
	 * 						  				PRIVATE
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    static private Object formatValue(ValueData nodeValue) {
        if (nodeValue.isNull()) {
            return "NULL";
        } else if (nodeValue.isBlank()) {
            return "";
        }
        DataGroup dataGroup = nodeValue.valueType().fieldType().dataType().primitiveType().dataGroup();
        switch (dataGroup) {
            case DECIMAL:
                return getDecimalFormat().format(((Number) nodeValue.activeValue()).doubleValue());
            case NUMBER:
                return getNumberFormat().format(((Number) nodeValue.activeValue()).longValue());
            default:
                return nodeValue.activeValue().toString();
        }
    }

    /*
	 * TODO
     */
    static private Object formatObject(ObjectData nodeObject) {
        return null;
    }

    /*
	 * TODO
     */
    static private Object formatArray(ArrayData nodeArray) {
        return null;
    }
}
