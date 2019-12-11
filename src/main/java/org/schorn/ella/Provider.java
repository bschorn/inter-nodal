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
package org.schorn.ella;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.context.ContextProvider;
import org.schorn.ella.error.ErrorProvider;
import org.schorn.ella.event.EventProvider;
import org.schorn.ella.html.HtmlProvider;
import org.schorn.ella.http.HTTPProvider;
import org.schorn.ella.io.IOProvider;
import org.schorn.ella.load.LoadProvider;
import org.schorn.ella.model.ModelProvider;
import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.parser.ParserProvider;
import org.schorn.ella.repo.RepoProvider;
import org.schorn.ella.server.ServerProvider;
import org.schorn.ella.services.ServicesProvider;
import org.schorn.ella.sql.SQLProvider;
import org.schorn.ella.transform.TransformProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider Interface
 *
 * @author schorn
 *
 */
public interface Provider {

    static final Logger LGR = LoggerFactory.getLogger(Provider.class);

    /**
     *
     */
    static final List<Providers> PROVIDERS = new ArrayList<>();

    public enum Providers {
        CONTEXT(ContextProvider.class, true),
        META(NodeProvider.class, false),
        TRANSFORM(TransformProvider.class, false),
        PARSER(ParserProvider.class, false),
        ERROR(ErrorProvider.class, false),
        EVENT(EventProvider.class, true),
        IO(IOProvider.class, true),
        REPO(RepoProvider.class, true),
        SERVER(ServerProvider.class, true),
        LOAD(LoadProvider.class, true),
        API(ServicesProvider.class, false),
        SQL(SQLProvider.class, false),
        HTML(HtmlProvider.class, false),
        HTTP(HTTPProvider.class, false),
        MODEL(ModelProvider.class, false);

        Class<? extends Provider> interfaceClass;
        private Provider instance;
        private boolean dataProvider;

        Providers(Class<? extends Provider> interfaceClass, boolean dataProvider) {
            this.interfaceClass = interfaceClass;
            this.dataProvider = dataProvider;
            String[] parts = this.interfaceClass.getName().split("\\.");
            StringJoiner implPath = new StringJoiner(".", "", "Impl");
            for (int i = 0; i < parts.length; i += 1) {
                if (i == parts.length - 2) {
                    implPath.add("impl");
                }
                implPath.add(parts[i]);
            }
            /*
            * By default we expect the providers to be at a class path relative to 
            * their interface: a.b.c.Provider -> a.b.impl.c.ProviderImpl
            * If the property has already been set (overridden) then we use it,
            * otherwise we default to implPath
            */
            String classPath = System.getProperty(this.interfaceClass.getSimpleName(), implPath.toString());
            System.setProperty(this.interfaceClass.getSimpleName(), classPath);
            try {
                this.instance = ComponentProperties.PROVIDER.newInstance(interfaceClass);
                if (this.instance != null) {
                    this.instance.init();
                    PROVIDERS.add(this);
                } else {
                    LGR.error("{}.ctor() - {} has no implementation at: '{}'",
                            this.getClass().getSimpleName(), this.name(),
                            System.getProperty(this.interfaceClass.getSimpleName(), "no path specified"));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(100);
            }
        }

        @SuppressWarnings("unchecked")
        public <T> T getInstance(Class<T> interfaceClass) {
            return (T) this.instance;
        }

        public boolean dataProvider() {
            return this.dataProvider;
        }
    }

    /**
     * Initialization of the Provider
     *
     * The implementations for the interfaces of this Provider are determined
     * and stored.
     *
     * init() is called only once.
     *
     *
     * @throws Exception
     */
    void init() throws Exception;

    /**
     * Register Context with Provider
     *
     * The instances for NodeContext are created here so issues can be handled
     * before we go to far.
     *
     * registerContext() is called only once per NodeContext.
     *
     * @param context
     */
    void registerContext(AppContext context) throws Exception;

    /**
     * Instance Creator
     *
     * @param interfaceFor
     * @param params
     * @return
     * @throws Exception
     */
    <T> T createInstance(Class<T> interfaceFor, Object... params) throws Exception;

    /**
     * Reusable Instances (Mingleton)
     *
     * This should only be used with stateless/thread-safe objects.
     *
     * Multiple Singletons - Same class but different constructor parameters.
     *
     * This maintains a single instance for each parameter list.
     *
     * Car RED1 = Provider.reuseInstance(Car.class, Color.RED); Car RED2 =
     * Provider.reuseInstance(Car.class, Color.RED); RED1 == RED2 Car BLUE =
     * Provider.reuseInstance(Car.class, Color.BLUE); RED1 != BLUE
     *
     * @param interfaceFor
     * @param params
     * @return
     * @throws Exception
     */
    <T> T createReusable(Class<T> interfaceFor, Object... params) throws Exception;

    /**
     * This is the same as createReusable() except it will not throw. It will
     * return a null value instead.
     *
     * Use this method when you are certain that the implementation has already
     * been located and there will not be any NoClassFoundException(s).
     *
     *
     * @param interfaceFor
     * @param params
     * @return
     *
     */
    <T> T getReusable(Class<T> interfaceFor, Object... params);

    /**
     * This is the same as getReusable() except it will only accept requests for
     * implementers of Renewable interface.
     *
     * Use this method when you know can't reuse this class but it did implement
     * Renewable so it get you a new one without using reflection. So it's still
     * safe to believe there will be no throws.
     *
     *
     * @param interfaceFor
     * @param params
     * @return
     *
     */
    <T extends Renewable<?>> T getRenewable(Class<T> interfaceFor, Object... params);

    <T extends Renewable<?>> T renew(Class<T> interfaceFor, Object... params);

    <T extends Mingleton> T getMingleton(Class<T> interfaceFor, Object... params);

    <T extends Singleton> T getSingleton(Class<T> interfaceFor);

    /**
     * Allows the wiring between Interface and Implementation to be done
     * programmatically rather than configuration
     *
     * @param interfaceFor
     * @param implFor
     */
    void mapInterfaceToImpl(Class<?> interfaceFor, Class<?> implFor);

}
