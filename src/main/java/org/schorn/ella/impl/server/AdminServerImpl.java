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
package org.schorn.ella.impl.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.server.ActiveServer;
import org.schorn.ella.server.ActiveServer.AppletState;
import org.schorn.ella.server.ActiveServer.ContextApplet;
import org.schorn.ella.server.ActiveServer.ContextServer;
import org.schorn.ella.server.ActiveServer.ServerEvent;
import org.schorn.ella.services.ContentAPI;
import org.schorn.ella.services.ContentFormatter;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class AdminServerImpl implements ActiveServer.AdminServer {

    static final Logger LGR = LoggerFactory.getLogger(AdminServerImpl.class);

    private final Integer ioMaxThreads;
    private final ExecutorService ioThreadPool;
    private final CopyOnWriteArrayList<ContextServer> activeServers = new CopyOnWriteArrayList<>();
    private final List<ContextApplet> applets = new ArrayList<>();

    public AdminServerImpl() {
        this.ioMaxThreads = ActiveServer.Config.get().maxIOThreads();
        this.ioThreadPool = Executors.newFixedThreadPool(this.ioMaxThreads);
    }

    void add(ContextServer activeServer) {
        this.activeServers.add(activeServer);
    }

    void del(ContextServer activeServer) {
        this.activeServers.remove(activeServer);
    }

    List<ContextServer> servers() {
        return Collections.unmodifiableList(this.activeServers);
    }

    List<ContextApplet> applets() {
        return Collections.unmodifiableList(this.applets);
    }

    @Override
    public ExecutorService executorForIO() {
        return this.ioThreadPool;
    }

    @Override
    public void initServers() throws Exception {
        for (Class<?> activeCustomizationClass : ActiveServer.Config.get().servers()) {
            try {
                if (ContextServer.class.isAssignableFrom(activeCustomizationClass)) {
                    ContextServer activeServer
                            = (ContextServer) activeCustomizationClass.getDeclaredConstructor().newInstance();
                    if (activeServer != null) {
                        LGR.info("{}.initServers() - adding ContextServer: -> {}",
                                this.getClass().getSimpleName(),
                                activeServer.name());
                        try {
                            this.add(activeServer);
                            activeServer.init();
                        } catch (Exception ex) {
                            LGR.error("{}.load() - failed on ContextServer.init() -> {}. Exception: {}",
                                    this.getClass().getSimpleName(),
                                    activeServer.name(), Functions.getStackTraceAsString(ex));
                            this.del(activeServer);
                        }
                    }
                }
            } catch (Exception ex) {
                LGR.error("{}.initServers() - failed to load class '{}'. Exception: {}",
                        this.getClass().getSimpleName(),
                        activeCustomizationClass.getSimpleName(),
                        Functions.getStackTraceAsString(ex));
            }
        }
        for (ContextServer activeServer : this.servers()) {
            try {
                activeServer.postInit();
            } catch (Exception ex) {
                LGR.error("{}.initServers() - failed on ContextServer.postInit() -> {}. Exception: {}",
                        this.getClass().getSimpleName(),
                        activeServer.name(), Functions.getStackTraceAsString(ex));
                this.del(activeServer);
            }
        }
    }

    @Override
    public void initApplets() throws Exception {
        for (ContextServer activeServer : this.servers()) {
            for (ContextApplet applet : activeServer.applets()) {
                try {
                    LGR.info("{}.initApplets() - calling ContextApplet.init() -> {}", this.getClass().getSimpleName(), applet.toString());
                    applet.init();
                    LGR.info("{}.initApplets() - ContextApplet initialized -> {}", this.getClass().getSimpleName(), applet.toString());
                } catch (Exception ex) {
                    applet.update(ex);
                    LGR.error("{}.initApplets() - failed on ContextApplet.init() -> {}", this.getClass().getSimpleName(), applet.toString(), Functions.stackTraceToString(ex));
                }
            }
        }

    }

    @Override
    public void startApplets() throws Exception {
        for (ContextServer activeServer : this.servers()) {
            for (ContextApplet applet : activeServer.applets()) {
                if (applet.exception() != null) {
                    continue;
                }
                if (applet.state().equals(AppletState.AVAILABLE)) {
                    try {
                        switch (applet.type()) {
                            case BATCH_JOB:
                            case EVENT_DRIVEN:
                            case DEAMON_SERVICE:
                                LGR.info("{}.startApplets() - calling ActiveApplet.start() -> {}",
                                        this.getClass().getSimpleName(),
                                        applet.toString());
                                applet.start();
                                break;
                            default:
                                break;
                        }
                    } catch (Exception ex) {
                        applet.update(ex);
                        LGR.error("{}.startApplets() - failed on ActiveApplet.start() for: {}",
                                this.getClass().getSimpleName(),
                                applet.toString(), Functions.getStackTraceAsString(ex));

                    }
                }
            }
        }
    }

    @Override
    public void stopApplets() throws Exception {
        this.activeServers.stream().forEach(svr -> svr.applets().stream().filter(ap -> ap.state().equals(AppletState.RUNNING)).forEach(ap -> ap.stop()));
    }

    @Override
    public void event(ServerEvent event) {
    }

    @Override
    public ArrayData status() {
        return null;
    }

    @Override
    public ContentAPI getContentAPI(String context_server) {
        Optional<ContextServer> optContextServer = this.activeServers.stream()
                .filter(asvr -> asvr.name().equals(context_server))
                .findAny();
        if (optContextServer.isPresent()) {
            return optContextServer.get().contentAPI();
        }
        return null;
    }

    @Override
    public ContentFormatter getContentFormatter(String context_server) {
        Optional<ContextServer> optContextServer = this.activeServers.stream()
                .filter(asvr -> asvr.name().equals(context_server))
                .findAny();
        if (optContextServer.isPresent()) {
            return optContextServer.get().contentFormatter();
        }
        return null;
    }

}
