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

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.parser.ActiveParser;
import org.schorn.ella.transform.ActiveTransform;

/**
 *
 * @author schorn
 *
 */
public class JSONToOpenNodeImpl extends TransformExceptionImpl implements ActiveTransform.JSONToOpenNode {

    private ActiveParser.ReadJson parseJson;
    private AppContext context;

    public JSONToOpenNodeImpl(AppContext context) {
        this.context = context;
        this.parseJson = ActiveParser.ReadJson.get();
    }

    @Override
    public String toString() {
        return String.format("%s", this.getClass().getSimpleName(), this.sourceFormat().toString(),
                this.targetFormat().toString());
    }

    @Override
    public OpenNode apply(String content) {
        this.clearException();
        try {
            return this.parseJson.parse(content);
        } catch (Exception ex) {
            this.setException(ex);
        }
        return null;
    }
}
