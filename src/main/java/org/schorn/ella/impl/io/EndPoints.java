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
package org.schorn.ella.impl.io;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.schorn.ella.io.EndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public enum EndPoints {
    ;
    static final Logger LGR = LoggerFactory.getLogger(EndPoints.class);

    /**
     *
     * @param filename
     * @param tag
     * @return
     */
    static public EndPoint.FilePoint create(String filename, String tag) {
        return new FilePointImpl(filename, tag);
    }

    static public class URIPointImpl implements EndPoint.URIPoint {

        private final URI uri;

        public URIPointImpl(URI uri) {
            this.uri = uri;
        }

        @Override
        public boolean isReady() {
            try {
                Path path = Paths.get(this.get());
                return Files.exists(path);
            } catch (Throwable t) {
                return false;
            }
        }

        @Override
        public URI get() {
            return this.uri;
        }

        @Override
        public String tag() {
            return this.uri.getSchemeSpecificPart();
        }

    }

    /**
     *
     */
    static class FilePointImpl implements EndPoint.FilePoint {

        private final String filename;
        private final String tag;

        /**
         *
         */
        FilePointImpl(String filename, String tag) {
            this.filename = filename;
            this.tag = tag;
        }

        @Override
        public Path get() {
            return Paths.get(filename);
        }

        @Override
        public String tag() {
            return this.tag;
        }

        @Override
        public boolean isReady() {
            try {
                Paths.get(filename);
                return true;
            } catch (Throwable t) {
                return false;
            }
        }

    }

}
