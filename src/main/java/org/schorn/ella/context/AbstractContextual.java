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
package org.schorn.ella.context;

import java.util.HashMap;
import java.util.Map;

import org.schorn.ella.FunctionalException;
import org.schorn.ella.context.ActiveContext.Contextual;

/**
 *
 * There is one instance per context. The instance may be used across multiple
 * threads. Exception is kept by thread - one per thread.
 *
 * @author schorn
 *
 */
public abstract class AbstractContextual implements Contextual, FunctionalException {

    private final AppContext context;
    private final Map<Long, Exception> exception;

    protected AbstractContextual(AppContext context) {
        this.context = context;
        this.exception = new HashMap<>();
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    @Override
    public void throwException() throws Exception {
        Exception exception = this.exception.remove(Thread.currentThread().getId());
        if (exception != null) {
            throw exception;
        }
    }

    protected void setException(Exception exception) {
        this.exception.put(Thread.currentThread().getId(), exception);
    }
    /*
	protected Exception getException() {
		Exception exception = this.exception.get(Thread.currentThread().getId());
		if (exception != null) {
			return exception;
		}
		return null;
	}
     */

}
