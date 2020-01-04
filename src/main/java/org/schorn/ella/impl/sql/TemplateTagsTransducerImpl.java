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
package org.schorn.ella.impl.sql;

import java.util.StringJoiner;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.sql.RDBMS.TemplateTags;
import org.schorn.ella.sql.RDBMS.TemplateTagsTransducer;

/**
 *
 * @author schorn
 *
 */
public class TemplateTagsTransducerImpl extends AbstractContextual implements TemplateTagsTransducer {

    public TemplateTagsTransducerImpl(AppContext context) {
        super(context);
    }

    /**
     *
     * @param tag
     */
    public String transduce(TemplateTags tag, String content, Object object) {
        if (object instanceof ObjectType) {
            return transduce0(tag, content, (ObjectType) object);
        } else if (object instanceof ObjectData) {
            return transduce0(tag, content, (ObjectData) object);
        } else {
            return null;
        }

    }

    /**
     * There are two replace methods. This one takes an ObjectData because tags
     * that are handle within require data.
     *
     *
     * @param content
     * @param objectData
     * @return
     */
    private String transduce0(TemplateTags tag, String content, ObjectData objectData) {
        switch (tag) {
            case TGT_COLUMN_VALUES:
                StringJoiner columnValues = new StringJoiner(", ", "", "");
                objectData.activeValues(ValueData.class).stream().forEach(vd -> {
                    if (vd.isNull()) {
                        columnValues.add("NULL");
                    } else if (vd.valueType().fieldType().dataType().primitiveType().dataGroup().isQuoted()) {
                        columnValues.add(String.format("'%s'", vd.activeValue().toString()));
                    } else {
                        columnValues.add(String.format("%s", vd.activeValue().toString()));
                    }
                });
                return content.replace(tag.tag(), columnValues.toString());
            case WHERE_KEYS_EQUAL_VALUES:
                StringJoiner keyEqualValues = new StringJoiner(" AND ", "", "");
                objectData.getKeyValues().stream().forEach(vdkey -> {
                    if (vdkey.valueType().fieldType().dataType().primitiveType().dataGroup().isQuoted()) {
                        keyEqualValues.add(String.format("%s = '%s'", vdkey.name(), vdkey.activeValue().toString()));
                    } else {
                        keyEqualValues.add(String.format("%s = %s", vdkey.name(), vdkey.activeValue().toString()));
                    }
                });
                return content.replace(tag.tag(), keyEqualValues.toString());
            default:
                return transduce0(tag, content, objectData.objectType());
        }
    }

    /**
     * There are two replace methods. This one takes an ObjectType so that we
     * could pre-build this sections for reuse because they don't require any
     * data to build.
     *
     * @param content
     * @param objectType
     * @return
     */
    private String transduce0(TemplateTags tag, String content, ObjectType objectType) {
        switch (tag) {
            case TGT_SCHEMA:
                // the ActiveContext.getPropertyValue() asks ActiveServer which locates the ActiveServer instance that owns that context and 
                // calls a method on the ActiveServer interface that the instance would have to implement return the value
                // This is a kludge but keeps ActiveSQL provider decoupled from the App.
                //return content.replace(tag.tag(), objectType.context().getProperty(ActiveSQL.getPropertyKey(ActiveSQL.class, TemplateTags.TGT_SCHEMA.name(), objectType)));
                return content;
            case TGT_TABLE:
                return content.replace(tag.tag(), objectType.name());
            case TGT_COLUMN_NAMES:
                StringJoiner columnNames = new StringJoiner(", ", "", "");
                objectType.valueTypes().forEach(vd -> columnNames.add(vd.name()));
                return content.replace(tag.tag(), columnNames.toString());
            case SET_BY_COLUMN_NAMES:
                StringJoiner setColumnsByName = new StringJoiner(", ", "", "");
                objectType.valueTypes().stream().forEach(vd -> setColumnsByName.add(String.format("%s = <src-alias>.%s", vd.name(), vd.name())));
                return content.replace(tag.tag(), setColumnsByName.toString());
            case TGT_JOIN_ON_KEYS:
                StringJoiner joinOnKeys = new StringJoiner(" AND ", "", "");
                objectType.schema().keys().stream()
                        .map(md -> md.activeType().name())
                        .forEach(name -> joinOnKeys.add(String.format("<tgt-alias>.%s = <src-alias>.%s", name, name)));

                return content.replace(tag.tag(), joinOnKeys.toString());
            case TGT_TABLE_SPECIAL_KEY:
                ValueType specialKey = objectType.specialKey();
                if (specialKey != null) {
                    return content.replace(tag.tag(), specialKey.name());
                }
                return content.replace(tag.tag(), "!ERROR!");
            case TGT_ALIAS:
                return content.replace(tag.tag(), "TGT");
            case SRC_ALIAS:
                return content.replace(tag.tag(), "SRC");
            default:
                break;
        }
        return content;
    }

}
