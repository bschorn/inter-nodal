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

import java.util.concurrent.ExecutorService;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.server.ActiveServer.ServerEvent;
import org.schorn.ella.services.ContentAPI;
import org.schorn.ella.services.ContentFormatter;

/**
 *
 * @author schorn
 *
 */
public interface AdminServer {

    /**
     * Configuration for AdminServer
     */
    public enum Cfg {
        MAX_IO_THREADS(Integer.class, ServerConfig.ADMIN_SERVER_CFG_MAX_IO_THREADS.asClassType(Integer.class)),;

        Class<?> valueClass;
        Object value;

        Cfg(Class<?> classFor, Object value) {
            this.valueClass = classFor;
            this.value = value;
        }

        public Class<?> valueClass() {
            return this.valueClass;
        }

        public Object valueAsObject() {
            return this.value;
        }

        @SuppressWarnings("unchecked")
        public <T> T valueAs(Class<T> classForT) {
            return (T) this.value;
        }
    }

    static public String[] getActiveServers() {
        return ServerConfig.ACTIVE_SERVERS.asArray(",");
    }

    ExecutorService executorForIO();

    void initServers() throws Exception;

    void initApplets() throws Exception;

    void startApplets() throws Exception;

    void stopApplets() throws Exception;

    ContentAPI getContentAPI(String context_server);

    ContentFormatter getContentFormatter(String context_server);

    void event(ServerEvent event);

    ArrayData status();

    static AdminServer instance() {
        return ServerProvider.provider().getAdminServer();
    }

}
