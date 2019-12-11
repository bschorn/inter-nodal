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
package org.schorn.ella.impl.sql;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.Mingleton;
import org.schorn.ella.Provider;
import org.schorn.ella.Renewable;
import org.schorn.ella.Singleton;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
abstract class AbstractProvider implements Provider {

    private static final Logger LGR = LoggerFactory.getLogger(AbstractProvider.class);

    /**
     * Map of interface class to implementation class
     */
    Map<Class<?>, Class<?>> CLASS_IMPL = new HashMap<>();

    /**
     * Previously created instances that can be reused. The objects contained
     * need to be stateless and can have a single instance run concurrently.
     */
    Map<String, Object> REUSE_IMPL = new HashMap<>();

    @Override
    public void mapInterfaceToImpl(Class<?> interfaceFor, Class<?> implFor) {
        this.CLASS_IMPL.put(interfaceFor, implFor);
    }

    /**
     * Creates a new instance of the implementation class register for the
     * interface in parameters + any parameters for the constructor.
     *
     * This removes any static coupling between interfaces and implementations
     * but requires that both sides agree in order to create a new instance.
     *
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws Exception
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T createInstance(Class<T> interfaceFor, Object... params) throws Exception {
        Class<?> classFor = CLASS_IMPL.get(interfaceFor);
        if (classFor == null) {
            LGR.error("{}.newInstance() - there is no implementation specified for interface {}",
                    this.getClass().getSimpleName(), interfaceFor.getSimpleName());
            return null;
        }
        Constructor<?> constructor = null;
        T newInstance = null;
        for (Constructor<?> ctr : classFor.getDeclaredConstructors()) {
            if (params.length == ctr.getParameterCount()) {
                constructor = ctr;
                for (int i = 0; i < params.length; i = i + 1) {
                    Class<?> paramClass = ctr.getParameterTypes()[i];
                    Object paramObj = params[i];
                    if (paramObj == null || paramClass.isInstance(paramObj)) {
                        continue;
                    }
                    constructor = null;
                    break;
                }
            }
            if (constructor != null) {
                newInstance = (T) constructor.newInstance(params);
                break;
            }
        }
        if (newInstance == null) {
            StringJoiner joiner = new StringJoiner(", ", "[", "]");
            for (Object o : params) {
                joiner.add(String.format("(%s) %s", o.getClass().getSimpleName(), o.toString()));
            }
            LGR.error("{}.newInstance() - there is no constructor available to match the parameters {} specified for interface {}",
                    this.getClass().getSimpleName(),
                    joiner.toString(),
                    interfaceFor.getSimpleName());
        }
        return newInstance;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T createReusable(Class<T> interfaceFor, Object... params) throws Exception {
        StringJoiner joiner = new StringJoiner(":", "", "");
        joiner.add(interfaceFor.getName());
        for (Object param : params) {
            if (param != null) {
                joiner.add(param.getClass().getName());
                joiner.add(param.toString());
            }
        }
        String key = joiner.toString();
        Object instance = REUSE_IMPL.get(key);
        if (instance == null) {
            instance = createInstance(interfaceFor, params);
            if (instance != null) {
                REUSE_IMPL.put(key, instance);
            }
        }
        return (T) instance;
    }

    @Override
    public <T> T getReusable(Class<T> interfaceFor, Object... params) {
        try {
            return (T) createReusable(interfaceFor, params);
        } catch (Exception e) {
            LGR.error(Functions.stackTraceToString(e));
        }
        return null;
    }

    @Override
    public <T extends Renewable<?>> T getRenewable(Class<T> interfaceFor, Object... params) {
        try {
            return (T) createReusable(interfaceFor, params);
        } catch (Exception e) {
            LGR.error(Functions.stackTraceToString(e));
        }
        return null;
    }

    @Override
    public <T extends Mingleton> T getMingleton(Class<T> interfaceFor, Object... params) {
        try {
            return (T) createReusable(interfaceFor, params);
        } catch (Exception e) {
            LGR.error(Functions.stackTraceToString(e));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Renewable<?>> T renew(Class<T> interfaceFor, Object... params) {
        T t = this.getRenewable(interfaceFor, params);
        return (T) t.renew(params);
    }

    @Override
    public <T extends Singleton> T getSingleton(Class<T> interfaceFor) {
        try {
            return (T) createReusable(interfaceFor);
        } catch (Exception e) {
            LGR.error(Functions.stackTraceToString(e));
        }
        return null;
    }
}
