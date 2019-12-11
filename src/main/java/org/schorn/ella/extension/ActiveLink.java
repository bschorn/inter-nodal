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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.node.ActiveNode.Role;

/**
 *
 * @author schorn
 *
 */
public interface ActiveLink {

    default void link(ActiveNode.ActiveData childData) {
        if (this instanceof ActiveNode.ActiveData) {
            Support.link(((ActiveNode.ActiveData) this).activeId(), childData.activeId());
        }
    }

    default void delink(ActiveNode.ActiveData childData) {
        if (this instanceof ActiveNode.ActiveData) {
            Support.delink(((ActiveNode.ActiveData) this).activeId(), childData.activeId());
        }
    }

    /**
     *
     * @param oldChildData
     * @param newChildData
     */
    default void relink(ActiveNode.ActiveData oldChildData, ActiveNode.ActiveData newChildData) {
        if (this instanceof ActiveNode.ActiveData) {
            Support.relink(((ActiveNode.ActiveData) this).activeId(), oldChildData.activeId(), newChildData.activeId());
        }
    }

    default List<Integer> parents() {
        if (this instanceof ActiveNode.ActiveData) {
            return Support.parents(((ActiveNode.ActiveData) this).activeId());
        }
        return Support.EMPTY_LIST;
    }

    default List<Integer> parents(Role parentRole) {
        if (this instanceof ActiveNode.ActiveData) {
            List<Integer> parents = Support.parents(((ActiveNode.ActiveData) this).activeId());
            return parents.stream()
                    .map(activeId -> NodeProvider.provider().getActiveData(activeId))
                    .filter(activeData -> activeData != null)
                    .filter(activeData -> activeData.role().equals(parentRole))
                    .map(activeData -> activeData.activeId())
                    .collect(Collectors.toList());
        }
        return Support.EMPTY_LIST;
    }

    /**
     *
     * @return
     */
    default List<Integer> children() {
        if (this instanceof ActiveNode.ActiveData) {
            return Support.children(((ActiveNode.ActiveData) this).activeId());
        }
        return Support.EMPTY_LIST;
    }

    /*
	 * Implementation
     */
    static class Support {

        static final private List<Integer> EMPTY_LIST = new ArrayList<>(0);
        static Map<Integer, Set<Integer>> PARENT_CHILD = new TreeMap<>();
        static Map<Integer, Set<Integer>> CHILD_PARENT = new TreeMap<>();

        static void link(Integer parent, Integer child) {
            synchronized (child) {
                Set<Integer> parents = CHILD_PARENT.get(child);
                if (parents == null) {
                    parents = new TreeSet<>();
                    CHILD_PARENT.put(child, parents);
                }
                synchronized (parent) {
                    Set<Integer> children = PARENT_CHILD.get(parent);
                    if (children == null) {
                        children = new TreeSet<>();
                        PARENT_CHILD.put(parent, children);
                    }
                    children.add(child);
                    parents.add(parent);
                }
            }
        }

        static void delink(Integer parent, Integer child) {
            Set<Integer> children = PARENT_CHILD.get(parent);
            Set<Integer> parents = CHILD_PARENT.get(child);
            if (children != null && parents != null) {
                synchronized (child) {
                    synchronized (parent) {
                        children.remove(child);
                        parents.remove(parent);
                    }
                }
            }
        }

        static void relink(Integer parent, Integer oldChild, Integer newChild) {
            if (oldChild.intValue() == newChild.intValue()) {
                return;
            }
            synchronized (oldChild) {
                synchronized (newChild) {
                    synchronized (parent) {
                        delink(parent, oldChild);
                        link(parent, newChild);
                    }
                }
            }

        }

        static List<Integer> parents(Integer child) {
            Set<Integer> parents = CHILD_PARENT.get(child);
            return parents.stream().collect(Collectors.toList());
        }

        static List<Integer> children(Integer parent) {
            Set<Integer> children = PARENT_CHILD.get(parent);
            return children.stream().collect(Collectors.toList());
        }
    }
}
