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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.util.OptionalEx;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.ActiveTransform.DSVLineParser;
import org.schorn.ella.transform.TransformProvider;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.ActiveIO.Tabular;
import org.schorn.ella.io.ReaderWorkerWriter.ProcessNode;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.StructData;

/**
 *
 * @author schorn
 *
 */
public class TabularIO {

    /**
     *
     *
     *
     */
    static public class Incoming implements ProcessNode<String, StructData> {

        private static final Logger LGR = LoggerFactory.getLogger(Incoming.class);

        ConcurrentLinkedQueue<StructData> queue = new ConcurrentLinkedQueue<>();
        AppContext nodeContext;
        @SuppressWarnings("rawtypes")
        Transform importer;
        Consumer<StructData> consumer;
        DSVLineParser parser;
        String typeName;
        Tabular tabular;

        public Incoming(AppContext nodeContext, Consumer<StructData> consumer, DSVLineParser parser, String typeName) throws Exception {
            this.nodeContext = nodeContext;
            this.importer = TransformProvider.provider().getTransform(this.nodeContext, Format.DSV, Format.ActiveNode);
            this.consumer = consumer;
            this.parser = parser;
            this.typeName = typeName;
            this.tabular = null;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void accept(String line) {
            List<Object> tokens = this.parser.apply(line);
            if (this.tabular == null) {
                try {
                    this.tabular = Tabular.create(this.nodeContext, this.typeName, tokens);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                if (this.tabular.getFieldNames().size() == tokens.size()) {
                    this.tabular.setFieldValues(tokens);
                } else {
                    tokens = tryAgainParser(line);
                    if (this.tabular.getFieldNames().size() == tokens.size()) {
                        this.tabular.setFieldValues(tokens);
                    } else {
                        LGR.error("Unable to parse line: " + line);
                        return;
                    }
                }
                OptionalEx<StructData> optStructData = (OptionalEx<StructData>) this.importer.apply(this.tabular);
                if (optStructData.isPresent()) {
                    StructData structData = optStructData.get();
                    if (this.consumer != null && structData != null) {
                        this.consumer.accept(structData);
                    } else {
                        this.queue.add(structData);
                    }
                }
            }
        }

        @Override
        public StructData get() {
            return this.queue.poll();
        }

    }

    /**
     * Node-to-JSON Process
     *
     *
     */
    static public class Outgoing implements ProcessNode<StructData, String> {

        @SuppressWarnings("unused")
        private static final Logger LGR = LoggerFactory.getLogger(Outgoing.class);

        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        AppContext nodeContext;
        @SuppressWarnings("rawtypes")
        Transform exporter;
        Consumer<String> consumer;

        /**
         *
         * @param nodeContext
         * @param consumer
         * @throws Exception
         */
        public Outgoing(AppContext nodeContext, Consumer<String> consumer) throws Exception {
            this.nodeContext = nodeContext;
            this.exporter = TransformProvider.provider().getTransform(nodeContext, Format.ActiveNode, Format.DSV);
            this.consumer = consumer;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void accept(StructData node) {
            if (node == null) {
                this.consumer.accept(null);
            } else {
                String line = (String) this.exporter.apply(node);
                if (this.consumer != null) {
                    this.consumer.accept(line);
                } else {
                    this.queue.add(line);
                }
            }
        }

        @Override
        public String get() {
            return this.queue.poll();
        }

    }

    /**
     * Hack
     *
     * @param line
     * @return
     */
    public static List<Object> tryAgainParser(String line) {
        final char ndelm = '^';
        final char odelm = ',';
        final char qual = '"';
        boolean in = false;
        char[] na = new char[line.length()];
        int nai = 0;
        for (int i = 0; i < line.length(); ++i) {
            switch (line.charAt(i)) {
                case odelm:
                    na[nai++] = in ? odelm : ndelm;
                    break;
                case qual:
                    in = in ? false : true;
                    break;
                default:
                    na[nai++] = line.charAt(i);
                    break;
            }
        }
        String newStr = new String(na);
        String[] newfields = newStr.split("[\\^]");
        return Arrays.asList(newfields).stream().map(s -> s.trim()).collect(Collectors.toList());
    }

}
