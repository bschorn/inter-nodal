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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Predicate;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;
import org.schorn.ella.repo.RepoSupport.ActiveCondition;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActiveQuery Implementation
 *
 *
 *
 * @author schorn
 *
 */
public class ActiveQueryImpl implements ActiveQuery {

    /*
	 * 
     */
    static public ActiveQuery create(AppContext context,
            ObjectType[] from,
            String name,
            List<ValueType> select,
            Predicate<ActiveData> where,
            Set<Integer> queryFlags,
            List<ValueType> orderBy,
            ObjectType to) {
        return new ActiveQueryImpl(context, name, select.toArray(new ValueType[0]), null, from, new ObjectType[]{to}, where, queryFlags, orderBy.toArray(new ValueType[0]), "");
    }

    /*
	 * 
     */
    static public Builder builder(AppContext context, String name) {
        return new ActiveQueryImpl.BuilderImpl(context, name);
    }

    static final Logger LGR = LoggerFactory.getLogger(ActiveQuery.class);

    private final AppContext context;
    private final Map<ObjectType, Set<ValueType>> selectable;
    private final ValueType[] select;
    private final ValueTypeMember[] selectValueTypeMember;
    private final ObjectType[] from;
    private final String name;
    private final Predicate<ActiveData> where;
    private final Set<Integer> queryFlags;
    private final ValueType[] orderBy;
    private final String whereDescription;
    private ObjectType[] to = null;

    /*
	 * 
     */
    public ActiveQueryImpl(AppContext context,
            String name,
            ValueType[] select,
            ValueTypeMember[] selectValueTypeMember,
            ObjectType[] from,
            ObjectType[] to,
            Predicate<ActiveData> where,
            Set<Integer> queryFlags,
            ValueType[] orderBy,
            String whereDescription) {
        this.context = context;
        this.from = from;
        this.select = select;
        this.selectValueTypeMember = selectValueTypeMember;
        this.selectable = new HashMap<>();
        if (this.select != null) {
            Set<ValueType> set = new HashSet<>();
            this.selectable.put(from[0], set);
            for (ValueType vt : select) {
                set.add(vt);
            }
        } else if (this.selectValueTypeMember != null) {
            for (ValueTypeMember vtm : selectValueTypeMember) {
                Set<ValueType> set = this.selectable.get(vtm.memberOfType());
                if (set == null) {
                    set = new HashSet<>();
                    this.selectable.put(vtm.memberOfType(), set);
                }
                set.add(vtm.memberType());
            }
        }
        this.to = to;
        this.name = name;
        this.where = where != null ? where : n -> true;
        this.whereDescription = whereDescription.trim();
        if (this.selectValueTypeMember == null && (this.select == null || this.select.length == 0) && this.to == null) {
            this.to = new ObjectType[this.from.length];
            this.to[0] = this.from[0];
        }
        this.queryFlags = queryFlags;
        this.orderBy = orderBy;
    }

    @Override
    public AppContext context() {
        return this.context;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public ValueType[] select() {
        return this.select;
    }

    @Override
    public ValueType select(int index) {
        return this.select[index];
    }

    @Override
    public ValueType[] select(ObjectType memberOfType) {
        int i = 0;
        for (ValueTypeMember valueTypeMember : this.selectValueTypeMember) {
            if (valueTypeMember.equals(memberOfType)) {
                i++;
            }
        }
        ValueType[] valueTypes = new ValueType[i];
        i = 0;
        for (ValueTypeMember valueTypeMember : this.selectValueTypeMember) {
            if (valueTypeMember.equals(memberOfType)) {
                valueTypes[i++] = valueTypeMember.memberType();
            }
        }
        return valueTypes;
    }

    @Override
    public boolean isSelected(ActiveType memberOfType, ActiveType memberType) {
        Set<ValueType> set = this.selectable.get(memberOfType);
        return (set != null && set.contains(memberType));
    }

    @Override
    public QueryType queryType() {
        if (this.select == null && this.selectValueTypeMember == null && this.from.length == 1) {
            return QueryType.OBJECT;
        } else if (this.select != null && this.select.length == 1 && this.from().length == 1) {
            return QueryType.LIST;
        } else if (this.selectValueTypeMember != null) {
            return QueryType.COPY_REDUCE;
        } else {
            return QueryType.OTHER;
        }
    }

    @Override
    public ObjectType[] from() {
        return this.from;
    }

    @Override
    public Predicate<ActiveData> where() {
        return this.where;
    }

    @Override
    public ValueType[] orderBy() {
        return this.orderBy;
    }

    @Override
    public boolean hasFlag(QueryFlag queryFlag) {
        return this.queryFlags.contains(queryFlag.flagId());
    }

    @Override
    public ObjectType[] to() throws Exception {
        if (this.to == null) {
            ObjectType[] to = new ObjectType[this.from.length];
            for (int i = 0; i < this.from.length; i += 1) {
                String tempTypeName = String.format("%s#%s", this.from[i].name(), UUID.randomUUID().toString());
                ObjectType.Builder builder = ObjectType.builder(this.context(), tempTypeName, this.from[i].attributes());
                if (this.select != null) {
                    for (ValueType valueType : this.select) {
                        if (this.from[i].isMember(valueType)) {
                            builder.add(valueType);
                        }
                    }
                    to[i] = builder.lease();
                } else if (this.selectValueTypeMember != null) {
                    to[i] = from[i];
                }

            }
            this.to = to;
        }
        return this.to;
    }

    @Override
    public ArrayData resultData() throws Exception {
        /*
		 * By calling the method to() rather than using member this.to in the below segment,
		 * we are insuring not to get the 'null' that this.to may still be.
         */
        return this.to()[0].arrayType().create();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("SELECT ");
        if (this.hasFlag(QueryFlags.DISTINCT)) {
            builder.append(" DISTINCT ");
        } else {
            builder.append("");
        }
        if (this.select == null && this.selectValueTypeMember == null) {
            builder.append("  *");
        } else if (this.select != null && this.select.length > 0) {
            StringJoiner joiner = new StringJoiner(", ", "", "");
            for (ValueType valueType : this.select) {
                joiner.add(valueType.name());
            }
            builder.append(joiner.toString());
        } else if (this.selectValueTypeMember != null && this.selectValueTypeMember.length > 0) {
            StringJoiner joiner = new StringJoiner(", ", "", "");
            for (ValueTypeMember valueTypeMember : this.selectValueTypeMember) {
                joiner.add(String.format("%s.%s", valueTypeMember.memberOfType().name(), valueTypeMember.memberType().name()));
            }
            builder.append(joiner.toString());
        }
        builder.append(" FROM ");
        //builder.append("  ");
        StringJoiner joiner = new StringJoiner(", ", "", "");
        for (ObjectType otype : this.from()) {
            joiner.add(String.format("%s.$%s", this.context.name(), otype.name()));
        }
        builder.append(joiner.toString());
        if (this.whereDescription.length() > 0) {
            builder.append(" WHERE ");
            builder.append(this.whereDescription);
        }
        if (this.hasFlag(QueryFlags.SORT) && this.orderBy != null && this.orderBy.length >= 0) {
            StringJoiner orderByJoiner = new StringJoiner(", ", " ORDER BY ", "");
            for (ValueType valueType : this.orderBy) {
                orderByJoiner.add(valueType.name());
            }
            builder.append(orderByJoiner.toString());
        }
        return builder.toString();
    }

    /**
     * Builder for RepoQuery
     *
     *
     */
    static public class BuilderImpl implements ActiveQuery.Builder {

        AppContext context;
        String name;
        ValueType[] select = null;
        ValueTypeMember[] selectValueTypeMember = null;
        ObjectType[] fromType = new ObjectType[1];
        ObjectType[] toType = null;
        Predicate<ActiveData> where = null;
        List<String> descriptives = new ArrayList<>();
        Set<Integer> queryFlags = new HashSet<>();
        ValueType[] orderBy = null;

        public BuilderImpl(AppContext context, String name) {
            this.context = context;
            this.name = name;
        }

        @Override
        public Builder ditto() {
            BuilderImpl builder = new BuilderImpl(this.context, this.name);
            if (this.select != null) {
                builder.select = new ValueType[this.select.length];
                System.arraycopy(this.select, 0, builder.select, 0, this.select.length);
            }
            if (this.selectValueTypeMember != null) {
                builder.selectValueTypeMember = new ValueTypeMember[this.selectValueTypeMember.length];
                System.arraycopy(this.selectValueTypeMember, 0, builder.selectValueTypeMember, 0, this.selectValueTypeMember.length);
            }
            if (this.fromType != null) {
                builder.fromType = new ObjectType[this.fromType.length];
                System.arraycopy(this.fromType, 0, builder.fromType, 0, this.fromType.length);
            }
            if (this.toType != null) {
                builder.toType = new ObjectType[this.toType.length];
                System.arraycopy(this.toType, 0, builder.toType, 0, this.toType.length);
            }
            if (this.orderBy != null) {
                builder.orderBy = new ValueType[this.orderBy.length];
                System.arraycopy(this.orderBy, 0, builder.orderBy, 0, this.orderBy.length);
            }
            for (String descriptive : this.descriptives) {
                builder.descriptives.add(descriptive);
            }
            builder.queryFlags.addAll(this.queryFlags);
            return builder;
        }

        @Override
        public ActiveQuery build() throws Exception {
            if (this.context == null) {
                throw new Exception("RepoQuery.Builder has a null AppContext");
            }
            if (this.fromType == null || this.fromType.length == 0 || this.fromType[0].isDynamic()) {
                throw new Exception(String.format("%s.build() - can not call build(). The builder has no from() or the ObjectType [%s] is not able to be queried",
                        this.getClass().getSimpleName(),
                        this.fromType == null ? "null" : this.fromType[0].name()));
            }

            if (this.descriptives != null) {
                StringJoiner joiner = new StringJoiner("  ", "  ", "");
                this.descriptives.forEach(v -> joiner.add(v));
                return new ActiveQueryImpl(this.context, this.name, this.select, this.selectValueTypeMember, this.fromType, this.toType, this.where, this.queryFlags, this.orderBy, joiner.toString());
            } else {
                return new ActiveQueryImpl(this.context, this.name, this.select, this.selectValueTypeMember, this.fromType, this.toType, this.where, this.queryFlags, this.orderBy, "");
            }
        }

        public Builder from(String from) {
            this.fromType[0] = ObjectType.get(this.context, from);
            return this;
        }

        @Override
        public Builder from(ObjectType[] objectType) {
            this.fromType = objectType;
            return this;
        }

        public Builder to(ObjectType[] objectType) {
            this.toType = objectType;
            return this;
        }

        public Builder select(List<String> select) {
            List<ValueType> list = new ArrayList<>();
            for (String value_type : select) {
                ValueType valueType = ValueType.get(this.context, value_type);
                if (valueType != null) {
                    list.add(valueType);
                }
            }
            this.select = list.toArray(new ValueType[0]);
            return this;
        }

        public Builder select(ValueType... valueTypes) {
            this.select = valueTypes;
            return this;
        }

        @Override
        public Builder select(ValueTypeMember[] valueTypeMembers) {
            this.selectValueTypeMember = valueTypeMembers;
            return this;
        }

        @Override
        public Builder order_by(String... value_types) {
            this.queryFlags.add(QueryFlags.SORT.flagId());
            List<ValueType> list = new ArrayList<>();
            for (String value_type : value_types) {
                ValueType valueType = ValueType.get(this.context, value_type);
                if (valueType != null) {
                    list.add(valueType);
                }
            }
            this.orderBy = list.toArray(new ValueType[0]);
            return this;
        }

        @Override
        public Builder order_by(ValueType... valueTypes) {
            this.queryFlags.add(QueryFlags.SORT.flagId());
            this.orderBy = valueTypes;
            return this;
        }

        @Override
        public Builder order_by(ValueTypeMember... valueTypeMembers) {
            this.queryFlags.add(QueryFlags.SORT.flagId());
            ValueType[] valueTypes = new ValueType[valueTypeMembers.length];
            int i = 0;
            for (ValueTypeMember valueTypeMember : valueTypeMembers) {
                valueTypes[i++] = valueTypeMember.memberType();
            }
            this.orderBy = valueTypes;
            return null;
        }

        public Builder where(Predicate<ActiveData> where) {
            this.where = where;
            if (where instanceof ActiveCondition) {
                this.descriptives.add(((ActiveCondition) where).descriptive());
            }
            return this;
        }

        public Builder and(Predicate<ActiveData> and) {
            if (this.where == null) {
                this.where = and;
                if (and instanceof ActiveCondition) {
                    this.descriptives.add(((ActiveCondition) and).descriptive());
                }
            } else {
                this.where = this.where.and(and);
                if (and instanceof ActiveCondition) {
                    this.descriptives.add("AND " + ((ActiveCondition) and).descriptive());
                }
            }
            return this;
        }

        public Builder or(Predicate<ActiveData> or) {
            if (this.where == null) {
                this.where = or;
                if (or instanceof ActiveCondition) {
                    this.descriptives.add(((ActiveCondition) or).descriptive());
                }
            } else {
                if (or instanceof ActiveCondition) {
                    this.descriptives.add("OR " + ((ActiveCondition) or).descriptive());
                }
                this.where = this.where.or(or);
            }
            return this;
        }

        /*
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder("SELECT");
			if (this.hasFlag(QueryFlags.DISTINCT)) {
				builder.append(" DISTINCT\n");
			} else {
				builder.append("\n");
			}
			if (this.select.isEmpty()) {
				builder.append("  *");
			} else {
				this.select.forEach(field -> { builder.append("  "); builder.append(field); });
			}
			builder.append("\nFROM\n");
			builder.append("  ");
			builder.append(this.context.getName());
			builder.append(".");
			StringJoiner joiner = new StringJoiner(", ","","");
			for (ObjectType otype : this.fromType) {
				joiner.add(String.format("%s.$%s", this.context.getName(), otype.name()));
			}
			builder.append(joiner.toString());
			if (!this.descriptives.isEmpty()) {
				builder.append("\nWHERE\n");
				builder.append(this.descriptives);
			}
			return builder.toString();
		}
         */
        @Override
        public Builder distinct() {
            this.queryFlags.add(QueryFlags.DISTINCT.flagId());
            return this;
        }

        @Override
        public Builder addFlags(QueryFlag... queryFlags) {
            for (QueryFlag queryFlag : queryFlags) {
                this.queryFlags.add(queryFlag.flagId());
            }
            return this;
        }

        @Override
        public boolean hasFlag(QueryFlag queryFlag) {
            return this.queryFlags.contains(queryFlag.flagId());
        }
    }
}
