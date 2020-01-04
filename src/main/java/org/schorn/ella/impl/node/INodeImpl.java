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
package org.schorn.ella.impl.node;

import java.time.LocalDateTime;
import java.util.UUID;
import org.schorn.ella.format.SupportString;
import org.schorn.ella.node.ActiveNode.INode;

/**
 *
 * @author schorn
 *
 */
public class INodeImpl implements INode {

    public static INode create(ActiveData activeData, Identity identity) {
        return new INodeImpl(activeData, identity);
    }

    private ActiveData activeData;
    private UUID uuid;
    private LocalDateTime createTS;
    private LocalDateTime modifyTS;
    private LocalDateTime accessTS;
    private Identity createId;
    private Identity modifyId;
    private Identity accessId;

    public INodeImpl(ActiveData activeData, Identity identity) {
        this.activeData = activeData;
        this.uuid = UUID.randomUUID();
        this.createTS = LocalDateTime.now();
        this.modifyTS = LocalDateTime.now();
        this.accessTS = LocalDateTime.now();
        this.createId = identity;
        this.modifyId = identity;
        this.accessId = identity;
    }

    public INodeImpl() {

    }

    @Override
    public String name() {
        return this.activeData.name();
    }

    @Override
    public UUID uuid() {
        return this.uuid;
    }

    @Override
    public ActiveData data() {
        return this.activeData;
    }

    @Override
    public LocalDateTime createTS() {
        return this.createTS;
    }

    @Override
    public Identity createId() {
        return this.createId;
    }

    @Override
    public LocalDateTime modifyTS() {
        return this.modifyTS;
    }

    @Override
    public Identity modifyId() {
        return this.modifyId;
    }

    /**
     *
     * @return
     */
    @Override
    public LocalDateTime accessTS() {
        return this.accessTS;
    }

    @Override
    public Identity accessId() {
        return this.accessId;
    }

    @Override
    public void modify(Identity identity) {
        this.modifyId = identity;
        this.modifyTS = LocalDateTime.now();
    }

    /**
     *
     * @param identity
     */
    @Override
    public void access(Identity identity) {
        this.accessId = identity;
        this.accessTS = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof INode) {
            return this.compareTo((INode) object) == 0;
        }
        return false;
    }

    @Override
    public int compareTo(INode that) {
        return this.uuid().compareTo(that.uuid());
    }

    @Override
    public String toString() {
        return SupportString.format(this);
    }

}
