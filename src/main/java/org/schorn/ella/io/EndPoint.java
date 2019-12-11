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
package org.schorn.ella.io;

import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Properties;

/**
 *
 * @author schorn
 *
 * @param <T>
 */
public interface EndPoint<T> {

    public enum Type {
        Database, URI, File, WebSocket, Http, IQ, List, Map, MQ;
    }

    public enum Purpose {
        Inflow, Outflow, Biflow;
    }

    Type type();

    boolean isReady();

    T get();

    String tag();

    /**
     *
     *
     *
     */
    public interface DatabasePoint extends EndPoint<Properties> {

        @Override
        default Type type() {
            return EndPoint.Type.Database;
        }

        @Override
        Properties get();

        /**
         *
         * @return
         */
        String server();

        String schema();

    }

    public interface URIPoint extends EndPoint<URI> {

        static public URIPoint create(URI uri) throws Exception {
            return IOProvider.provider().createInstance(URIPoint.class, uri);
        }

        @Override
        default Type type() {
            return EndPoint.Type.URI;
        }

        @Override
        URI get();

    }

    //@Deprecated
    public interface FilePoint extends EndPoint<Path> {

        static public FilePoint create(String filename, String tag) throws Exception {
            return IOProvider.provider().createInstance(FilePoint.class, filename, tag);
        }

        @Override
        default Type type() {
            return EndPoint.Type.File;
        }

        @Override
        Path get();

    }

    @Deprecated
    public interface WebSocketPoint extends EndPoint<URL> {

        @Override
        default Type type() {
            return EndPoint.Type.WebSocket;
        }

        @Override
        URL get();
    }

    @Deprecated
    public interface HttpPoint extends EndPoint<URL> {

        @Override
        default Type type() {
            return EndPoint.Type.Http;
        }

        @Override
        URL get();
    }

}
