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

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.services.ContentAPI;
import org.schorn.ella.services.ContentFormatter;

/**
 *
 * @author schorn
 *
 */
public interface ActiveServer {

    public enum ServerEvent {
        RECO,
        SOD,
        OPEN,
        CLOSE,
        EOD
    }

    /**
     * Executor Service for all tasks.
     *
     * @return
     */
    static public ExecutorService executorService() {
        return AdminServer.instance().executorForIO();
    }

    /**
     *
     * @throws Exception
     */
    static public void load() throws Exception {
        AdminServer.instance().initServers();
    }

    /**
     * Usually, a context will have one server and a server will have one
     * context. But this is not a rule.
     *
     */
    interface ContextServer {

        void init() throws Exception;

        void postInit() throws Exception;

        String name();

        AppContext context();

        List<ContextApplet> applets();

        Object getProperty(Object propertyKey);

        ContentAPI contentAPI();

        ContentFormatter contentFormatter();

    }

    /**
     * A server will have one or more applets. Generally, applets are
     * independent of each other but dependent on the context and the server.
     */
    interface ContextApplet {

        void init() throws Exception;

        String name();

        AppContext context();

        /**
         *
         * @return
         */
        ContextServer server();

        /**
         *
         * @param propertyKey
         * @return
         */
        Object getProperty(Object propertyKey);

        AppletState state();

        AppletType type();

        AppletTrigger trigger();

        AppletMethod method();

        Exception exception();

        void update(AppletState state);

        void update(AppletTrigger trigger);

        void update(AppletType type);

        void update(AppletMethod method);

        void update(Exception exception);

        void start();

        void suspend();

        void resume();

        void stop();

        void kill();

        void restart();

    }

    /**
     * State
     */
    public enum AppletState {
        /**
         * applet is not ready or has been disabled
         */
        UNAVAILABLE,
        /**
         * applet is ready
         */
        AVAILABLE,
        /**
         * applet has been started but is not fully running
         */
        INITIALIZING,
        /**
         * applet is fully running
         */
        RUNNING,
        /**
         * applet has been suspended
         */
        SUSPENDED,
        /**
         * applet has been resumed
         */
        RESUMING,
        /**
         * applet is being stopped but is not fully stopped
         */
        STOPPING,
        /**
         * applet has been stopped (and will shortly become AVAILABLE)
         */
        STOPPED,
        /**
         * applet is being killed
         */
        KILLING,
        /**
         * applet has been stopped (and will become UNAVAILABLE)
         */
        KILLED,;
    }

    /**
     * Type
     */
    public enum AppletType {
        /**
         * starts, runs to completion, stops
         */
        BATCH_JOB,
        /**
         * starts, runs until stopped
         */
        DEAMON_SERVICE,
        /**
         * when started, registers with the event manager to have its code run
         * directly following an event and/or condition when stopped,
         * unregisters with the event manager
         */
        EVENT_DRIVEN,
        /**
         * applet is not ready
         */
        UNINITIALIZED,;
    }

    /**
     * Trigger
     */
    public enum AppletTrigger {
        /**
         * before recovery
         */
        PRE_RECO,
        /**
         * after recovery starts
         */
        WITH_RECO,
        /**
         * after recovery finishes
         */
        POST_RECO,
        /**
         * as part of the start-of-day process
         */
        WITH_SOD,
        /**
         * sometime before the activity window starts/opens (e.g. market hours)
         */
        WITH_OPEN,
        /**
         * sometime after the activity window ends/closes
         */
        WITH_CLOSE,
        /**
         * as part of the end-of-day process
         */
        WITH_EOD,
        /**
         * starts when externally initiated
         */
        ON_DEMAND,;
    }

    /**
     *
     */
    public enum AppletMethod {
        SERIAL,
        CONCURRENT,
        NA,;
    }

}
