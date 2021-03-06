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
package org.schorn.ella.app;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.schorn.ella.Component;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.MetaReader;
import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.server.ActiveServer;
import org.schorn.ella.util.CommandLineArgs;
import org.schorn.ella.util.Functions;
import org.schorn.ella.util.StringCached;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public final class ActiveApp {

    static final Logger LGR = LoggerFactory.getLogger(ActiveApp.class);

    static private AppConfig APP_CONFIG = null;

    public interface Config {

        static public Config get() {
            return APP_CONFIG;
        }

        LocalDate date();

        String environment();

        String language();

        String context();

        List<String> contexts();

        String rootPath();

        String configPath();

    }

    private final Map<String, MetaReader.MetaSupplier> metaSuppliersMap = new HashMap<>();

    public void addContext(String context, MetaReader.MetaSupplier metaSupplier) {
        if (!Config.get().contexts().contains(context)) {
            LGR.error("{}.addContext() - context was not configured: {}.",
                    this.getClass().getSimpleName(),
                    context);
        }
        if (this.metaSuppliersMap.containsKey(context)) {
            LGR.info("{}.addContext() - replacing MetaReader.MetaSupplier for context: '{}'",
                    ActiveApp.class.getSimpleName(),
                    context);
        }
        this.metaSuppliersMap.put(context, metaSupplier);
    }

    /**
     * For any static dependencies that require specific order of creation.
     *
     */
    private void initStatic() {
        //AppContext context = AppContext.Common;
        StringCached.initialize();
        MetaTypes.initialize();
        //MapTypes.initialize();
    }

    /**
     * Standard Default Values
     */
    private void initDefaultValues() {
        NodeProvider.provider().setDefaultValue(MetaTypes.ValueTypes.idata_uuid.valueType(), AvailableActions.DefaultActions.AUTO_UUID);
        NodeProvider.provider().setDefaultValue(MetaTypes.ValueTypes.idata_cts.valueType(), AvailableActions.DefaultActions.AUTO_TS);
        NodeProvider.provider().setDefaultValue(MetaTypes.AutoTypes.octs.valueType(), AvailableActions.DefaultActions.AUTO_TS);
        NodeProvider.provider().setDefaultValue(MetaTypes.AutoTypes.over.valueType(), AvailableActions.DefaultActions.AUTO_VERSION);
        NodeProvider.provider().setDefaultValue(MetaTypes.AutoTypes.okey.valueType(), AvailableActions.DefaultActions.AUTO_KEY);
    }

    private void initMeta() {
        for (String context : Config.get().contexts()) {
            URI metaURI = ActiveNode.Config.get(context).metadata();
            if (this.metaSuppliersMap.containsKey(context)) {
                continue;
            }
            try {
                Path metaPath = Paths.get(metaURI);
                if (Files.exists(metaPath)) {
                    this.metaSuppliersMap.put(context, new MetaReader.FileMetaSupplier(metaPath));
                    LGR.info("{}.initMeta() - added context '{}' -> '{}'",
                            ActiveApp.class.getSimpleName(),
                            context,
                            metaPath.toAbsolutePath().toString());
                } else {
                    LGR.error("{}.meta() - File does not exist: '{}'",
                            ActiveApp.class.getSimpleName(),
                            metaPath.toString());
                }
            } catch (Exception ex) {
                LGR.error("{}.meta() - Loading Meta File: '{}' caught Exception: {}",
                        ActiveApp.class.getSimpleName(), metaURI.toString(),
                        Functions.getStackTraceAsString(ex));
            }
        }
    }

    /*
    private void initLabels() throws Exception {
        String language = AppConfig.LANGUAGE.asString();
        if (language == null) {
            LGR.error("{}.initLabels() - there is no Active.Lang specified.",
                    this.getClass().getSimpleName());
            language = "en-US";
        }
        this.activeLang = ActiveLang.create(language);
        for (AppContext context : AppContext.values()) {
            if (context.hasRepo()) {
                List labelUrls = (List) AppConfig.ACTIVE_LABEL.asNaturalType();
                for (Object ourl : labelUrls) {
                    if (ourl instanceof URL) {
                        URL url = (URL) ourl;
                        try {
                            this.activeLang.loadLabels(context, url);
                        } catch (Exception ex3) {
                            LGR.error("{}.initLabels() - {}",
                                    this.getClass().getSimpleName(),
                                    Functions.stackTraceToString(ex3));
                        }
                    }
                }
            }
        }
    }
     */

    public void start(String[] args) throws Exception {
        AppContext.meta(Collections.unmodifiableMap(this.metaSuppliersMap));
        //this.initLabels();
        AppContext.recover();
        ActiveServer.AdminServer.instance().initServers();
        ActiveServer.AdminServer.instance().initApplets();
        AppContext.record();
        ActiveServer.AdminServer.instance().startApplets();
    }

    private void initAppConfig() throws Exception {
        APP_CONFIG = AppConfig.create(Component.ActiveApp.configMap());
    }

    private ActiveApp(String[] args) throws Exception {
        Component.bootstrap(CommandLineArgs.init(args).getProperties());
        this.initAppConfig();
        Component.init();
        //this.initActivityCfg();
        this.initStatic();
        this.initDefaultValues();
        this.initMeta();
    }

    /**
     *
     */
    static public class Starter {

        private static volatile ActiveApp APP = null;
        static private final Object LOCK = new Object();
        private final String[] args;

        public Starter(String[] args) {
            this.args = args;
        }

        public Starter create() throws Exception {
            ActiveApp app = Starter.APP;
            if (app == null) {
                synchronized (LOCK) {
                    app = Starter.APP;
                    if (app == null) {
                        Starter.APP = app = new ActiveApp(this.args);
                    }
                }
            }
            return this;
        }

        public void start() throws Exception {
            APP.start(this.args);
        }

        public ActiveApp get() {
            return APP;
        }
    }

    /**
     * MAIN (for test & development)
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            ActiveApp.Starter appStarter = new ActiveApp.Starter(args);
            appStarter.create();
            appStarter.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}


/*
public void stop() {
    for (AppContext context : AppContext.values()) {
            //context.close();
            EventLogBroker eventLogBroker = RepoProvider.provider().getContextEventLogBroker(context);
            try {
                    eventLogBroker.close();
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }
    for (AppContext context : AppContext.values()) {
            EventLogBroker eventLogBroker = RepoProvider.provider().getContextEventLogBroker(context);
            try {
                    eventLogBroker.thread().join();
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }
}

*/
