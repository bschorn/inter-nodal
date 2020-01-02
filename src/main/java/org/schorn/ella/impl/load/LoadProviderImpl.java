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
package org.schorn.ella.impl.load;

import java.util.ArrayList;
import java.util.List;
import org.schorn.ella.AbstractProvider;
import org.schorn.ella.Mingleton;
import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.load.ActiveObjectLoad;
import org.schorn.ella.load.ActiveTabularLoad;
import org.schorn.ella.load.LoadProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public class LoadProviderImpl extends AbstractProvider implements LoadProvider {

    private static final Logger LGR = LoggerFactory.getLogger(LoadProviderImpl.class);

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                MEMBERS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private List<Class<? extends Mingleton>> mingletons = new ArrayList<>();
    private List<Class<? extends Renewable<?>>> renewables = new ArrayList<>();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                METHODS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void init() {
        this.mapInterfaceToImpl(ActiveObjectLoad.JsonBulkProcessor.class, JsonBulkProcessorImpl.class);
        this.mapInterfaceToImpl(ActiveTabularLoad.ActiveTypeValue.class, ActiveTypeValueImpl.class);
        this.mapInterfaceToImpl(ActiveTabularLoad.DbRowProcessor.class, DbRowProcessorImpl.class);
        this.mapInterfaceToImpl(ActiveTabularLoad.ActiveObjectAssembler.class, ActiveObjectAssemblerImpl.class);
        this.mapInterfaceToImpl(ActiveTabularLoad.LoadFromQuery.class, LoadFromQueryImpl.class);

        /*
		 * Mingletons: one instance per NodeContext.
         */
        //his.mingletons.add(XYZ.class);
        /*
		 * Renewables: using an instance of an object to create
		 * subsequent instances (instance factory)
         */
        this.renewables.add(ActiveTabularLoad.LoadFromQuery.class);

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

}
