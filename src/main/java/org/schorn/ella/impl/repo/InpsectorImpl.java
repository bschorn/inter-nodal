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
import org.schorn.ella.repo.RepoCoordinators.Inspector;
import org.schorn.ella.repo.RepoActions.ActivityActionable;
import org.schorn.ella.repo.RepoActions.ActivityEntitled;
import org.schorn.ella.repo.RepoActions.ValidateActivity;
import org.schorn.ella.repo.RepoProvider;

/**
 *
 *
 * @author schorn
 *
 */
class InpsectorImpl extends AbstractContextual implements Inspector {

    private final ValidateActivity validate;
    private final ActivityActionable actionable;
    private final ActivityEntitled entitled;

    InpsectorImpl(AppContext context) {
        super(context);
        this.validate = RepoProvider.provider().getMingleton(ValidateActivity.class, context);
        this.actionable = RepoProvider.provider().getMingleton(ActivityActionable.class, context);
        this.entitled = RepoProvider.provider().getMingleton(ActivityEntitled.class, context);
    }

    @Override
    public ValidateActivity isValid() {
        return this.validate;
    }

    @Override
    public ActivityEntitled isEntitled() {
        return this.entitled;
    }

    @Override
    public ActivityActionable isActionable() {
        return this.actionable;
    }
}
