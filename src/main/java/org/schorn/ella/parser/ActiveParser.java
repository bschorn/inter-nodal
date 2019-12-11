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
package org.schorn.ella.parser;

import java.io.DataInput;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import org.schorn.ella.Thingleton;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.OpenNode;

/**
 *
 * @author schorn
 *
 */
public interface ActiveParser {

    /**
     * ReadJson (Thingleton is a Thread-based Singleton)
     *
     */
    interface ReadJson extends Thingleton {

        OpenNode parse(File file) throws Exception;

        OpenNode parse(URL url) throws Exception;

        OpenNode parse(InputStream stream) throws Exception;

        OpenNode parse(Reader reader) throws Exception;

        OpenNode parse(byte[] data) throws Exception;

        OpenNode parse(byte[] data, int offset, int len) throws Exception;

        OpenNode parse(String content) throws Exception;

        OpenNode parse(char[] content) throws Exception;

        OpenNode parse(char[] content, int offset, int len) throws Exception;

        OpenNode parse(DataInput input) throws Exception;

        static public ReadJson get() {
            return ParserProvider.provider().getReadJson();
        }
    }

    /**
     * WriteJson (Thingleton is a Thread-based Singleton)
     *
     */
    interface WriteJson extends Thingleton {

        String produceRecord(ActiveData activeData) throws Exception;

        String produceOutput(ActiveData activeData) throws Exception;

        String produceOutput(OpenNode openNode) throws Exception;

        static public WriteJson get() {
            return ParserProvider.provider().getWriteJson();
        }
    }

    /**
     * ReadJson (Thingleton is a Thread-based Singleton)
     *
     */
    interface ReadYaml extends Thingleton {

        OpenNode parse(File file) throws Exception;

        OpenNode parse(URL url) throws Exception;

        OpenNode parse(InputStream stream) throws Exception;

        OpenNode parse(Reader reader) throws Exception;

        OpenNode parse(byte[] data) throws Exception;

        OpenNode parse(byte[] data, int offset, int len) throws Exception;

        OpenNode parse(String content) throws Exception;

        OpenNode parse(char[] content) throws Exception;

        OpenNode parse(char[] content, int offset, int len) throws Exception;

        //OpenNode parse(DataInput input) throws Exception;

        static public ReadYaml get() {
            return ParserProvider.provider().getReadYaml();
        }
    }

    /**
     * WriteJson (Thingleton is a Thread-based Singleton)
     *
     */
    interface WriteYaml extends Thingleton {

        String produceRecord(ActiveData activeData) throws Exception;

        String produceOutput(ActiveData activeData) throws Exception;

        String produceOutput(OpenNode openNode) throws Exception;
        
        
        static public WriteYaml get() {
            return ParserProvider.provider().getWriteYaml();
        }
    }

}
