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
package org.schorn.ella.impl.context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.ActiveContext.Data;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.http.ActiveHTTP;
import org.schorn.ella.io.EndPoint;
import org.schorn.ella.repo.ActiveRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class ContextDataImpl extends AbstractContextual implements Data {

    @SuppressWarnings("unused")
    static private final Logger LGR = LoggerFactory.getLogger(ContextDataImpl.class);

    private final boolean hasRepo;
    private final ActiveRepo repo;
    private final Map<Class<?>, Predicate<?>> repoFilters;
    private final EndPoint.URIPoint uriPoint;
    private final ActiveHTTP.ContextServer activeHTTP;

    ContextDataImpl(AppContext context, boolean hasRepo) throws Exception {
        super(context);
        this.hasRepo = hasRepo;
        if (this.hasRepo) {
            this.repo = ActiveRepo.get(this.context());
        } else {
            this.repo = null;
        }
        this.repoFilters = new HashMap<>();
        this.uriPoint = this.httpAddress();
        if (this.uriPoint != null) {
            this.activeHTTP = ActiveHTTP.ContextServer.get(this.uriPoint);
        } else {
            this.activeHTTP = null;
        }
    }

    @Override
    public boolean hasRepo() {
        return this.hasRepo;
    }

    @Override
    public ActiveRepo repo() {
        return this.repo;
    }

    @Override
    public <T> void setRepoFilter(Class<T> classOfT, Predicate<T> repoFilter) {
        this.repoFilters.put(classOfT, repoFilter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Predicate<T> getRepoFilter(Class<T> classForT) {
        return (Predicate<T>) this.repoFilters.get(classForT);
    }

    @Override
    public ActiveHTTP.ContextServer http() {
        return this.activeHTTP;
    }

}
