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
package org.schorn.ella.impl.parser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.node.OpenNode.OpenStruct;
import org.schorn.ella.parser.ActiveParser.ReadYaml;

/**
 *
 *
 */
public class ReadYamlImpl implements ReadYaml {

    static Map<Thread, ReadYaml> INSTANCES = new HashMap<>();

    static public ReadYaml getInstance() {
        ReadYaml parseYaml = INSTANCES.get(Thread.currentThread());
        if (parseYaml == null) {
            parseYaml = new ReadYamlImpl();
            INSTANCES.put(Thread.currentThread(), parseYaml);
        }
        return parseYaml;
    }

    private final YAMLFactory yamlFactory;

    private ReadYamlImpl() {
        this.yamlFactory = new YAMLFactory();
    }

    /**
     *
     * @param parser
     * @return
     * @throws Exception
     */
    private OpenNode parse(YAMLParser parser) throws Exception {
        boolean throwException = true;
        OpenNode openNode = null;
        OpenStruct rootNode = null;
        String keyName = null;
        String oldKeyName = null;

        JsonToken currentToken = parser.nextToken();
        while (parser.hasCurrentToken()) {
            //JsonToken currentToken = parser.currentToken();
            switch (currentToken) {
                case START_OBJECT:
                    if (openNode == null) {
                        if (rootNode == null) {
                            rootNode = (OpenStruct) OpenNode.createObject("root");
                        }
                        openNode = rootNode;
                    } else {
                        if (keyName == null) {
                            keyName = openNode.name();
                        }
                        OpenNode keyNameObj = OpenNode.createObject(keyName);
                        openNode.add(keyNameObj);
                        openNode = keyNameObj;
                    }
                    break;
                case END_OBJECT:
                    if (openNode != null) {
                        openNode = openNode.parent();
                    }
                    break;
                case START_ARRAY:
                    if (openNode == null) {
                        if (rootNode == null) {
                            rootNode = (OpenStruct) OpenNode.createArray("root");
                        }
                        openNode = rootNode;
                    } else {
                        if (keyName == null) {
                            keyName = openNode.name();
                        }
                        OpenNode keyNameAry = OpenNode.createArray(keyName);
                        openNode.add(keyNameAry);
                        openNode = keyNameAry;
                    }
                    break;
                case END_ARRAY:
                    if (openNode != null) {
                        openNode = openNode.parent();
                    }
                    break;
                case FIELD_NAME:
                    keyName = parser.getText();
                    break;
                default:
                    try {
                        if (openNode != null) {
                            keyName = (keyName == null) ? oldKeyName : keyName;
                            OpenNode openValue = OpenNode.createValue(keyName);
                            oldKeyName = keyName;
                            keyName = null;
                            boolean hasValue = true;
                            switch (currentToken) {
                                case VALUE_NUMBER_FLOAT:
                                    openValue.add(parser.getDecimalValue());
                                    break;
                                case VALUE_NUMBER_INT:
                                    openValue.add(parser.getLongValue());
                                    break;
                                case VALUE_STRING:
                                    if (parser.getText().isEmpty()) {
                                        hasValue = false;
                                    } else {
                                        openValue.add(parser.getText());
                                    }
                                    break;
                                case VALUE_TRUE:
                                    openValue.add(Boolean.TRUE);
                                    break;
                                case VALUE_FALSE:
                                    openValue.add(Boolean.FALSE);
                                    break;
                                case VALUE_NULL:
                                    //openValue.add(null);
                                    hasValue = false;
                                    break;
                                case VALUE_EMBEDDED_OBJECT:
                                case NOT_AVAILABLE:
                                default:
                                    hasValue = false;
                                    break;
                            }
                            if (hasValue) {
                                openNode.add(openValue);
                            }
                            //openNode = (hasValue) ? openNode.add(openValue) : openNode;
                        }
                    } catch (Exception ex) {
                        if (throwException) {
                            throw ex;
                        }
                    }
            }
            currentToken = parser.nextToken();
        }
        if (rootNode != null && rootNode.nodes() != null && rootNode.nodes().size() == 1) {
            if (rootNode.nodes().get(0).role().equals(Role.Object)) {
                return rootNode.nodes().get(0);
            }
        }
        return rootNode;
    }

    @Override
    public OpenNode parse(File file) throws Exception {
        return this.parse(this.yamlFactory.createParser(file));
    }

    @Override
    public OpenNode parse(URL url) throws Exception {
        return this.parse(this.yamlFactory.createParser(url));
    }

    @Override
    public OpenNode parse(InputStream stream) throws Exception {
        return this.parse(this.yamlFactory.createParser(stream));
    }

    @Override
    public OpenNode parse(Reader reader) throws Exception {
        return this.parse(this.yamlFactory.createParser(reader));
    }

    @Override
    public OpenNode parse(byte[] data) throws Exception {
        return this.parse(this.yamlFactory.createParser(data));
    }

    @Override
    public OpenNode parse(byte[] data, int offset, int len) throws Exception {
        return this.parse(this.yamlFactory.createParser(data, offset, len));
    }

    @Override
    public OpenNode parse(String content) throws Exception {
        return this.parse(this.yamlFactory.createParser(content));
    }

    @Override
    public OpenNode parse(char[] content) throws Exception {
        return this.parse(this.yamlFactory.createParser(content));
    }

    @Override
    public OpenNode parse(char[] content, int offset, int len) throws Exception {
        return this.parse(this.yamlFactory.createParser(content, offset, len));
    }

    /*
    @Override
    public OpenNode parse(DataInput input) throws Exception {
        return this.parse(this.yamlFactory.createParser(input));
    }
    */
}
