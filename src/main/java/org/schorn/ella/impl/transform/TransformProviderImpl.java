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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.schorn.ella.AbstractProvider;
import org.schorn.ella.Mingleton;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.transform.ActiveTransform;
import org.schorn.ella.transform.ActiveTransform.ActiveNodeToJSON;
import org.schorn.ella.transform.ActiveTransform.DSVLineParser;
import org.schorn.ella.transform.ActiveTransform.JSONToActiveNode;
import org.schorn.ella.transform.ActiveTransform.JSONToOpenNode;
import org.schorn.ella.transform.ActiveTransform.OpenNodeToActiveNode;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;
import org.schorn.ella.util.Functions;
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
    private final List<Class<? extends Mingleton>> mingletons = new ArrayList<>();

    @Override
    public void init() {
        this.mapInterfaceToImpl(ActiveTransform.OpenNodeToActiveNode.class, OpenNodeToActiveNodeImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.JSONToOpenNode.class, JSONToOpenNodeImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.JSONToActiveNode.class, JSONToActiveNodeImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.ActiveNodeToJSON.class, ActiveNodeToJSONImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.ActiveNodeToJsonRecord.class, ActiveNodeToJsonRecordImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.DSVLineParser.class, DSVLineParserImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.DSVStringToActiveNode.class, DSVStringToActiveNodeImpl.class);
        this.mapInterfaceToImpl(ActiveTransform.DSVStringToOpenNode.class, DSVStringToOpenNodeImpl.class);

        /*
	 * Mingletons: one instance per NodeContext.
         */
        this.mingletons.add(ActiveTransform.OpenNodeToActiveNode.class);
        this.mingletons.add(ActiveTransform.JSONToOpenNode.class);
        this.mingletons.add(ActiveTransform.JSONToActiveNode.class);
        this.mingletons.add(ActiveTransform.ActiveNodeToJSON.class);
        this.mingletons.add(ActiveTransform.ActiveNodeToJsonRecord.class);
        this.mingletons.add(ActiveTransform.DSVLineParser.class);
    }

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
    /**
     * Every NodeContext needs to create its own set of Transform instances (the
     * NodeContext is needed to find the data types).
     *
     * This only includes those Transform that are likely to be utilized. Any
     * additional transformations will have to be added by the client by
     * calling: TransformProvider.provider().addTransform(NodeContext,
     * Transform<?,?>)
     *
     * This method uses the interface classes (defined in this file) and calls
     * .newInstance() to have TransformProvider locate the implementations.
     *
     * @param context
     */
    //@Override
    public void registerContextOld(AppContext context) {
        Class<?>[] transforms = new Class<?>[]{
            OpenNodeToActiveNode.class,
            JSONToOpenNode.class,
            JSONToActiveNode.class,
            ActiveNodeToJSON.class,
            ActiveTransform.OpenNodeToJSON.class,
            DSVLineParser.class
        };
        for (Class<?> classOf : transforms) {
            try {
                /*
		 * Given and interface class, find the implementation and create instance
                 */
                Transform<?, ?> transform = (Transform<?, ?>) this.createInstance(classOf, context);
                /*
		 * Register instance with transformers list
                 */
                if (transform != null) {
                    this.addTransform(context, transform);
                    LGR.info("{}.registerContext('{}') - created instance of {} ({})",
                            this.getClass().getSimpleName(),
                            context.name(),
                            classOf.getSimpleName(),
                            transform.getClass().getName());
                } else {
                    LGR.error("{}.registerContext('{}') - no implementation for {}",
                            this.getClass().getSimpleName(),
                            context.name(),
                            classOf.getSimpleName());
                }
            } catch (Exception e) {
                LGR.error("{}.registerContext('{}') - failed on implementation for {}",
                        this.getClass().getSimpleName(),
                        context.name(),
                        classOf.getSimpleName());
                LGR.error(Functions.getStackTraceAsString(e));
            }
        }
    }

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
