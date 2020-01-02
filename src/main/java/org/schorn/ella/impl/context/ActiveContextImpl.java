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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.schorn.ella.Provider;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.ActiveContext;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.context.AppContext.ContextRole;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Identity;
import org.schorn.ella.node.ActiveNode.Identity.IdentityType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.repo.ActiveRepo;
import org.schorn.ella.util.Functions;
import org.schorn.ella.util.StringCached;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class ActiveContextImpl implements ActiveContext {

    @SuppressWarnings("unused")
    static private final Logger LGR = LoggerFactory.getLogger(ActiveContextImpl.class);

    /**
     * The maxium NodeContext that can be registered in a single session.
     */
    static final int MAX = 50;

    /**
     *
     *
     */
    static public class RegistryImpl implements Registry {

        static private final Logger LGR = LoggerFactory.getLogger(RegistryImpl.class);

        private AppContext[] contexts = new AppContext[MAX];

        @Override
        public void register(AppContext context) throws Exception {
            if (!this.values().stream().filter(n -> n.name().equals(context.name())).findAny().isPresent()) {
                LGR.info(String.format("%s.register() - registered context: %s",
                        AppContext.class.getSimpleName(), context.name()));
                this.contexts[context.ordinal()] = context;
                /*
                * Register the context with each Provider to create instances
                * for this context.
                 */
                for (Provider.Providers providerEnum : Provider.Providers.values()) {
                    try {
                        Provider instance = providerEnum.getInstance(Provider.class);
                        /*
                        * Non-data context should skip any provider that is data related.
                         */
                        if (!context.hasRepo() && providerEnum.dataProvider()) {
                            continue;
                        }
                        instance.registerContext(context);
                    } catch (Exception ex) {
                        LGR.error(Functions.stackTraceToString(ex));
                    }
                }
                context.open();
            }
        }

        @Override
        public Optional<AppContext> valueOf(String name) {
            return this.values().stream().filter(n -> n.name().equalsIgnoreCase(name)).findAny();
        }

        @Override
        public List<AppContext> values() {
            return Arrays.asList(this.contexts).stream().filter(c -> c != null).collect(Collectors.toList());
        }

        @Override
        public AppContext getContext(int contextIdx) {
            return this.contexts[contextIdx];
        }

        @Override
        public int getContextIdx(AppContext context) {
            for (int i = 0; i < this.contexts.length; i += 0) {
                if (this.contexts[i].equals(context)) {
                    return i;
                }
            }
            return -1;
        }
    }

    /**
     *
     *
     */
    static class AttributeImpl implements Attribute {

        static final private AtomicInteger ORDINALS = new AtomicInteger(0);
        @SuppressWarnings("unused")
        private final AppContext context;
        private final StringCached name;
        private final ContextRole mode;
        private final Integer ordinal;

        AttributeImpl(AppContext context, String name, ContextRole mode) {
            this.context = context;
            this.name = new StringCached(name);
            this.mode = mode;
            this.ordinal = ORDINALS.getAndIncrement();
        }

        @Override
        public String name() {
            return this.name.toString();
        }

        @Override
        public int ordinal() {
            return this.ordinal;
        }

        @Override
        public ContextRole contextMode() {
            return this.mode;
        }

    }

    /**
     *
     *
     */
    static class ErrorImpl implements Error {

        static private final Logger LGR = LoggerFactory.getLogger(ErrorImpl.class);

        @SuppressWarnings("unused")
        private final AppContext context;
        private final ValueType valueType;
        private final ObjectType objectType;
        private final ArrayType arrayType;

        ErrorImpl(AppContext context) {
            this.context = context;
            this.valueType = MetaTypes.ValueTypes.error_message.valueType();
            this.objectType = MetaTypes.ObjectTypes.error_data.objectType();
            this.arrayType = MetaTypes.Arrays.error_data.arrayType();
        }

        @Override
        public ValueType valueErrorType() {
            return this.valueType;
        }

        @Override
        public ObjectType objectErrorType() {
            return this.objectType;
        }

        @Override
        public ArrayType arrayErrorType() {
            return this.arrayType;
        }

        @Override
        public ObjectData createErrorObject(String message) {
            try {
                return this.objectType.create(this.objectType.create(message));
            } catch (Exception ex) {
                LGR.error(Functions.getStackTraceAsString(ex));
            }
            return null;
        }

        @Override
        public ArrayData createErrorArray(String[] messages) {
            try {
                ArrayData arrayData = this.arrayType.create();
                for (String message : messages) {
                    arrayData.add(this.objectType.create(this.valueType.create(message)));
                }
                return arrayData;
            } catch (Exception ex) {
                LGR.error(Functions.getStackTraceAsString(ex));
            }
            return null;
        }

    }

    /**
     *
     *
     */
    static class PropertyImpl implements Property {

        static private final Logger LGR = LoggerFactory.getLogger(PropertyImpl.class);

        @SuppressWarnings("unused")
        private final AppContext context;
        private final Map<Object, Object> objectMap;
        private final Map<String, String> stringMap;

        PropertyImpl(AppContext context) {
            this.context = context;
            this.objectMap = new HashMap<>();
            this.stringMap = new HashMap<>();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T getPropertyT(Class<T> classForT, Object propertyId) {
            Object object = objectMap.get(propertyId);
            if (object.getClass().isAssignableFrom(classForT)) {
                return (T) object;
            }
            return null;
        }

        /*
        @Override
        public String getProperty(String propertyKey) {
            String propertyValue = this.stringMap.get(propertyKey);
            if (propertyValue == null) {
                propertyValue = Component.NODE.getProperty(propertyKey);
                if (propertyValue == null) {
                    LGR.warn("{}.getProperty() - there was no property entry for '{}'",
                            this.getClass().getSimpleName(), propertyKey);
                }
            }
            return propertyValue;
        }

        @Override
        public String getProperty(String propertyKey, String defaultValue) {
            String propertyValue = this.stringMap.get(propertyKey);
            if (propertyValue == null) {
                propertyValue = Component.NODE.getProperty(propertyKey);
                if (propertyValue == null) {
                    return defaultValue;
                }
            }
            return propertyValue;
        }
         */

 /*
        @Override
        public void setPropertyT(Object propertyId, Object propertyObj) {
            this.objectMap.put(propertyId, propertyObj);
        }

        @Override
        public void setProperty(String propertyKey, String propertyValue) {
            this.stringMap.put(propertyKey, propertyValue);
        }
        */
    }

    /**
     *
     *
     */
    static class ActionImpl extends AbstractContextual implements Action {

        static private final Logger LGR = LoggerFactory.getLogger(ActionImpl.class);

        private boolean open;
        private final boolean submitToRepo;

        ActionImpl(AppContext context) {
            super(context);
            this.open = false;
            this.submitToRepo = submitToRepo(); // default method implemented in the interface Action
        }

        @Override
        public void exit(String why) {
            LGR.error("{}.exit() - Exit has been called for AppContext: '{}'. \nWhy? -> {}",
                    this.getClass().getSimpleName(), this.context().name(), why);
        }

        @Override
        public void open() {
            this.open = true;
        }

        @Override
        public boolean isOpen() {
            return this.open;
        }

        @Override
        public void submit(ObjectData objectData) {
            if (this.submitToRepo) {
                this.context().repo().submit(objectData);
            } else {
                this.context().http().update(objectData.objectType(), objectData);
            }
        }

        @Override
        public void load(ObjectData objectData, Identity identity) {
            /*
            * This bypasses the persistence (non-activity).
            * Should add little better security than Identity is of type Component.
            *
             */
            if (identity != null && identity.type().equals(IdentityType.Component)) {
                ActiveRepo.LoaderRepo repo = ActiveRepo.LoaderRepo.get(this.context());
                if (repo != null) {
                    repo.accept(objectData);
                }
            }
        }

    }

}
