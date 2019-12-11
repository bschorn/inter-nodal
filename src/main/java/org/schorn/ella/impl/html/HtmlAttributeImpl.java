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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import org.schorn.ella.html.ActiveHtml.HtmlAttribute;

/**
 *
 * @author schorn
 *
 */
class HtmlAttributeImpl implements HtmlAttribute {

    protected final String name;
    protected Object value;
    String rendered = null;

    HtmlAttributeImpl(String name, Object value) {
        this.name = name;
        this.value = value;
        this.setValue0(this.value);
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public Object value() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
        this.setValue0(this.value);
    }

    @Override
    public void addValue(Object value) {
        if (value == null) {
            return;
        }
        if (this.value == null) {
            this.setValue0(value);
            return;
        }
        if (value instanceof String && this.value instanceof String) {
            this.value = String.format("%s %s", this.value, value);
        } else if (value instanceof Number && this.value instanceof Number) {
            if (value instanceof BigDecimal && this.value instanceof BigDecimal) {
                this.value = BigDecimal.valueOf(((Number) value).doubleValue()).add(BigDecimal.valueOf(((Number) this.value).doubleValue()));
            } else if (value instanceof Double && this.value instanceof Double) {
                this.value = ((Double) value) + ((Double) this.value);
            } else if (value instanceof Float && this.value instanceof Float) {
                this.value = ((Float) value) + ((Float) this.value);
            } else if (value instanceof Integer && this.value instanceof Integer) {
                this.value = ((Integer) value) + ((Integer) this.value);
            }
        }
        this.setValue0(this.value);
    }

    @Override
    public String render() {
        return this.rendered;
    }

    @Override
    public String toString() {
        return this.rendered;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     * 
     * 										PRIVATE
     * 
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     * @param value
     */
    private void setValue0(Object value) {
        if (value != null) {
            if (value instanceof Boolean && (Boolean) value) {
                this.rendered = String.format("%s", name);
            } else if (value instanceof String) {
                this.rendered = String.format("%s='%s'", name, value);
            } else if (value instanceof Number) {
                if (value instanceof Double || value instanceof Float || value instanceof BigDecimal) {
                    this.rendered = String.format("%s='%f'", name, value);
                } else {
                    this.rendered = String.format("%s='%d'", name, value);
                }
            } else if (value instanceof Temporal) {
                if (value instanceof LocalDate) {
                    this.rendered = String.format("%s='%s'", ((LocalDate) value).format(DateTimeFormatter.ISO_LOCAL_DATE));
                } else if (value instanceof LocalTime) {
                    this.rendered = String.format("%s='%s'", ((LocalTime) value).format(DateTimeFormatter.ISO_LOCAL_TIME));
                } else if (value instanceof LocalDateTime) {
                    this.rendered = String.format("%s='%s'", ((LocalDateTime) value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                }
            }
        }
    }
}
