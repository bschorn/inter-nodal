/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.schorn.ella.Component;
import org.schorn.ella.server.ActiveServer;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bschorn
 */
public class ServerConfigImpl implements ActiveServer.Config {

    private static final Logger LGR = LoggerFactory.getLogger(ServerConfigImpl.class);

    private List<Class<?>> servers = null;
    private boolean masterServer = false;
    private URI masterServerAddress = null;
    private int maxIOThreads = 5;

    public ServerConfigImpl() throws ClassNotFoundException, URISyntaxException {
        Map<String, Object> map = Component.ActiveServer.configMap();
        if (map.containsKey("servers")) {
            List<String> list = (List<String>) map.get("servers");
            this.servers = new ArrayList<>();
            for (String className : list) {
                try {
                    this.servers.add(Class.forName(className));
                } catch (Exception ex) {
                    LGR.error("{}.ctor() - className: {} - Caught Exception: {}",
                            this.getClass().getSimpleName(),
                            className == null ? "null" : className,
                            Functions.stackTraceToString(ex));
                }
            }
        }
        if (map.get("masterServer") != null) {
            this.masterServer = (Boolean) map.get("masterServer");
        }
        if (map.get("masterServerAddress") != null) {
            this.masterServerAddress = new URI((String) map.get("masterServerAddress"));
        }
        if (map.get("maxIOThreads") != null) {
            this.maxIOThreads = (Integer) map.get("maxIOThreads");
        }
    }

    @Override
    public List<Class<?>> servers() {
        return this.servers;
    }

    @Override
    public boolean masterServer() {
        return this.masterServer;
    }

    @Override
    public URI masterServerAddress() {
        return this.masterServerAddress;
    }

    @Override
    public int maxIOThreads() {
        return this.maxIOThreads;
    }

}
