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
package org.schorn.ella.impl.transform;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.transform.ActiveTransform.DSVLineParser;
import org.schorn.ella.transform.ActiveTransform.DSVStringToOpenNode;

/**
 *
 * @author schorn
 *
 */
public class DSVStringToOpenNodeImpl extends TransformExceptionImpl implements DSVStringToOpenNode {

    private static final Logger LGR = LoggerFactory.getLogger(DSVStringToOpenNodeImpl.class);

    AppContext nodeContext;
    DSVLineParser parser;
    String typeName;
    String[] fieldNames = null;

    public DSVStringToOpenNodeImpl(AppContext nodeContext, Consumer<ActiveData> consumer, DSVLineParser parser, String typeName) throws Exception {
        this.nodeContext = nodeContext;
        this.parser = parser;
        this.typeName = typeName;
    }

    @Override
    public OpenNode apply(String line) {
        this.clearException();
        List<Object> tokens = this.parser.apply(line);
        if (this.fieldNames == null) {
            this.fieldNames = tokens.toArray(new String[0]);
        } else {
            if (this.fieldNames.length != tokens.size()) {
                tokens = tryAgainParser(line);
            }
            if (this.fieldNames.length != tokens.size()) {
                LGR.error("Unable to parse line: " + line);
                return null;
            }
            OpenNode openNode = OpenNode.createObject(this.typeName);
            for (int i = 0; i < this.fieldNames.length; ++i) {
                OpenNode openValue = OpenNode.createValue(this.fieldNames[i]);
                openValue.add(tokens.get(i));
            }
            return openNode;
        }
        return null;
    }

    /**
     * Hack
     *
     * @param line
     * @return
     */
    public static List<Object> tryAgainParser(String line) {
        final char ndelm = '^';
        final char odelm = ',';
        final char qual = '"';
        boolean in = false;
        char[] na = new char[line.length()];
        int nai = 0;
        for (int i = 0; i < line.length(); ++i) {
            switch (line.charAt(i)) {
                case odelm:
                    na[nai++] = in ? odelm : ndelm;
                    break;
                case qual:
                    in = in ? false : true;
                    break;
                default:
                    na[nai++] = line.charAt(i);
                    break;
            }
        }
        String newStr = new String(na);
        String[] newfields = newStr.split("[\\^]");
        return Arrays.asList(newfields).stream().map(s -> s.trim()).collect(Collectors.toList());
    }
}
