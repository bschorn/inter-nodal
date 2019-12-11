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
import org.schorn.ella.repo.ActiveRepo.ServicesRepo;
import org.schorn.ella.repo.RepoCoordinators;
import org.schorn.ella.repo.RepoProvider;

/**
 *
 *
 * @author schorn
 *
 */
class ServicesRepoImpl extends AbstractContextual implements ServicesRepo {

    private final RepoCoordinators.Inspector approver;
    private final RepoCoordinators.Receiver storage;
    private final RepoCoordinators.Dispatcher distributor;
    private final RepoCoordinators.Responder responder;
    private final RepoCoordinators.Summary summary;

    ServicesRepoImpl(AppContext context) {
        super(context);
        this.approver = RepoProvider.provider().getMingleton(RepoCoordinators.Inspector.class, context);
        this.storage = RepoProvider.provider().getMingleton(RepoCoordinators.Receiver.class, context);
        this.distributor = RepoProvider.provider().getMingleton(RepoCoordinators.Dispatcher.class, context);
        this.responder = RepoProvider.provider().getMingleton(RepoCoordinators.Responder.class, context);
        this.summary = RepoProvider.provider().getMingleton(RepoCoordinators.Summary.class, context);
    }

    @Override
    public RepoCoordinators.Inspector getInspector() {
        return this.approver;
    }

    @Override
    public RepoCoordinators.Receiver getReceiver() {
        return this.storage;
    }

    @Override
    public RepoCoordinators.Dispatcher getDispatcher() {
        return this.distributor;
    }

    @Override
    public RepoCoordinators.Responder getResponder() {
        return this.responder;
    }

    @Override
    public RepoCoordinators.Summary getSummary() {
        return this.summary;
    }

}
