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

import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.transform.ActiveTransform;
import org.schorn.ella.transform.TransformProvider;

/**
 *
 * @author schorn
 *
 */
public class JSONToActiveNodeImpl extends TransformExceptionImpl implements ActiveTransform.JSONToActiveNode {

    private final ActiveTransform.JSONToOpenNode part1;
    private final ActiveTransform.OpenNodeToActiveNode part2;

    public JSONToActiveNodeImpl(AppContext context) throws Exception {
        this.part1 = TransformProvider.provider().createInstance(ActiveTransform.JSONToOpenNode.class, context);
        if (this.part1 == null) {
            throw new Exception("JSONToActiveNode's Step-1 Transformer JSONToOpenNode was not found.");
        }
        this.part2 = TransformProvider.provider().createInstance(ActiveTransform.OpenNodeToActiveNode.class, context);
        if (this.part2 == null) {
            throw new Exception("JSONToActiveNode's Step-2 Transformer OpenNodeToActiveNode was not found.");
        }
    }

    @Override
    public StructData apply(String jsonString) {
        OpenNode openNode = this.part1.apply(jsonString);
        if (openNode != null) {
            return this.part2.apply(openNode);
        }
        return null;
    }

    @Override
    public void throwException() throws Exception {
        this.part1.throwException();
        this.part2.throwException();
    }

}
