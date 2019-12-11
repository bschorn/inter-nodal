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

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.schorn.ella.convert.TypeConverter;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.node.OpenNode.OpenArray;
import org.schorn.ella.node.OpenNode.OpenObject;
import org.schorn.ella.node.OpenNode.OpenValue;
import org.schorn.ella.parser.ActiveParser.WriteJson;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
public class WriteJsonImpl implements WriteJson {

    private static final Logger LGR = LoggerFactory.getLogger(WriteJsonImpl.class);

    static Map<Thread, WriteJson> INSTANCES = new HashMap<>();

    static public WriteJson getInstance() {
        WriteJson writeJson = INSTANCES.get(Thread.currentThread());
        if (writeJson == null) {
            writeJson = new WriteJsonImpl();
            INSTANCES.put(Thread.currentThread(), writeJson);
        }
        return writeJson;
    }

    private final ObjectMapper mapper;
    private final Boolean produceNulls;

    private WriteJsonImpl() {
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        this.produceNulls = false;
    }

    @Override
    public String produceRecord(ActiveData activeData) throws Exception {
        StringBuilderWriter writer = new StringBuilderWriter();
        try (LocalGenerator generator = new LocalGenerator(mapper.getFactory().createGenerator(writer))) {
            generator.writeStartObject();
            produce(activeData, generator);
            generator.writeEndObject();
        }
        return writer.toString();
    }

    /*
	 * 
	 * 
     */
    @Override
    public String produceOutput(ActiveData activeData) throws Exception {
        StringBuilderWriter writer = new StringBuilderWriter();
        try (LocalGenerator generator = new LocalGenerator(mapper.getFactory().createGenerator(writer))) {
            try {
                generator.useDefaultPrettyPrinter();
                generator.writeStartObject();
                produce(activeData, generator);
                generator.writeEndObject();
            } catch (Exception ex) {
                LGR.error("{}.produceOutput() - Caught Exception while Transforming ActiveData->JSON.\nException:\n{}\nBuffer:\n{}\nActiveData:\n{}",
                        this.getClass().getSimpleName(),
                        Functions.getStackTraceAsString(ex),
                        generator.toString(),
                        activeData.toString());
            }
        }
        return writer.toString();
    }

    /*
	 * 
     */
    @Override
    public String produceOutput(OpenNode openNode) throws Exception {
        StringBuilderWriter writer = new StringBuilderWriter();
        try (LocalGenerator generator = new LocalGenerator(mapper.getFactory().createGenerator(writer))) {
            generator.useDefaultPrettyPrinter();
            generator.writeStartObject();
            produce(openNode, generator);
            generator.writeEndObject();
        }
        return writer.toString();
    }

    /**
     *
     */
    private void produce(ActiveData inputData, LocalGenerator generator) throws Exception {
        switch (inputData.role()) {
            case Value:
                ValueData valueData = (ValueData) inputData;
                if (!this.produceNulls && valueData.isNull()) {
                    return;
                }
                switch (valueData.valueType().fieldType().dataType().primitiveType().dataGroup()) {
                    case TEXT:
                    case DATE:
                    case TIMESTAMP:
                    case TIME:
                        generator.writeStringField(valueData.name(), valueData.activeValue().toString());
                        break;
                    case DECIMAL:
                        generator.writeNumberField(valueData.name(), valueData.as(BigDecimal.class));
                        break;
                    case NUMBER:
                        generator.writeNumberField(valueData.name(), valueData.as(Long.class));
                        break;
                    case BOOL:
                        generator.writeBooleanField(valueData.name(), valueData.as(Boolean.class));
                        break;
                    default:
                        break;
                }
                break;
            case Object:
                ObjectData objectData = (ObjectData) inputData;
                if (!this.produceNulls && objectData.memberData(ActiveNode.GOOD_DATA_FILTER).isEmpty()) {
                    return;
                }
                generator.writeFieldName(inputData.name());
                generator.writeStartObject();
                for (ActiveData childData : objectData.memberData(ActiveNode.GOOD_DATA_FILTER)) {
                    produce(childData, generator);
                }
                generator.writeEndObject();
                break;
            case Array:
                ArrayData arrayData = (ArrayData) inputData;
                if (!this.produceNulls && arrayData.memberData().isEmpty()) {
                    return;
                }
                generator.writeFieldName(inputData.name());
                generator.writeStartArray();
                for (ActiveData childData : arrayData.memberData()) {
                    produce(childData, generator);
                }
                generator.writeEndArray();
                break;
            default:
                break;
        }
    }

    /**
     *
     */
    private void produce(OpenNode openNode, LocalGenerator generator) throws Exception {
        switch (openNode.role()) {
            case Value:
                OpenValue valueData = (OpenValue) openNode;
                if (!this.produceNulls && valueData.value() == null) {
                    return;
                }
                switch (valueData.type()) {
                    case STRING:
                        generator.writeStringField(valueData.name(), (String) valueData.value());
                        break;
                    case DECIMAL:
                        generator.writeNumberField(valueData.name(), (BigDecimal) valueData.value());
                        break;
                    case INTEGER:
                        generator.writeNumberField(valueData.name(), (Long) valueData.value());
                        break;
                    case BOOL:
                        generator.writeBooleanField(valueData.name(), (Boolean) valueData.value());
                        break;
                    default:
                        break;
                }
                break;
            case Object:
                OpenObject openObject = (OpenObject) openNode;
                if (!this.produceNulls && openObject.nodes().isEmpty()) {
                    return;
                }
                generator.writeFieldName(openNode.name());
                generator.writeStartObject();
                for (OpenNode openChild : openObject.nodes()) {
                    produce(openChild, generator);
                }
                generator.writeEndObject();
                break;
            case Array:
                OpenArray openArray = (OpenArray) openNode;
                if (!this.produceNulls && openArray.nodes().isEmpty()) {
                    return;
                }
                generator.writeFieldName(openNode.name());
                generator.writeStartArray();
                for (OpenNode childData : openArray.nodes()) {
                    produce(childData, generator);
                }
                generator.writeEndArray();
                break;
            default:
                break;
        }
    }

    /**
     *
     *
     *
     */
    static class LocalGenerator implements Closeable {

        enum WriterContext {
            OBJECT,
            ARRAY,
        }
        int object = 0;
        int array = 0;

        StringBuilder buf = new StringBuilder();
        Deque<WriterContext> context = new ArrayDeque<>();

        int tIdx = 0;

        private final JsonGenerator generator;

        LocalGenerator(JsonGenerator generator) {
            this.generator = generator;
        }

        public void writeStartObject() throws IOException {
            this.buf.append("{");
            this.generator.writeStartObject();
            this.object++;
            this.context.addFirst(WriterContext.OBJECT);
        }

        public void writeEndObject() throws IOException {
            this.buf.append("}");
            WriterContext wc = this.context.peekFirst();
            if (wc != null && wc.equals(WriterContext.OBJECT)) {
                this.generator.writeEndObject();
                this.object--;
                this.context.removeFirst();
            }
        }

        /*
		 *  [
         */
        public void writeStartArray() throws IOException {
            this.buf.append("[");
            this.generator.writeStartArray();
            this.array++;
            this.context.addFirst(WriterContext.ARRAY);
        }

        public void writeEndArray() throws IOException {
            this.buf.append("]");
            WriterContext wc = this.context.peekFirst();
            if (wc != null && wc.equals(WriterContext.ARRAY)) {
                this.generator.writeEndArray();
                this.array--;
                this.context.removeFirst();
            }
        }

        /*
		 *  "field_name" : {  }
         */
        public void writeFieldName(String name) throws IOException {
            WriterContext wc = this.context.peekFirst();
            if (wc != null && wc.equals(WriterContext.OBJECT)) {
                this.buf.append("'").append(name).append("': ");
                this.generator.writeFieldName(name);
            }
        }

        /*
		 *  { "boolean_field" : true }
         */
        public void writeBooleanField(String name, boolean booleanValue) throws IOException {
            if (WriterContext.ARRAY.equals(this.context.peekFirst())) {
                this.buf.append(String.format("%s", booleanValue ? "true" : "false"));
            } else if (WriterContext.OBJECT.equals(this.context.peekFirst())) {
                this.buf.append("'").append(name).append("': ").append(booleanValue ? "true" : "false");
                this.generator.writeFieldName(name);
            }
            this.generator.writeBoolean(booleanValue);
        }

        /*
		 *  { "number_field" : 0 }
         */
        public void writeNumberField(String name, Long as) throws IOException {
            if (WriterContext.ARRAY.equals(this.context.peekFirst())) {
                this.buf.append("'").append(String.format("%d", as));
            } else if (WriterContext.OBJECT.equals(this.context.peekFirst())) {
                this.buf.append("'").append(name).append("': ").append(String.format("%d", as));
                this.generator.writeFieldName(name);
            }
            this.generator.writeNumber(as);
        }

        /*
		 *  { "number_field" : 0.0 }
         */
        public void writeNumberField(String name, BigDecimal as) throws Exception {
            if (WriterContext.ARRAY.equals(this.context.peekFirst())) {
                this.buf.append(String.format("%f", as.doubleValue()));
            } else if (WriterContext.OBJECT.equals(this.context.peekFirst())) {
                this.buf.append("'").append(name).append("': ").append(String.format("%f", as.doubleValue()));
                this.generator.writeFieldName(name);
            }
            this.generator.writeNumber(TypeConverter.recast(BigDecimal.class, String.class, as));
        }

        /*
		 *  { "string_field" : "" }
         */
        public void writeStringField(String name, String string) throws IOException {
            if (WriterContext.ARRAY.equals(this.context.peekFirst())) {
                this.buf.append(String.format("%s", string));
            } else if (WriterContext.OBJECT.equals(this.context.peekFirst())) {
                this.buf.append("'").append(name).append("': ").append(String.format("'%s'", string));
                this.generator.writeFieldName(name);
            }
            this.generator.writeString(string);
        }

        public void useDefaultPrettyPrinter() throws IOException {
            this.generator.useDefaultPrettyPrinter();

        }

        @Override
        public void close() throws IOException {
            this.generator.close();
        }

        @Override
        public String toString() {
            return this.buf.toString();
        }
    }
}
