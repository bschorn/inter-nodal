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
import java.util.stream.Collectors;
import org.schorn.ella.app.NodeConfig;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveRef;
import org.schorn.ella.node.ActiveNode.ActiveRef.ReferenceType;
import org.schorn.ella.node.ActiveNode.DomainType;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.MetaTypes.AutoTypes;

/**
 *
 * @author schorn
 *
 */
public interface AppContextExt {

    /**
     * EntityTypes associated with this instance
     */
    default List<ObjectType> aggregateTypes() {
        if (this instanceof AppContext) {
            return ((AppContext) this).objectTypes().stream()
                    .filter(ot -> ot.domainType().equals(DomainType.Aggregate))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * EntityTypes associated with this instance
     */
    default List<ObjectType> entityTypes() {
        if (this instanceof AppContext) {
            return ((AppContext) this).objectTypes().stream()
                    .filter(ot -> ot.domainType().equals(DomainType.Aggregate) || ot.domainType().equals(DomainType.Entity))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * EntityTypes associated with this instance
     */
    default List<ObjectType> valueObjectTypes() {
        if (this instanceof AppContext) {
            return ((AppContext) this).objectTypes().stream()
                    .filter(ot -> ot.domainType().equals(DomainType.ValueObject))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * AutoVersioning of ObjectData is on by default. If you want to turn off
     * AutoVersioning for a context:
     * myAppContext.setProperty(ActiveConfig.AUTO_VERSIONING.key(), "0");
     *
     *
     * @return
     */
    default boolean useAutoVersioning() {
        if (this instanceof AppContext) {
            AppContext context = (AppContext) this;
            if (context.hasRepo()) {
                String autovercfg = context.getProperty(NodeConfig.AUTO_VERSIONING.propertyKey(), "1");
                return !autovercfg.equals("0");
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @return
     */
    default List<ValueType> autoTypes() {
        if (this instanceof AppContext) {
            AppContext context = (AppContext) this;
            if (context.useAutoVersioning()) {
                List<ValueType> autoTypes = new ArrayList<>();
                for (AutoTypes autoType : AutoTypes.values()) {
                    autoTypes.add(autoType.valueType());
                }
                return autoTypes;
            }
        }
        return new ArrayList<>(0);
    }

    default void assignParentAsAttribute(ObjectType childType, ObjectType parentType) {
        if (this instanceof AppContext) {
            AppContext context = (AppContext) this;
            ActiveRef activeRef = context.getActiveRef();
            activeRef.get(childType).add(ReferenceType.PARENT_AS_ATTRIBUTE, parentType);
        }
    }

    default void assignParent(ObjectType childType, ObjectType parentType) {
        if (this instanceof AppContext) {
            AppContext context = (AppContext) this;
            ActiveRef activeRef = context.getActiveRef();
            activeRef.get(childType).add(ReferenceType.PARENT, parentType);
        }
    }

}
