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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.util.HashMap;
import java.util.Map;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.OpenNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.parser.ActiveParser.WriteYaml;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;

/**
 *
 * @author schorn
 *
 */
public class WriteYamlImpl implements WriteYaml {

    private static final Logger LGR = LoggerFactory.getLogger(WriteYamlImpl.class);

    static Map<Thread, WriteYaml> INSTANCES = new HashMap<>();

    static public WriteYaml getInstance() {
        WriteYaml writeYaml = INSTANCES.get(Thread.currentThread());
        if (writeYaml == null) {
            writeYaml = new WriteYamlImpl();
            INSTANCES.put(Thread.currentThread(), writeYaml);
        }
        return writeYaml;
    }


    @Override
    public String produceRecord(ActiveNode.ActiveData activeData) throws Exception {
        String json = ((StructData) activeData).asJsonString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        return new YAMLMapper().writeValueAsString(jsonNode);        
    }

    @Override
    public String produceOutput(ActiveNode.ActiveData activeData) throws Exception {
        String json = ((StructData) activeData).asJsonString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        return new YAMLMapper().writeValueAsString(jsonNode);        
    }

    @Override
    public String produceOutput(OpenNode openNode) throws Exception {
        String json = openNode.asJsonString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        return new YAMLMapper().writeValueAsString(jsonNode);        
    }

    

}
