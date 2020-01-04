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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.repo.RepoSupport.ActiveUpdate;
import org.schorn.ella.repo.RepoSupport.UpdateData;
import org.schorn.ella.server.ActiveServer;

/**
 *
 * @author schorn
 *
 */
public class ActiveUpdateImpl extends AbstractContextual implements ActiveUpdate {

    private final ObjectType targetType;
    private final OpenNode updateNode;
    private UpdateData[] updateData = null;

    public ActiveUpdateImpl(AppContext context) {
        super(context);
        this.updateNode = null;
        this.targetType = null;
    }

    public ActiveUpdateImpl(AppContext context, ObjectType targetType, OpenNode updateNode) {
        super(context);
        this.targetType = targetType;
        this.updateNode = updateNode;
    }

    @Override
    public ActiveUpdate renew(Object... params) {
        ObjectType targetType = this.targetType;
        OpenNode updateNode = this.updateNode;
        for (Object param : params) {
            if (param instanceof OpenNode) {
                updateNode = (OpenNode) param;
            } else if (param instanceof ObjectType) {
                targetType = (ObjectType) param;
            }
        }
        return new ActiveUpdateImpl(this.context(), targetType, updateNode);
    }

    @Override
    public ObjectType targetType() {
        return this.targetType;
    }

    @Override
    public boolean unbundle() {
        try {
            switch (this.updateNode.role()) {
                case Object:
                    this.updateData = new UpdateData[1];
                    this.updateData[0] = UpdateData.create(this.context(), this.targetType, this.updateNode);
                    break;
                case Array:
                    OpenNode.OpenArray openArray = (OpenNode.OpenArray) this.updateNode;
                    this.updateData = new UpdateData[openArray.nodes().size()];
                    List<OpenNode> nodes = openArray.nodes();
                    for (int i = 0; i < nodes.size(); i += 1) {
                        this.updateData[i] = UpdateData.create(this.context(), this.targetType, nodes.get(i));
                    }
                    break;
                default:
                    throw new Exception(String.format("%s.unbundle() - ",
                            this.getClass().getSimpleName()));
            }
        } catch (Exception ex) {
            this.setException(ex);
            return false;
        }
        return true;
    }

    @Override
    public UpdateData[] updateData() {
        return this.updateData;
    }

    @Override
    public void run() {
        try {

            List<Future<UpdateData>> results = ActiveServer.executorService().invokeAll(Arrays.asList(this.updateData));
            for (Future<UpdateData> future : results) {
                if (future.isDone()) {
                    try {
                        UpdateData updateData = future.get();
                    } catch (CancellationException ce) {

                    } catch (ExecutionException ee) {

                    } catch (InterruptedException ie) {

                    }
                }
            }

        } catch (Exception ex) {
            this.setException(ex);
        }

    }

}
