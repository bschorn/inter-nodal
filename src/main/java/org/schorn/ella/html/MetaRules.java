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
package org.schorn.ella.html;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author bschorn
 */
public interface MetaRules {

    public Object[] members();

    public interface MetaValue {
        String getName();
        String getValue();
    }

    public interface MetaValueRules {

        public Object[] options();

        public String getValue(Object value);
    }

    /**
     * https://www.iana.org/assignments/character-sets/character-sets.xhtml
     */
    public enum Attribute implements MetaRules {
        charset(Charset.availableCharsets().values()),
        http_equiv((Object) HttpEquiv.values()),
        name((Object) MetaName.values()),
        content;

        private final Object[] members;

        Attribute(Object... members) {
            this.members = members;
        }

        @Override
        public Object[] members() {
            return this.members;
        }
    }

    /**
     *
     */
    public enum AttributeClass {
        AttributeName(Attribute.charset, Attribute.http_equiv, Attribute.name),
        AttributeValue(Attribute.content);

        private final List<Attribute> attributes;

        AttributeClass(Attribute... attributes) {
            this.attributes = Arrays.asList(attributes);
        }

        static public boolean isName(Attribute attribute) {
            return AttributeClass.AttributeName.attributes.contains(attribute);
        }

        static public boolean isValue(Attribute attribute) {
            return AttributeClass.AttributeValue.attributes.contains(attribute);
        }
    }

    /**
     *
     */
    public enum HttpEquiv implements MetaRules.MetaValue {
        content_security_policy("%s"),
        refresh("%d;url=%s");

        private String value;

        HttpEquiv(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public String getName() {
            return this.name().replace("_", "-");
        }
    }

    public enum MetaName implements MetaRules {
        application_name(),
        author(),
        description(),
        generator(),
        keywords(),
        referrer((Object) Referrer.values()),
        theme_color(),
        color_scheme(),
        creator(),
        googlebot(),
        publisher(),
        robots(),
        slurp(),
        viewport((Object) ViewPort.values());

        private final Object[] members;

        MetaName(Object... members) {
            this.members = members;
        }

        @Override
        public Object[] members() {
            return this.members;
        }

    }

    public enum Referrer implements MetaRules.MetaValue {
        no_referrer,
        origin,
        no_referrer_when_downgrade,
        origin_when_cross_origin,
        same_origin,
        strict_origin,
        strict_origin_when_cross_origin,
        unsafe_URL;

        @Override
        public String getName() {
            return "referrer";
        }

        @Override
        public String getValue() {
            return this.name().replace("_", "-");
        }

    }

    /**
     * <meta name="viewport" content="width=device-width, initial-scale=1">
     */
    public enum ViewPort implements MetaRules.MetaValueRules {
        width("%d", "device-width"),
        height("%d", "device-height"),
        initial_scale("%d"),
        maximum_scale("%d"),
        minimum_scale("%d"),
        user_scalable("%s", "yes", "no");

        private final String format;
        private final Object[] options;

        ViewPort(String format, Object... options) {
            this.format = format;
            this.options = options;
        }

        public Object[] options() {
            return this.options;
        }

        public String getValue(Object value) {
            if (value instanceof Integer) {
                return String.format(this.format, (Integer) value);
            }
            if (value instanceof String) {
                return String.format(this.format, (String) value);
            }
            return "";
        }

    }

}
