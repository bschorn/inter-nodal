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

import org.schorn.ella.Provider;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;
import org.schorn.ella.repo.RepoSupport.ActiveQuery.QueryFlag;

/**
 *
 * @author schorn
 *
 */
public interface ServicesProvider extends Provider {

    /**
     * Implementation for this interface is retrieved
     */
    static ServicesProvider provider() {
        return Provider.Providers.SERVICES.getInstance(ServicesProvider.class);
    }

    /**
     * Creates Named Query using real types (previously created).
     *
     * @param context
     * @param name
     * @param description
     * @param selectValueTypes
     * @param fromType
     * @param toType
     * @param queryFlags
     * @param orderBy
     * @param options
     * @param filters
     * @return
     * @throws Exception
     */
    public NamedQuery createNamedQuery(AppContext context, String name, String description, ValueTypeMember[] selectValueTypes,
            ObjectType[] fromType, ObjectType[] toType, QueryFlag[] queryFlags, ValueType[] orderBy, String[] options,
            String[] filters) throws Exception;

    /**
     * Creates Named Query using string descriptions of types. They will have to
     * be converted to real types.
     *
     * @param context_str
     * @param name
     * @param description
     * @param select_types
     * @param from_types
     * @param to_types
     * @param query_flags
     * @param order_by_value_types
     * @param options
     * @param filters
     * @return
     * @throws Exception
     */
    public NamedQuery createNamedQuery(String context_str, String name, String description, String[] select_types,
            String[] from_types, String[] to_types, String[] query_flags, String[] order_by_value_types, String[] options,
            String[] filters) throws Exception;
}
