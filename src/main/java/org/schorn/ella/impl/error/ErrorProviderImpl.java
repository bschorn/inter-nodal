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
package org.schorn.ella.impl.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.error.ActiveError.DataErrorManager;
import org.schorn.ella.error.ActiveError.ErrorFlag;
import org.schorn.ella.error.ActiveError.ObjectDataError;
import org.schorn.ella.error.ErrorProvider;
import org.schorn.ella.util.Functions;

/**
 *
 *
 * @author schorn
 *
 */
public class ErrorProviderImpl extends AbstractProvider implements ErrorProvider {

    private static final Logger LGR = LoggerFactory.getLogger(ErrorProviderImpl.class);

    private final Map<AppContext, DataErrorManager> managers = new HashMap<>();
    private final Map<AppContext, List<Renewable<?>>> templates = new HashMap<>();

    @Override
    public void init() {
        this.mapInterfaceToImpl(DataErrorManager.class, DataErrorManagerImpl.class);
        this.mapInterfaceToImpl(ObjectDataError.class, ObjectDataErrorImpl.class);
    }

    @Override
    public void registerContext(AppContext context) {
        try {
            this.managers.put(context, this.createInstance(DataErrorManager.class, context));
            LGR.info(String.format("%s.registerContext('%s') - createInstance of %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    DataErrorManager.class.getSimpleName()
            ));
            List<Renewable<?>> templates = new ArrayList<>();
            this.templates.put(context, templates);
            /*
			 * Add Renewables to contextTemplates list
             */
            Renewable<?> template = this.createInstance(ObjectDataError.class, context);
            templates.add(template);
            LGR.info(String.format("%s.registerContext('%s') - createInstance of %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    ObjectDataError.class.getSimpleName()
            ));
        } catch (Exception e) {
            LGR.error(Functions.stackTraceToString(e));
        }

    }

    @Override
    public DataErrorManager dataErrorManager(AppContext context) {
        return this.managers.get(context);
    }

    @Override
    public <T> T create(AppContext context, Class<T> classOfT, ErrorFlag flag, Object value) {
        List<Renewable<?>> contextTemplates = this.templates.get(context);
        Optional<Renewable<?>> optTemplate = contextTemplates.stream().filter(r -> classOfT.isInstance(r)).findAny();
        if (optTemplate.isPresent()) {
            return classOfT.cast(optTemplate.get().renew(flag, value));
        }
        return null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                MEMBERS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
 /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                METHODS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
}
