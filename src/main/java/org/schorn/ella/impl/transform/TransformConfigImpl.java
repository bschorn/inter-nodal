/*
 * The MIT License
 *
 * Copyright 2019 bschorn.
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

import java.util.Map;
import java.util.regex.Pattern;
import org.schorn.ella.Component;
import org.schorn.ella.transform.ActiveTransform;


/**
 *
 * @author bschorn
 */
public class TransformConfigImpl implements ActiveTransform.Config {

    private String contextName;
    private Class<?> lineParserCSV = org.schorn.ella.impl.transform.DSVLineParserImpl.class;
    private Pattern lineParserCSVPattern = Pattern.compile("(?:(?<=\")([^\"]*)(?=\"))|(?<=,|^)([^,]*)(?=,|$)");

    public TransformConfigImpl(String contextName) throws Exception {
        this.contextName = contextName;
        Map<String, Object> map = Component.ActiveTransform.configMap(contextName);
        if (map.containsKey("lineParserCSV")) {
            String className = (String) map.get("lineParserCSV");
            this.lineParserCSV = Class.forName(className);
        }
        if (map.containsKey("lineParserCSVPattern")) {
            String regex = (String) map.get("lineParserCSVPattern");
            this.lineParserCSVPattern = Pattern.compile(regex);
        }
    }

    @Override
    public Class<?> lineParserCSV() {
        return this.lineParserCSV;
    }

    @Override
    public Pattern lineParserCSVPattern() {
        return this.lineParserCSVPattern;
    }

}
