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
package org.schorn.ella.impl.transform;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.schorn.ella.AbstractProvider;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.transform.ActiveTransform;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public class TransformProviderImpl extends AbstractProvider implements TransformProvider {

    private static final Logger LGR = LoggerFactory.getLogger(TransformProviderImpl.class);

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                MEMBERS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    //private final List<Class<? extends Mingleton>> mingletons = new ArrayList<>();

    @Override
    public void init() {
        this.mapInterfaceToImpl(ActiveTransform.Config.class, TransformConfigImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.OpenNodeToActiveNode.class, OpenNodeToActiveNodeImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.JSONToOpenNode.class, JSONToOpenNodeImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.JSONToActiveNode.class, JSONToActiveNodeImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.ActiveNodeToJSON.class, ActiveNodeToJSONImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.ActiveNodeToJsonRecord.class, ActiveNodeToJsonRecordImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.DSVLineParser.class, DSVLineParserImpl.class);
        //this.mapInterfaceToImpl(ActiveTransform.DSVStringToActiveNode.class, DSVStringToActiveNodeImpl.class);
        //this.mapInterfaceToImpl(ActiveTransform.DSVStringToOpenNode.class, DSVStringToOpenNodeImpl.class);

        /*
	 * Mingletons: one instance per NodeContext.
         */
 /*
        this.mingletons.add(ActiveTransform.OpenNodeToActiveNode.class);
        this.mingletons.add(ActiveTransform.JSONToOpenNode.class);
        this.mingletons.add(ActiveTransform.JSONToActiveNode.class);
        this.mingletons.add(ActiveTransform.ActiveNodeToJSON.class);
        this.mingletons.add(ActiveTransform.ActiveNodeToJsonRecord.class);
        this.mingletons.add(ActiveTransform.DSVLineParser.class);
         */
    }

    @Override
    protected void registerCreateNotification(AppContext context, Object registeredObj) {
        if (registeredObj instanceof Transform) {
            this.addTransform(context, (Transform<?, ?>) registeredObj);
        }
    }
    @Override
    public void registerContext(AppContext context) throws Exception {
        for (Class<?> classFor : this.mingletons()) {
            Object object = this.createReusable(classFor, context);
            LGR.info(String.format("%s.registerContext('%s') - create Mingleton: %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    classFor.getSimpleName()
            ));
            registerCreateNotification(context, object);
        }
        for (Class<?> classFor : this.renewables()) {
            Object object = this.createReusable(classFor, context);
            LGR.info(String.format("%s.registerContext('%s') - create Renewable: %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    classFor.getSimpleName()
            ));
            registerCreateNotification(context, object);
        }
    }

    /*
    @Override
    public void registerContext(AppContext context) throws Exception {
        for (Class<?> classFor : this.mingletons) {
            Transform<?, ?> transform = (Transform<?, ?>) this.createReusable(classFor, context);
            this.addTransform(context, transform);
            LGR.info(String.format("%s.registerContext('%s') - create Mingleton: %s",
                    this.getClass().getSimpleName(),
                    context.name(),
                    classFor.getSimpleName()
            ));
        }
    }
     */

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    *                                
    *                                MEMBERS
    *                                
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    private final Map<AppContext, Set<Transform<?, ?>>> transformers = new HashMap<>();

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    *                                
    *                                METHODS
    *                                
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
 /*
     *	Add Transform                                
     */
    @Override
    public void addTransform(AppContext context, Transform<?, ?> transform) {
        Set<Transform<?, ?>> set = this.transformers.get(context);
        if (set == null) {
            set = new HashSet<>();
            this.transformers.put(context, set);
        }
        if (transform != null) {
            set.add(transform);
            LGR.info(String.format("Adding Transformer %s for context %s",
                    transform.getClass().getSimpleName(), context.name()));
        }

    }

    /*
     *	Get Transform                                
     */
    @Override
    public Transform<?, ?> getTransform(AppContext context, Format source, Format target) {
        Set<Transform<?, ?>> set = this.transformers.get(context);
        if (set != null) {
            Optional<Transform<?, ?>> optTransform = set.stream()
                    .filter(transform -> (transform.sourceFormat().equals(source) && transform.targetFormat().equals(target)))
                    .findAny();
            if (optTransform.isPresent()) {
                return optTransform.get();
            }
        }
        return null;
    }

}
