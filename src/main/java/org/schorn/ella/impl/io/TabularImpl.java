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

import java.util.ArrayList;
import java.util.List;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.ActiveIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class TabularImpl implements ActiveIO.Tabular {

    @SuppressWarnings("unused")
    private static final Logger LGR = LoggerFactory.getLogger(TabularImpl.class);

    AppContext context;
    String rowName;
    List<String> fieldNames;
    List<Object> fieldValues;
    boolean allowDynamicFields = false;

    public TabularImpl(AppContext context, String rowName, List<Object> fieldNames) {
        this.context = context;
        this.rowName = rowName;
        this.fieldNames = new ArrayList<>();
        /*
        fieldNames.forEach(fn -> this.fieldNames.add(fn.toString()));
        {
            String propertyValue = AppConfig.getTabularAllowDynamicFields("*");
            if (propertyValue != null && !propertyValue.equalsIgnoreCase("0")) {
                this.allowDynamicFields = true;
            } else {
                propertyValue = AppConfig.getTabularAllowDynamicFields(this.rowName);
                if (!propertyValue.equalsIgnoreCase("0")) {
                    this.allowDynamicFields = true;
                }
            }
        }
        */
    }

    @Override
    public String getRowName() {
        return this.rowName;
    }

    @Override
    public List<String> getFieldNames() {
        return this.fieldNames;
    }

    @Override
    public List<Object> getFieldValues() {
        return this.fieldValues;
    }

    @Override
    public void setFieldValues(List<Object> fieldValues) {
        this.fieldValues = fieldValues;
    }

    @Override
    public boolean allowDynamicFields() {
        return this.allowDynamicFields;
    }

}
