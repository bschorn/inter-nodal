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

import org.schorn.ella.context.AppContext;
import org.schorn.ella.server.ActiveServer.AppletMethod;
import org.schorn.ella.server.ActiveServer.AppletState;
import org.schorn.ella.server.ActiveServer.AppletTrigger;
import org.schorn.ella.server.ActiveServer.AppletType;
import org.schorn.ella.server.ActiveServer.ContextApplet;
import org.schorn.ella.server.ActiveServer.ContextServer;

/**
 *
 * @author schorn
 *
 */
public abstract class AbstractApplet implements ContextApplet {

    private final ContextServer server;
    private AppletState state = AppletState.UNAVAILABLE;
    private AppletType type = AppletType.UNINITIALIZED;
    private AppletTrigger trigger = null;
    private AppletMethod method = AppletMethod.NA;
    private Exception exception = null;

    public AbstractApplet(ContextServer server, Object... params) {
        this.server = server;
        for (Object param : params) {
            if (param instanceof AppletState) {
                this.state = (AppletState) param;
            } else if (param instanceof AppletType) {
                this.type = (AppletType) param;
            } else if (param instanceof AppletTrigger) {
                this.trigger = (AppletTrigger) param;
            } else if (param instanceof AppletMethod) {
                this.method = (AppletMethod) param;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s): [state] %s [type] %s [method] %s [trigger] %s [exception] %s",
                this.name(),
                this.server != null ? this.server.name() : "null",
                this.state != null ? this.state.name() : "null",
                this.type != null ? this.type.name() : "null",
                this.method != null ? this.method.name() : "null",
                this.trigger != null ? this.trigger.name() : "null",
                this.exception() == null ? "none" : this.exception().getMessage());
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public AppContext context() {
        return this.server.context();
    }

    @Override
    public ContextServer server() {
        return this.server;
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
    public AppletTrigger trigger() {
        return this.trigger;
    }

    @Override
    public AppletMethod method() {
        return this.method;
    }

    @Override
    public Exception exception() {
        return this.exception;
    }

    @Override
    public void update(AppletState state) {
        this.state = state;
    }

    /**
     *
     * @param type
     */
    @Override
    public void update(AppletType type) {
        this.type = type;
    }

    @Override
    public void update(AppletMethod method) {
        this.method = method;
    }

    @Override
    public void update(AppletTrigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public void update(Exception exception) {
        this.exception = exception;
    }

    @Override
    public Object getProperty(Object propertyKey) {
        return null;
    }

    @Override
    public void start() {
    }

    @Override
    public void suspend() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void stop() {
    }

    @Override
    public void kill() {
        this.stop();
    }

    @Override
    public void restart() {
        this.stop();
        this.start();
    }

}
