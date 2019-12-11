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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.schorn.ella.Resources;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.MetaReader;
import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.node.NodeProvider;
import org.schorn.ella.server.AdminServer;
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
public final class ActiveMain {

    static final Logger LGR = LoggerFactory.getLogger(ActiveMain.class);

    private final List<String> contexts = new ArrayList<>();
    private final Map<String, MetaReader.MetaSupplier> metaSuppliersMap = new HashMap<>();

    public void addContext(String context, MetaReader.MetaSupplier metaSupplier) {
        if (!this.contexts.contains(context)) {
            this.contexts.add(context);
        }
        if (this.metaSuppliersMap.containsKey(context)) {
            LGR.info("{}.addContext() - replacing MetaReader.MetaSupplier for context: '{}'",
                    ActiveMain.class.getSimpleName(),
                    context);
        }
        this.metaSuppliersMap.put(context, metaSupplier);
    }
    /**
     *
     * @throws Exception
     */
    void initActivityCfg() throws Exception {
        String activityDate = System.getProperty("ActivityDate", DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now()));
        String activityFileName = NodeConfig.ACTIVITY_FILE.value().replace("{DATE}", activityDate);
        String activityFile = String.format("%s%s%s",
                NodeConfig.ACTIVITY_DIR.value(),
                File.separator,
                activityFileName
        );

        System.setProperty(AppContext.class.getSimpleName() + ".ActivityFile",
                activityFile);
    }

    /**
     * For any static dependencies that require specific order of creation.
     *
     */
    void initStatic() {
        //AppContext context = AppContext.Common;
        StringCached.initialize();
        MetaTypes.initialize();
        //MapTypes.initialize();
    }

    /**
     * Standard Default Values
     */
    void initDefaultValues() {
        NodeProvider.provider().setDefaultValue(MetaTypes.ValueTypes.idata_uuid.valueType(), AvailableActions.DefaultActions.AUTO_UUID);
        NodeProvider.provider().setDefaultValue(MetaTypes.ValueTypes.idata_cts.valueType(), AvailableActions.DefaultActions.AUTO_TS);
        NodeProvider.provider().setDefaultValue(MetaTypes.AutoTypes.octs.valueType(), AvailableActions.DefaultActions.AUTO_TS);
        NodeProvider.provider().setDefaultValue(MetaTypes.AutoTypes.over.valueType(), AvailableActions.DefaultActions.AUTO_VERSION);
        NodeProvider.provider().setDefaultValue(MetaTypes.AutoTypes.okey.valueType(), AvailableActions.DefaultActions.AUTO_KEY);
    }

    void initMeta() {
        for (String context : NodeConfig.ACTIVE_CONTEXTS.values(",")) {
            if (!this.contexts.contains(context)) {
                this.contexts.add(context);
            }
        }
        for (String metaFileMapEntry : NodeConfig.ACTIVE_METAS.values(",")) {
            String[] mapEntry = metaFileMapEntry.split(":");
            if (mapEntry.length == 2) {
                String context = mapEntry[0];
                String metaFile = mapEntry[1];
                if (!this.metaSuppliersMap.containsKey(context)) {
                    try {
                        Path metaPath = Paths.get(metaFile);
                        if (metaFile.startsWith(".")) {
                            metaFile = metaFile.substring(2);
                            String[] dirFiles = metaFile.split("[\\\\/]");
                            Resources resources = new Resources();
                            metaPath = resources.getResourcedPath(dirFiles);
                        }
                        if (Files.exists(metaPath)) {
                            this.metaSuppliersMap.put(context, new MetaReader.FileMetaSupplier(metaPath));
                            LGR.info("{}.initMeta() - added context '{}' -> '{}'",
                                    ActiveMain.class.getSimpleName(),
                                    context,
                                    metaPath.toAbsolutePath().toString());
                        } else {
                            LGR.error("{}.meta() - File does not exist: '{}'",
                                    ActiveMain.class.getSimpleName(),
                                    metaPath.toString());
                        }
                    } catch (Exception ex) {
                        LGR.error("{}.meta() - Loading Meta File: '{}' caught Exception: {}",
                                ActiveMain.class.getSimpleName(), metaFile, Functions.getStackTraceAsString(ex));
                    }
                }
            }
        }
    }

    /*
    public void init() throws Exception {
        this.initActivityCfg();
        this.initStatic();
        this.initDefaultValues();
        AppContext.meta();
        AppContext.recover();
        AdminServer.instance().initServers();
        AdminServer.instance().initApplets();
    }
     */

    public void start(String[] args) throws Exception {
        AppContext.meta(Collections.unmodifiableMap(this.metaSuppliersMap));
        AppContext.recover();
        AdminServer.instance().initServers();
        AdminServer.instance().initApplets();
        AppContext.record();
        AdminServer.instance().startApplets();
    }

    private ActiveMain(String[] args) throws Exception {
        new CommandLineArgs(args).loadIntoSystemProperties();
        this.initActivityCfg();
        this.initStatic();
        this.initDefaultValues();
        this.initMeta();
    }

    /**
     *
     */
    static public class Starter {

        private static volatile ActiveMain APP = null;
        static private final Object LOCK = new Object();
        private final String[] args;

        public Starter(String[] args) {
            this.args = args;
        }

        public Starter create() throws Exception {
            ActiveMain APP = Starter.APP;
            if (APP == null) {
                synchronized (LOCK) {
                    APP = Starter.APP;
                    if (APP == null) {
                        Starter.APP = APP = new ActiveMain(this.args);
                    }
                }
            }
            return this;
        }

        public void start() throws Exception {
            APP.start(this.args);
        }

        public ActiveMain get() {
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
            ActiveMain.Starter appStarter = new ActiveMain.Starter(args);
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
