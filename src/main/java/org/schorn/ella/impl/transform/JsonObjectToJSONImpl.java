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
package org.schorn.ella.impl.transform;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.schorn.ella.context.AppContext;

/**
 *
 * @author schorn
 *
 */
public class JsonObjectToJSONImpl extends TransformExceptionImpl /*implements JsonObjectToJSON*/ {
    /*
	static class JsonPrettyPrint {
	    JsonWriterFactory writerFactory;
	    public JsonPrettyPrint() {
	        Map<String, Boolean> config = new HashMap<>();
	        config.put(JsonGenerator.PRETTY_PRINTING, true);
	        this.writerFactory = Json.createWriterFactory(config);
	    }
	    public String asString(JsonObject jsonObject) {
	        StringWriter stringWriter = new StringWriter();
	        try (JsonWriter jsonWriter = this.writerFactory.createWriter(stringWriter)) {
	            jsonWriter.write(jsonObject);
	        }
	        return stringWriter.toString();
	    }
	    public StringWriter asStringWriter(JsonObject jsonObject) {
	        StringWriter stringWriter = new StringWriter();
	        try (JsonWriter jsonWriter = this.writerFactory.createWriter(stringWriter)) {
	            jsonWriter.write(jsonObject);
	        }
	        return stringWriter;
	    }
	}
     */

 /*
	private final JsonPrettyPrint prettyPrint = new JsonPrettyPrint();

    ActiveContext context;

    public JsonObjectToJSONImpl(ActiveContext context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return String.format("%s", this.getClass().getSimpleName(), this.sourceFormat().toString(), this.targetFormat().toString());
    }
	
	@Override
	public String apply(JsonObject jsonObject) {
		return this.prettyPrint.asString(jsonObject);
	}
     */
}
