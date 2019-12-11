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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.NodeProvider;

/**
 *
 * @author schorn
 *
 */
public interface ActiveFamily {

    default ActiveData getParent() {
        if (this instanceof ActiveData) {
            Integer parentId = Support.PARENTS.get(((ActiveData) this).activeId());
            if (parentId != null) {
                return NodeProvider.provider().getActiveData(parentId);
            }
        }
        return null;
    }

    default void setParent(ActiveData parentData) {
        if (this instanceof ActiveData) {
            Support.PARENTS.put(((ActiveData) this).activeId(), parentData.activeId());
        }
    }

    default <T extends ActiveData> T getClosestAncestor(Class<T> classForT) {
        if (this instanceof ActiveData) {
            Role role = Role.roleFor(classForT);
            ActiveData parentData = ((ActiveData) this).getParent();

            while (parentData != null) {
                if (parentData.role().equals(role)) {
                    return classForT.cast(parentData);
                }
                ActiveData nextData = parentData.getParent();
                if (nextData == parentData || nextData == null) {
                    break;
                }
                parentData = nextData;
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    default ObjectData parentObject() {
        if (this instanceof ActiveData && this instanceof ActiveLink) {
            ActiveData activeData = (ActiveData) this;
            ActiveLink activeLink = (ActiveLink) this;
            List<Integer> parentIds = activeLink.parents(Role.Object);
            final AppContext context = activeData.context();
            List<ActiveData> parents = NodeProvider.provider().getActiveData(parentIds, ad -> ad.context().equals(context));
            List<ObjectData> parentObjects = parents.stream()
                    .filter(ad -> ad.role().equals(Role.Object))
                    .map(ad -> (ObjectData) ad)
                    .collect(Collectors.toList());
            if (!parentObjects.isEmpty()) {
                return parentObjects.get(0);
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    default List<ObjectData> getDescendants() {
        if (this instanceof ObjectData) {
            List<ObjectData> list = new ArrayList<>();
            Support.traverseFor((ObjectData) this, list);
            return list;
        }
        return new ArrayList<>(0);
    }

    /**
     * Reverses up the lineage (skipping arrays)
     *
     * root -> ObjectData -> ObjectData -> this (not included)
     *
     * @return
     */
    default List<ObjectData> getAncestors() {
        if (this instanceof ActiveData) {
            List<ObjectData> list = new ArrayList<>();
            Support.reverseFor((ActiveData) this, list);
            Collections.reverse(list);
            return list;
        }
        return new ArrayList<>(0);
    }

    default String getLineage() {
        if (this instanceof ActiveData) {
            List<ObjectData> ancestors = this.getAncestors();
            StringJoiner joiner = new StringJoiner("->", "", "");
            ancestors.stream()
                    .map(od -> String.format("%s[%d]", od.name(), od.activeId()))
                    .forEach(s -> joiner.add(s));
            return joiner.toString();
        }
        return null;
    }

    /**
     *
     *
     */
    static class Support {

        static private Map<Integer, Integer> PARENTS = new ConcurrentHashMap<>();

        static void reverseFor(ActiveData activeData, List<ObjectData> ancestors) {
            ActiveData parentData = activeData.getParent();
            if (parentData != null && parentData != activeData) {
                if (!parentData.role().equals(Role.Array)) {
                    ancestors.add((ObjectData) parentData);
                }
                reverseFor(parentData, ancestors);
            }
        }

        static void traverseFor(ActiveData activeData, List<ObjectData> descendants) {
            switch (activeData.role()) {
                case Object:
                case Array:
                    StructData structData = (StructData) activeData;
                    for (ActiveData child : structData.nodes()) {
                        switch (child.role()) {
                            case Object:
                                descendants.add((ObjectData) child);
                            case Array:
                                traverseFor(child, descendants);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
