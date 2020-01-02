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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.schorn.ella.html.ActiveHtml.HtmlAttribute;
import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class HtmlElementImpl implements HtmlElement {

    static public final String[] INDENT = new String[]{"  ", "    ", "      ", "        ", "          ", "            ", "              "};
    static public final String LINEFEED = "\n";
    private static final Logger LGR = LoggerFactory.getLogger(HtmlElementImpl.class);

    protected HtmlElement parent;
    protected Integer level = 0;
    protected String tag;
    protected String textContent = "";
    protected List<HtmlElement> children = new ArrayList<>();
    protected List<HtmlAttribute> attributes = new ArrayList<>();

    HtmlElementImpl() {
        this.parent = this;
        this.tag = "root";
    }

    HtmlElementImpl(String tag) {
        this.parent = this;
        this.tag = tag;
    }

    HtmlElementImpl(HtmlTag html) throws Exception {
        this.parent = this;
        this.tag = html.tag();
        if (html instanceof HtmlTag.Tag) {
            HtmlTag.Tag htmlTag = (HtmlTag.Tag) html;
            for (HtmlTag.TagAttribute attribute : htmlTag.attributes()) {
                switch (attribute.attributeValueType()) {
                    case BOOL:
                        this.addAttribute0(HtmlAttribute.create(attribute.attributeName(), htmlTag.getFlagAttribute(attribute)));
                        break;
                    case LIST:
                        break;
                    case INTEGER:
                        this.addAttribute0(HtmlAttribute.create(attribute.attributeName(), htmlTag.getIntegerAttribute(attribute)));
                        break;
                    case DOUBLE:
                        this.addAttribute0(HtmlAttribute.create(attribute.attributeName(), htmlTag.getDoubleAttribute(attribute)));
                        break;
                    case TEXT:
                    case TEMPORAL:
                        this.addAttribute0(HtmlAttribute.create(attribute.attributeName(), htmlTag.getTextAttribute(attribute)));
                        break;
                }
            }
        }
    }

    public void setLevel(Integer level) {
        this.level = level;
        this.children.forEach(e -> ((HtmlElementImpl) e).setLevel(level + 1));
    }

    public void setTextContent(String content) {
        this.textContent = content;
    }

    public String getTextContent() {
        return this.textContent;
    }

    public final void append(HtmlElement element) {
        HtmlElementImpl elementImpl = (HtmlElementImpl) element;
        // element is not a root (has a parent already)
        if (elementImpl.parent != element) {
            // remove element from previous parent's children list
            ((HtmlElementImpl) elementImpl.parent).children.remove(element);
        }
        // set parent to this
        elementImpl.parent = this;
        // add to this children's list
        this.children.add(element);
    }

    public void addClass(String className) {
        List<HtmlAttribute> attrs = this.attributes.stream().filter(a -> a.name().equals("class")).collect(Collectors.toList());
        if (attrs.isEmpty()) {
            try {
                this.addAttribute0(HtmlAttribute.create("class", className));
            } catch (Exception ex) {
                LGR.error(Functions.stackTraceToString(ex));
            }
        } else {
            attrs.stream().forEach(a -> a.addValue(className));
        }
    }

    public void addAttribute(HtmlAttribute attribute) {
        this.addAttribute0(attribute);
    }

    public void setId(String value) throws Exception {
        if (value != null) {
            this.addAttribute0(HtmlAttribute.create("id", value));
        }
    }

    public String getId() {
        Optional<HtmlAttribute> id = this.attributes.stream().filter(a -> a.name().equalsIgnoreCase("id")).findFirst();
        if (id.isPresent()) {
            return id.get().value().toString();
        }
        return "";
    }

    @Override
    public String render() {
        StringBuilder builder = new StringBuilder();
        builder.append(renderIndent());
        builder.append(renderStartTag());
        builder.append(renderContent());
        if (!this.children.isEmpty()) {
            builder.append(renderLinefeed());
            builder.append(renderChildren());
            builder.append(renderIndent());
        }
        builder.append(renderEndTag());
        builder.append(renderLinefeed());
        return builder.toString();
    }

    @Override
    public String toString() {
        try {
            return render();
        } catch (Exception ex) {
            LGR.error(Functions.getStackTraceAsString(ex));
        }
        return "";
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
     * 
     * 										PROTECTED
     * 
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    protected final void addAttribute0(HtmlAttribute attribute) {
        if (attribute.render() != null) {
            List<HtmlAttribute> existing = this.attributes.stream().filter(a -> a.name().equals(attribute.name())).collect(Collectors.toList());
            if (existing.isEmpty()) {
                this.attributes.add((HtmlAttributeImpl) attribute);
            } else {
                existing.forEach(a -> a.setValue(attribute.value()));
            }
        }
    }

    protected String renderIndent() {
        return INDENT[this.level];
    }

    protected String renderLinefeed() {
        return LINEFEED;
    }

    protected String renderAttributes() {
        StringJoiner joiner = new StringJoiner(" ", "", "");
        attributes.forEach((attribute) -> {
            joiner.add(attribute.toString());
        });
        return joiner.toString();
    }

    protected String renderStartTag() {
        if (this.attributes.isEmpty()) {
            return String.format("<%s>", this.tag);
        } else {
            return String.format("<%s %s>", this.tag, this.renderAttributes());
        }
    }

    protected String renderContent() {
        return this.textContent != null ? this.textContent : "";
    }

    protected String renderChildren() {
        StringBuilder builder = new StringBuilder();
        if (this.children != null && !this.children.isEmpty()) {
            children.stream()
                    .filter(e -> ((HtmlElementImpl) e).parent == this)
                    .forEach(e -> {
                        try {
                            builder.append(e.render());
                        } catch (Exception ex) {

                        }
                    });
        }
        return builder.toString();
    }

    protected String renderEndTag() {
        return String.format("</%s>", this.tag);
    }

}
