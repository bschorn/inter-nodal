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

import org.schorn.ella.context.AppContext;

/**
 *
 *
 * @author schorn
 *
 */
public class FilteredDataToJsonObjectImpl extends TransformExceptionImpl /*implements FilteredDataToJsonObject*/ {

    AppContext context;

    public FilteredDataToJsonObjectImpl(AppContext context) {
        this.context = context;
    }
    /*
    @Override
    public String toString() {
        return String.format("%s", this.getClass().getSimpleName(), this.sourceFormat().toString(), this.targetFormat().toString());
    }
     */
 /*
     * 
     * 
     */
 /*    
	@Override
	public JsonObject apply(ObjectData filterable) {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        switch (filterable.role()) {
        case Object:
        	builder.add(filterable.name(), jsonObjectBuilder((ObjectData) filterable));
        	break;
        case Array:
        	builder.add(filterable.name(), jsonArrayBuilder((ArrayData) filterable));
        	break;
        default:
        	break;
        }
        return builder.build();
	}
     */
 /*
	 * 
     */
 /*    
    public JsonArrayBuilder jsonArrayBuilder(StructData arrayData) {
        JsonArrayBuilder builder = JsonProvider.provider().createArrayBuilder();
        arrayData.nodes().forEach((activeData) -> {
            switch (activeData.role()) {
                case Value:
                    ValueData vdata = (ValueData) activeData;
                    Object value = vdata.activeValue();
                    switch (vdata.valueType().fieldType().dataType().primitiveType().dataGroup()) {
                    case DECIMAL:
                    	builder.add((BigDecimal) value);
                    	break;
                    case NUMBER:
                    	builder.add((Long) value);
                    	break;
                    case BOOL:
                    	builder.add((Boolean) value);
                    	break;
                    default:
                    	builder.add((String) value.toString());
                    	break;
                    }
                    break;
                case Object:
                    builder.add(jsonObjectBuilder((ObjectData) activeData));
                    break;
                case Array:
                    builder.add(jsonArrayBuilder((ArrayData) activeData));
                    break;
                default:
                    break;
            }
        });
        return builder;
    }
     */
 /*
     * 
     */
 /*    
    public JsonObjectBuilder jsonObjectBuilder(StructData objectData) {
        JsonObjectBuilder builder = JsonProvider.provider().createObjectBuilder();
        objectData.nodes().forEach((activeData) -> {
            switch (activeData.role()) {
                case Value:
                	ValueData vdata = (ValueData) activeData;
                    Object value = vdata.activeValue();
                    switch (vdata.valueType().fieldType().dataType().primitiveType().dataGroup()) {
                    case DECIMAL:
                        builder.add(vdata.name(), (BigDecimal) value);
                    	break;
                    case NUMBER:
                        builder.add(vdata.name(), (Long) value);
                    	break;
                    case BOOL:
                        builder.add(vdata.name(), (Boolean) value);
                    	break;
                    default:
                        builder.add(vdata.name(), (String) value.toString());
                    	break;
                    }
                    break;
                case Object:
                    builder.add(activeData.name(), jsonObjectBuilder((ObjectData) activeData));
                    break;
                case Array:
                    builder.add(activeData.name(), jsonArrayBuilder((ArrayData) activeData));
                    break;
                default:
                    break;
            }
        });
        return builder;
    }
     */
}
