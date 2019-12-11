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

import java.io.Reader;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.parser.ActiveParser;

/**
 *
 * @author schorn
 *
 */
public class JsonReaderToOpenNodeImpl extends TransformExceptionImpl /*implements JsonReaderToOpenNode*/ {

    /*
	ActiveJson.ReadJson parseJson;
	ActiveContext nodeContext;

	public JsonReaderToOpenNodeImpl(ActiveContext nodeContext) {
		this.nodeContext = nodeContext;
		this.parseJson = ActiveJson.ReadJson.get();
	}

	@Override
	public String toString() {
		return String.format("%s", this.getClass().getSimpleName(), this.sourceFormat().toString(),
				this.targetFormat().toString());
	}

	@Override
	public OpenNode apply(Reader reader) {
		this.clearException();
		try {
			return this.parseJson.parse(reader);
		} catch (Exception ex) {
			this.setException(ex);
		}
		return null;
	}
     */
}
