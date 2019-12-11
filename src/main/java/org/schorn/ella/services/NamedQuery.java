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
package org.schorn.ella.services;

import java.util.List;

import org.schorn.ella.FunctionalException;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.ActiveQuery.QueryFlag;

/**
 * NamedQuery is just a typed container for collecting the values necessary for
 * an ActiveQuery.Builder object which itself is a staging object for an
 * ActiveQuery object.
 *
 *
 * @author schorn
 *
 */
public interface NamedQuery extends FunctionalException {

    ObjectType createIntoType(AppContext context, ValueTypeMember[] selectValueTypeMembers, ObjectType fromType) throws Exception;

    String queryContext();

    String queryName();

    String queryDescription();

    List<String> selectFields();

    List<String> queryOptions();

    List<String> queryFilters();

    ActiveQuery.Builder builder();

    /**
     * NamedQuery (Analog)
     *
     * @param context_str
     * @param query_name
     * @param description
     * @param select_type
     * @param from_type
     * @param to_type
     * @param query_flags
     * @param order_by_value_type
     * @param options
     * @param filters
     * @return
     * @throws Exception
     */
    static public NamedQuery create(
            String context_str,
            String query_name,
            String description,
            String[] select_type,
            String[] from_type,
            String[] to_type,
            String[] query_flags,
            String[] order_by_value_type,
            String[] options,
            String[] filters) throws Exception {
        return ServicesProvider.provider().createNamedQuery(context_str, query_name, description, select_type, from_type, to_type, query_flags, order_by_value_type, options, filters);
    }

    /**
     * NamedQuery (Digital)
     *
     * @param context
     * @param name
     * @param description
     * @param selectValueTypeMembers
     * @param fromType
     * @param toType
     * @param queryFlags
     * @param orderBy
     * @param options
     * @param filters
     * @return
     * @throws Exception
     */
    static public NamedQuery create(
            AppContext context,
            String name,
            String description,
            ValueTypeMember[] selectValueTypeMembers,
            ObjectType[] fromType,
            ObjectType[] toType,
            QueryFlag[] queryFlags,
            ValueType[] orderBy,
            String[] options,
            String[] filters) throws Exception {
        return ServicesProvider.provider().createNamedQuery(context, name, description, selectValueTypeMembers, fromType, toType, queryFlags, orderBy, options, filters);
    }

}
