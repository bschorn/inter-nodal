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
package org.schorn.ella.server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.MapReader;
import org.schorn.ella.node.MetaReader;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public interface AppServer {

    static final Logger LGR = LoggerFactory.getLogger(AppServer.class);

    static public String getProperty(AppContext activeContext, String propertyKey) {
        for (AppServer activeServer : Support.instance.servers()) {
            if (activeContext.equals(activeServer.context())) {
                if (activeContext.hasRepo()) {
                    String propertyValue = activeServer.getProperty(propertyKey);
                    if (propertyValue == null) {
                        LGR.error("PropertyKey {} was not set in context: {}", propertyKey, activeContext.name());
                    }
                    return propertyValue;
                }
            }
        }
        //LGR.error("PropertyKey {} was not set because context: {} was not found", propertyKey, activeContext.getName());
        return null;
    }

    /**
     *
     *
     */
    static class Support {

        static final Support instance = new Support();
        private final ExecutorService ioThreadPool = Executors.newFixedThreadPool(1);

        private final CopyOnWriteArrayList<AppServer> activeServers = new CopyOnWriteArrayList<>();
        private final List<AppServer.ActiveApplet> applets = new ArrayList<>();

        void add(AppServer activeServer) {
            this.activeServers.add(activeServer);
        }

        void del(AppServer activeServer) {
            this.activeServers.remove(activeServer);
        }

        List<AppServer> servers() {
            return Collections.unmodifiableList(this.activeServers);
        }

        List<ActiveApplet> applets() {
            return Collections.unmodifiableList(this.applets);
        }
    }

    /**
     *
     * @throws Exception
     */
    static public void load() throws Exception {
        String activeServers = ServerConfig.ACTIVE_SERVERS.value();
        String[] classNames = activeServers.split(",");
        for (String className : classNames) {
            try {
                if (className != null) {
                    Class<?> activeCustomizationClass = Class.forName(className);
                    if (AppServer.class.isAssignableFrom(activeCustomizationClass)) {
                        AppServer activeServer = (AppServer) activeCustomizationClass.newInstance();
                        if (activeServer != null) {
                            LGR.info("{}.load() - adding ActiveServer: -> {}",
                                    AppServer.class.getSimpleName(),
                                    activeServer.name());
                            try {
                                Support.instance.add(activeServer);
                                LGR.info("{}.load() - calling ActiveServer.init() -> {}",
                                        AppServer.class.getSimpleName(),
                                        activeServer.name());
                                activeServer.init();
                                LGR.info("{}.load() - accepting ActiveServer -> {}",
                                        AppServer.class.getSimpleName(),
                                        activeServer.name());
                            } catch (Exception ex) {
                                LGR.error("{}.load() - failed on ActiveServer.init() -> {}. Exception: {}",
                                        AppServer.class.getSimpleName(),
                                        activeServer.name(), Functions.getStackTraceAsString(ex));
                                Support.instance.del(activeServer);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                LGR.error("{}.load() - failed to load class '{}'. Exception: {}",
                        AppServer.class.getSimpleName(),
                        className, Functions.getStackTraceAsString(ex));
            }
        }
        for (AppServer activeServer : Support.instance.servers()) {
            try {
                activeServer.postInit();
            } catch (Exception ex) {
                LGR.error("{}.load() - failed on ActiveServer.postInit() -> {}. Exception: {}",
                        AppServer.class.getSimpleName(),
                        activeServer.name(), Functions.getStackTraceAsString(ex));
                Support.instance.del(activeServer);
            }
        }
        for (AppServer activeServer : Support.instance.servers()) {
            for (ActiveApplet applet : activeServer.applets()) {
                try {
                    LGR.info("{}.load() - calling ActiveApplet.init() -> {}", AppServer.class.getSimpleName(), applet.toString());
                    applet.init();
                    LGR.info("{}.load() - ActiveApplet initialized -> {}", AppServer.class.getSimpleName(), applet.toString());
                } catch (Exception ex) {
                    applet.update(ex);
                    LGR.error("{}.load() - failed on ActiveApplet.init() -> {}", AppServer.class.getSimpleName(), applet.toString(), Functions.stackTraceToString(ex));
                }
            }
        }
        for (AppServer activeServer : Support.instance.servers()) {
            for (ActiveApplet applet : activeServer.applets()) {
                if (applet.exception() != null) {
                    continue;
                }
                if (applet.state().equals(ActiveApplet.AppletState.AVAILABLE)) {
                    try {
                        switch (applet.type()) {
                            case BATCH_SOD:
                            case INTERACTIVE:
                                LGR.info("{}.load() - calling ActiveApplet.start() -> {}",
                                        AppServer.class.getSimpleName(),
                                        applet.toString());
                                applet.start();
                                break;
                            default:
                                LGR.info("{}.load() - doing nothing -> {}",
                                        AppServer.class.getSimpleName(),
                                        applet.toString());
                                break;
                        }
                    } catch (Exception ex) {
                        applet.update(ex);
                        LGR.error("{}.load() - failed on ActiveApplet.start() for: {}",
                                AppServer.class.getSimpleName(),
                                applet.toString(), Functions.getStackTraceAsString(ex));

                    }
                }
            }
        }
    }

    /**
     *
     * @param activeContext
     */
    static public void notifyRecovered(AppContext activeContext) {
        LGR.info("{}.notifyRecovered() - ActiveContext: {}", AppServer.class.getSimpleName(), activeContext.name());
        for (AppServer activeServer : Support.instance.servers()) {
            if (!activeServer.context().equals(activeContext)) {
                continue;
            }
            for (ActiveApplet applet : activeServer.applets()) {
                if (applet.exception() != null) {
                    continue;
                }
                if (applet.state().equals(ActiveApplet.AppletState.AVAILABLE)) {
                    try {
                        switch (applet.type()) {
                            case BATCH_EOD:
                            case INTERACTIVE:
                                LGR.info("{}.load() - calling ActiveApplet.start() -> {}",
                                        AppServer.class.getSimpleName(),
                                        applet.toString());
                                applet.start();
                                break;
                            default:
                                LGR.info("{}.load() - doing nothing -> {}",
                                        AppServer.class.getSimpleName(),
                                        applet.toString());
                                break;
                        }
                    } catch (Exception ex) {
                        applet.update(ex);
                        LGR.error("{}.load() - failed on ActiveApplet.start() for: {}",
                                AppServer.class.getSimpleName(),
                                applet.toString(), Functions.getStackTraceAsString(ex));

                    }
                }
            }
        }
    }

    /**
     * Executor Service for all tasks.
     *
     * @return
     */
    static public ExecutorService executorService() {
        return Support.instance.ioThreadPool;
    }

    /**
     *
     * @throws Exception
     */
    void init() throws Exception;

    void postInit() throws Exception;

    AppContext context();

    String name();

    List<ActiveApplet> applets();

    String getProperty(String propertyKey);

    /**
     *
     * @throws Exception
     */
    default void registerMeta(String metaFile) throws Exception {
        if (metaFile == null) {
            throw new Exception(String.format("%s.registerMeta() - metaDataFileName() returned null.",
                    this.getClass().getSimpleName()));
        }
        Path metaFilePath = Paths.get(metaFile);
        if (!Files.exists(metaFilePath)) {
            throw new Exception(String.format("%s.registerMeta() - meta-data file %s does not exist.",
                    this.getClass().getSimpleName(),
                    metaFile));
        }
        MetaReader metaReader = MetaReader.createFromFile(metaFilePath);
        if (metaReader != null) {
            metaReader.register();
            for (ObjectType objectType : metaReader.context().objectTypes()) {
                System.out.println(objectType.toString());
            }
        } else {
            throw new Exception(String.format("%s.registerMeta() - MetaReader.createFromFile() returned null.",
                    this.getClass().getSimpleName()));
        }
    }

    /**
     *
     * @param mapFile
     * @throws Exception
     */
    default void registerMap(String mapFile) throws Exception {
        if (mapFile == null) {
            throw new Exception(String.format("%s.registerMap() - mapFile() returned null.",
                    this.getClass().getSimpleName()));
        }
        Path mapFilePath = Paths.get(mapFile);
        if (!Files.exists(mapFilePath)) {
            throw new Exception(String.format("%s.registerMap() - meta-data file %s does not exist.",
                    this.getClass().getSimpleName(),
                    mapFile));
        }
        MapReader mapReader = MapReader.createFromFile(mapFilePath);
        if (mapReader != null) {
            mapReader.register();
        } else {
            throw new Exception(String.format("%s.registerMap() - MetaReader.createFromFile() returned null.",
                    this.getClass().getSimpleName()));
        }
    }

    /**
     * Applet Interface
     *
     */
    interface ActiveApplet {

        public enum AppletState {
            UNAVAILABLE,
            AVAILABLE,
            INITIALIZING,
            RUNNING,
            STOPPED,
            STOPPING,
            KILLED,;
        }

        public enum AppletType {

            /**
             *
             */
            DEAMON,
            INTERACTIVE,
            BATCH_SOD,
            BATCH_EOD,
            NA,;
        }

        public enum AppletRunMode {
            FOREGROUND,
            BACKGROUND,
            NA,;
        }

        void init();

        String name();

        AppServer server();

        AppletState state();

        AppletType type();

        AppletRunMode mode();

        boolean start();

        boolean stop();

        boolean kill();

        Exception exception();

        boolean update(AppletState state);

        boolean update(AppletRunMode mode);

        boolean update(AppletType type);

        boolean update(Exception exception);

        default boolean restart() {
            this.stop();
            while (!this.state().equals(AppletState.STOPPED)) {
                if (this.state().equals(AppletState.STOPPING)) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    return false;
                }
            }
            return this.start();
        }

    }

    /**
     *
     * Abstract Implementation (available for App use but required)
     *
     */
    static public abstract class AbstractActiveApplet implements ActiveApplet {

        private AppletState state = AppletState.UNAVAILABLE;
        private AppletRunMode mode = AppletRunMode.NA;
        private AppletType type = AppletType.NA;
        private Exception exception = null;

        @Override
        public String toString() {
            return String.format("%s (%s): [type] %s [mode] %s [state] %s [exception] %s",
                    this.name(),
                    this.server().name(),
                    this.type.name(),
                    this.mode.name(),
                    this.state.name(),
                    this.exception() == null ? "none" : this.exception().getMessage());
        }

        private AppServer activeServer;

        public AbstractActiveApplet(AppServer activeServer) {
            this.activeServer = activeServer;
        }

        @Override
        public String name() {
            return this.getClass().getSimpleName();
        }

        @Override
        public AppServer server() {
            return this.activeServer;
        }

        @Override
        public AppletState state() {
            return this.state;
        }

        @Override
        public AppletType type() {
            return this.type;
        }

        @Override
        public AppletRunMode mode() {
            return this.mode;
        }

        @Override
        public Exception exception() {
            return this.exception;
        }

        @Override
        public boolean update(AppletState state) {
            this.state = state;
            return true;
        }

        @Override
        public boolean update(AppletRunMode mode) {
            this.mode = mode;
            return true;
        }

        @Override
        public boolean update(AppletType type) {
            this.type = type;
            return true;
        }

        @Override
        public boolean update(Exception exception) {
            this.exception = exception;
            return true;
        }
    }

}
