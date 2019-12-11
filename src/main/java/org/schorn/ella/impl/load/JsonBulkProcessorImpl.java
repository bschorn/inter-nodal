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
package org.schorn.ella.impl.load;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.util.Functions;
import org.schorn.ella.load.ActiveObjectLoad.JsonBulkProcessor;
import org.schorn.ella.node.OpenNode;

/**
 * Reads a reader() to 'chunk' up a large JSON text string into smaller pieces.
 * It's not advisable to digest a single large JSON text. This pre-parses and
 * slices out chunks of JSON to be digested more easily.
 *
 * @author schorn
 *
 */
class JsonBulkProcessorImpl extends AbstractBulkProcessor<String, OpenNode> implements JsonBulkProcessor {

    private static final Logger LGR = LoggerFactory.getLogger(JsonBulkProcessorImpl.class);

    static private final char OPEN_ARRAY = '[';
    static private final char CLOSE_ARRAY = ']';
    static private final char OPEN_OBJECT = '{';
    static private final char CLOSE_OBJECT = '}';
    static private final char DOUBLE_QUOTE = '"';

    private int processCt = 0;
    private int limit = -1;

    public JsonBulkProcessorImpl(Reader reader, Function<String, OpenNode> function, Predicate<OpenNode> predicate, Consumer<OpenNode> consumer) {
        super(reader, function, predicate, consumer);
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     *
     *
     * @param classOfR
     * @param reader
     * @param postChunk
     * @param postFilter
     * @param consumer
     */
    public void run() {
        char arrayOrObject = '?';
        int arrayLevel = 0;
        int objectLevel = 0;
        int arrayStop = 0;
        int objectStop = 0;
        boolean inquote = false;
        int bufsize = 1000;
        char[] cbuf = new char[bufsize];
        for (int i = 0; i < bufsize; ++i) {
            cbuf[i] = 0;
        }
        try {
            Reader reader = reader();
            int pos = 0;
            int b = reader.read();
            while (b >= 0) {
                char c = (char) b;
                switch (c) {
                    case OPEN_ARRAY:
                        arrayLevel += inquote ? 0 : 1;
                        break;
                    case CLOSE_ARRAY:
                        arrayLevel += inquote ? 0 : -1;
                        break;
                    case OPEN_OBJECT:
                        objectLevel += inquote ? 0 : 1;
                        break;
                    case CLOSE_OBJECT:
                        objectLevel += inquote ? 0 : -1;
                        break;
                    case DOUBLE_QUOTE:
                        inquote = inquote ? false : true;
                        break;
                    default:
                        break;
                }
                if (arrayOrObject == '?' && arrayLevel == 1 && objectLevel == 0 && inquote == false) {
                    arrayOrObject = 'a';
                    arrayStop = 0;
                    arrayLevel = 0;
                } else if (arrayOrObject == '?' && arrayLevel == 0 && objectLevel == 1 && inquote == false) {
                    arrayOrObject = 'o';
                    objectStop = 0;
                    objectLevel = 0;
                } else if (arrayLevel < arrayStop || objectLevel < objectStop) {
                    // this means we are done.
                    b = reader.read();
                    // drain the remaining characters
                    while (b != -1) {
                        b = reader.read();
                    }
                    break;
                } else if (pos > 0 && arrayLevel == arrayStop && objectLevel == objectStop) {
                    // this means we are done with a node
                    cbuf[pos++] = c;
                    if (pos > 5) {
                        char[] node = new char[pos];
                        System.arraycopy(cbuf, 0, node, 0, pos);
                        /*
						 * 
                         */
                        process(new String(node));
                        /*
						 * 
                         */
                        if (this.limit > 0 && ++this.processCt >= this.limit) {
                            LGR.info("{}.run() - processed {} of {} limit, exiting.",
                                    this.getClass().getSimpleName(),
                                    this.processCt,
                                    this.limit);
                            return;
                        }
                    }
                    pos = 0;
                    cbuf = new char[bufsize];
                    for (int i = 0; i < bufsize; ++i) {
                        cbuf[i] = 0;
                    }
                } else if (c == '\n' || c == '\r' || c == '\t') {
                    // ignore
                } else {
                    // accumulate characters
                    cbuf[pos++] = c;
                }
                // read next character
                b = reader.read();
                // check if we need to increase the buffer size
                if (pos == bufsize) {
                    char[] ctemp = new char[bufsize];
                    System.arraycopy(cbuf, 0, ctemp, 0, bufsize);
                    cbuf = new char[bufsize * 2];
                    System.arraycopy(ctemp, 0, cbuf, 0, bufsize);
                    bufsize *= 2;
                }
            }
        } catch (IOException e) {
            try {
                reader().close();
            } catch (IOException e1) {
                LGR.error(Functions.stackTraceToString(e1));
            }
            LGR.error(Functions.stackTraceToString(e));
        } finally {
            try {
                reader().close();
            } catch (IOException e) {
                LGR.error(Functions.stackTraceToString(e));
            }
        }

    }

}
