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

import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;
import org.schorn.ella.util.Functions;

/**
 * JSON
 *
 * asJsonString() - converts an ActiveNode to a String of JSON
 *
 *
 * @author schorn
 *
 */
public interface ActiveJson {

    @SuppressWarnings({"rawtypes", "unchecked"})
    default String asJsonString() {
        if (this instanceof StructData) {
            StructData structData = (StructData) this;
            Transform transformer = TransformProvider.provider().getTransform(structData.context(), Format.ActiveNode, Format.JSON);
            String results = (String) transformer.apply((StructData) this);
            if (results == null) {
                try {
                    transformer.throwException();
                } catch (Exception ex) {
                    results = Functions.getStackTraceAsString(ex);
                }
            }
            return results;
        }
        return null;
    }
}
