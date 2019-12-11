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
package org.schorn.ella.impl.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.ActiveQuery.QueryFlag;
import org.schorn.ella.repo.RepoSupport.ActiveQuery.QueryFlags;
import org.schorn.ella.services.NamedQuery;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
public class NamedQueryImpl implements NamedQuery {

    private String queryName;
    private Exception exception = null;
    private AppContext context;
    private String queryDescription;
    private List<String> selectFields;
    private List<String> queryOptions;
    private List<String> queryFilters;
    private ActiveQuery.Builder queryBuilder;

    NamedQueryImpl(
            AppContext context,
            String name,
            String description,
            ValueTypeMember[] selectValueTypeMembers,
            ObjectType[] fromType,
            ObjectType[] toType,
            QueryFlag[] queryFlags,
            ValueType[] orderBy,
            String[] options,
            String[] filters) {
        try {
            this.context = context;
            this.queryName = name;
            this.queryDescription = description;
            this.selectFields = Arrays.asList(selectValueTypeMembers).stream().map(vtm -> vtm.memberType().name()).collect(Collectors.toList());
            this.queryBuilder = ActiveQuery.builder(context, this.queryName);
            this.queryBuilder.select(selectValueTypeMembers);
            this.queryBuilder.from(fromType);
            if (toType != null) {
                this.queryBuilder.to(toType);
            }
            this.queryBuilder.addFlags(queryFlags);
            this.queryBuilder.order_by(orderBy);
            this.queryOptions = Arrays.asList(options);
            this.queryFilters = Arrays.asList(filters);

        } catch (Exception e) {
            this.exception = e;
        }
    }

    NamedQueryImpl(
            String context_str,
            String name,
            String description,
            String[] select_types,
            String[] from_types,
            String[] to_types,
            String[] query_flags,
            String[] order_by,
            String[] options,
            String[] filters) {
        try {
            Optional<AppContext> optContext = AppContext.valueOf(context_str);
            if (optContext.isPresent()) {
                this.context = optContext.get();
                this.queryName = name;
                this.queryBuilder = ActiveQuery.builder(context, this.queryName);
                this.queryDescription = description;
                ValueTypeMember[] valueTypeMembers = ValueTypeMember.parse(this.context, select_types);
                this.queryBuilder.select(valueTypeMembers);

                ObjectType[] fromTypes = ObjectType.parse(this.context, from_types);
                this.queryBuilder.from(fromTypes);

                ObjectType[] toTypes = ObjectType.parse(this.context, to_types);
                this.queryBuilder.to(toTypes);

                QueryFlags[] queryFlags = new QueryFlags[query_flags.length];
                for (int i = 0; i < query_flags.length; i += 1) {
                    queryFlags[i] = QueryFlags.valueOf(query_flags[i]);
                }
                this.queryBuilder.addFlags(queryFlags);

                ValueType[] orderByValueTypes = ValueType.parse(this.context, order_by);
                this.queryBuilder.order_by(orderByValueTypes);

                this.selectFields = Arrays.asList(valueTypeMembers).stream().map(vtm -> vtm.memberType().name()).collect(Collectors.toList());
                this.queryOptions = Arrays.asList(options);
                this.queryFilters = Arrays.asList(filters);
            }
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Override
    public void throwException() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
    }

    public ActiveQuery.Builder builder() {
        return this.queryBuilder;
    }

    @Override
    public String toString() {
        try {
            return this.queryBuilder.build().toString();
        } catch (Exception e) {
            return Functions.getStackTraceAsString(e);
        }
    }

    @Override
    public ObjectType createIntoType(AppContext context, ValueTypeMember[] selectValueTypeMembers,
            ObjectType fromType) throws Exception {
        String tempTypeName = this.queryName;
        ObjectType.Builder builder = ObjectType.builder(context, tempTypeName, fromType.domainType());
        for (ValueTypeMember valueTypeMember : selectValueTypeMembers) {
            builder.add(valueTypeMember.memberType());
        }
        return builder.build();
    }

    @Override
    public String queryName() {
        return this.queryName;
    }

    @Override
    public String queryDescription() {
        return this.queryDescription;
    }

    @Override
    public List<String> selectFields() {
        return this.selectFields;
    }

    @Override
    public List<String> queryOptions() {
        return this.queryOptions;
    }

    @Override
    public List<String> queryFilters() {
        return this.queryFilters;
    }

    @Override
    public String queryContext() {
        return this.context.name();
    }

}
