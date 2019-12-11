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
package org.schorn.ella.impl.context;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.Singleton;
import org.schorn.ella.context.ActiveContext;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.context.AppContext.ContextRole;
import org.schorn.ella.context.ContextProvider;
import org.schorn.ella.context.ContextRegistry;

/**
 *
 * @author schorn
 *
 */
public class ContextProviderImpl extends AbstractProvider implements ContextProvider {

    private static final Logger LGR = LoggerFactory.getLogger(ContextProviderImpl.class);

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                MEMBERS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private final List<Class<? extends Singleton>> singletons = new ArrayList<>();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                METHODS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void init() throws Exception {
        this.mapInterfaceToImpl(ActiveContext.Meta.class, ContextMetaImpl.class);
        this.mapInterfaceToImpl(ActiveContext.Action.class, ActiveContextImpl.ActionImpl.class);
        this.mapInterfaceToImpl(ActiveContext.Activity.class, ContextActivityImpl.class);
        this.mapInterfaceToImpl(ActiveContext.Attribute.class, ContextMetaImpl.class);
        this.mapInterfaceToImpl(ActiveContext.Data.class, ContextDataImpl.class);
        this.mapInterfaceToImpl(ActiveContext.Error.class, ActiveContextImpl.ErrorImpl.class);
        this.mapInterfaceToImpl(ActiveContext.Property.class, ActiveContextImpl.PropertyImpl.class);
        this.mapInterfaceToImpl(ActiveContext.Registry.class, ActiveContextImpl.RegistryImpl.class);
        this.mapInterfaceToImpl(AppContext.class, AppContextImpl.class);

        this.singletons.add(ActiveContext.Registry.class);
        for (Class<?> classFor : this.singletons) {
            this.createReusable(classFor);
            LGR.info("{}.init() - create Singleton: {}",
                    this.getClass().getSimpleName(),
                    classFor.getSimpleName());
        }
    }

    @Override
    public void registerContext(AppContext context) throws Exception {
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T createContext(Class<T> interfaceFor, String name, ContextRole mode) throws Exception {
        AppContext context = null;
        if (ContextRegistry.isRegistered(name)) {
            context = ContextRegistry.get(name);
        } else {
            context = (AppContext) this.createInstance(interfaceFor, name, mode);
            if (context != null) {
                ContextRegistry.register(context);
            }
        }
        return (T) context;
    }

}
