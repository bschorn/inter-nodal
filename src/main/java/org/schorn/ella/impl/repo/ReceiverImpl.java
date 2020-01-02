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
package org.schorn.ella.impl.repo;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.repo.RepoActions.LogActivity;
import org.schorn.ella.repo.RepoActions.MaintainReferences;
import org.schorn.ella.repo.RepoActions.RegisterActivity;
import org.schorn.ella.repo.RepoCoordinators.Receiver;
import org.schorn.ella.repo.RepoProvider;

/**
 *
 * @author schorn
 *
 */
public class ReceiverImpl extends AbstractContextual implements Receiver {

    private final LogActivity persist;
    private final RegisterActivity store;
    private final MaintainReferences ref;

    ReceiverImpl(AppContext context) {
        super(context);
        this.persist = RepoProvider.provider().getMingleton(LogActivity.class, context);
        this.store = RepoProvider.provider().getMingleton(RegisterActivity.class, context);
        this.ref = RepoProvider.provider().getMingleton(MaintainReferences.class, context);
    }

    @Override
    public LogActivity persistActivity() {
        return this.persist;
    }

    @Override
    public RegisterActivity registerActivity() {
        return this.store;
    }

    @Override
    public MaintainReferences maintainReferences() {
        return this.ref;
    }

}
