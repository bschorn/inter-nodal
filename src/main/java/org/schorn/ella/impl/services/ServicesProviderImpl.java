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

import java.util.ArrayList;
import java.util.List;
import org.schorn.ella.AbstractProvider;
import org.schorn.ella.Mingleton;
import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;
import org.schorn.ella.repo.RepoSupport.ActiveQuery.QueryFlag;
import org.schorn.ella.services.ActiveServices;
import org.schorn.ella.services.NamedQuery;
import org.schorn.ella.services.ServicesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class ServicesProviderImpl extends AbstractProvider implements ServicesProvider {

    private static final Logger LGR = LoggerFactory.getLogger(ServicesProviderImpl.class);

    private List<Class<? extends Mingleton>> mingletons = new ArrayList<>();
    private List<Class<? extends Renewable<?>>> renewables = new ArrayList<>();

    @Override
    public void init() throws Exception {
        this.mapInterfaceToImpl(NamedQuery.class, NamedQueryImpl.class);
        this.mapInterfaceToImpl(ActiveServices.class, ServiceProviderImpl.class);

        this.mingletons.add(ActiveServices.class);
    }

    @Override
    public void registerContext(AppContext context) throws Exception {
        for (Class<?> classFor : this.mingletons) {
            this.createReusable(classFor, context);
            LGR.info(String.format("%s.registerContext('%s') - create Mingleton: %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    classFor.getSimpleName()
            ));
        }
        for (Class<?> classFor : this.renewables) {
            this.createReusable(classFor, context);
            LGR.info(String.format("%s.registerContext('%s') - create Renewable: %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    classFor.getSimpleName()
            ));
        }
    }

    @Override
    public NamedQuery createNamedQuery(AppContext context, String name, String description,
            ValueTypeMember[] selectValueTypes, ObjectType[] fromType, ObjectType[] toType, QueryFlag[] queryFlags,
            ValueType[] orderBy, String[] options, String[] filters) throws Exception {
        return new NamedQueryImpl(context, name, description, selectValueTypes, fromType, toType, queryFlags, orderBy, options, filters);
    }

    @Override
    public NamedQuery createNamedQuery(String context_str, String name, String description, String[] select_types,
            String[] from_types, String[] to_types, String[] query_flags, String[] order_by_value_types, String[] options,
            String[] filters) {
        return new NamedQueryImpl(context_str, name, description, select_types, from_types, to_types, query_flags, order_by_value_types, options, filters);
    }

}
