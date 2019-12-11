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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.repo.RepoActions.MaintainReferences;
import org.schorn.ella.repo.RepoData;
import org.schorn.ella.util.Functions;

/**
 *
 *
 * @author schorn
 *
 */
class MaintainReferencesImpl extends AbstractContextual implements MaintainReferences {

    private static final Logger LGR = LoggerFactory.getLogger(MaintainReferencesImpl.class);

    private final RepoData.CurrentState repoData;
    private final Map<Integer, List<ObjectData>> waitingChildren;

    protected MaintainReferencesImpl(AppContext context) {
        super(context);
        this.repoData = RepoData.CurrentState.get(context);
        this.waitingChildren = new HashMap<>();
    }

    /**
     * Find this object's parent and append as a child to that parent. If the
     * parent does not exists (yet) then stash the object onto a waiting list ->
     * waiting for the parent to go by then append the
     *
     *
     */
    @Override
    public void accept(ObjectData objectData) {

        List<ObjectData> objectFamily = new ArrayList<>();

        objectFamily.addAll(objectData.getAncestors());
        objectFamily.add(objectData);
        objectFamily.addAll(objectData.getDescendants());

        for (ObjectData familyMember : objectFamily) {
            this.accept0(familyMember);
        }
    }

    /**
     *
     * @param objectData
     */
    private void accept0(ObjectData objectData) {
        try {
            if (objectData == null) {
                return;
            }

            if (!objectData.objectType().equals(objectData.objectType().parentType())) {
                Integer parentKey = objectData.getParentSeriesKey();
                if (parentKey != null) {
                    /*
					 * Look for the parent by asking repo for parent type with parent key
                     */
                    ObjectData parentData = this.repoData.get(objectData.objectType().parentType(), parentKey);
                    if (parentData != null) {
                        /*
						 * 
                         */
                        if (objectData.objectType().isParentAnAttribute()) {
                            /*
							 * Append the parent to this object.
                             */
                            objectData.appendChild(parentData);
                        } else {
                            /*
							 * Append this object to the parent.
                             */
                            parentData.appendChild(objectData);
                        }
                    } else {
                        /*
						 * If we don't find the parent, we can place object onto a waiting list.
						 * When the parent eventually comes through we appendChild then.
                         */
                        List<ObjectData> waitingList = this.waitingChildren.get(parentKey);
                        if (waitingList == null) {
                            waitingList = new ArrayList<>();
                            this.waitingChildren.put(parentKey, waitingList);
                        }
                        waitingList.add(objectData);
                    }
                }
            }
            /*
			 * Are you a parent with waiting children?
             */
            Integer seriesKey = objectData.getSeriesKey();
            List<ObjectData> childrenList = this.waitingChildren.get(seriesKey);
            if (childrenList != null && childrenList.isEmpty()) {
                for (ObjectData childrenData : childrenList) {
                    if (childrenData.objectType().isParentAnAttribute()) {
                        childrenData.appendChild(objectData);
                    } else {
                        objectData.appendChild(childrenData);
                    }
                }
                childrenList.clear();
            }

        } catch (Exception ex) {
            this.setException(ex);
            LGR.error("{}.accept() - Failed to process:\n{}\nException:\n{}",
                    this.getClass().getSimpleName(),
                    objectData.toString(),
                    Functions.getStackTraceAsString(ex));
        }

    }
}
