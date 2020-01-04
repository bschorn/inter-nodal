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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.transform.ActiveTransform;

/**
 *
 * @author schorn
 *
 */
public class DSVLineParserImpl extends TransformExceptionImpl implements ActiveTransform.DSVLineParser {

    private final AppContext context;
    private final Pattern pattern;

    public DSVLineParserImpl(AppContext context) {
        this.context = context;
        Pattern pattern0 = Pattern.compile("(?:(?<=\")([^\"]*)(?=\"))|(?<=,|^)([^,]*)(?=,|$)");
        ActiveTransform.Config config = ActiveTransform.Config.get(context);
        if (config != null) {
            Pattern pattern1 = config.lineParserCSVPattern();
            if (pattern1 != null) {
                pattern0 = pattern1;
            }
        }
        this.pattern = pattern0;
    }

    @Override
    public List<Object> apply(String line) {
        this.clearException();
        List<Object> fields = new ArrayList<>();
        if (line == null) {
            return fields;
        }
        Matcher matcher = this.pattern.matcher(line);
        String match;
        while (matcher.find()) {
            match = matcher.group(1);
            if (match != null) {
                fields.add(match);
            } else {
                fields.add(matcher.group(2));
            }
        }
        return fields;
    }

    /**
     *
     * @return
     */
    public AppContext context() {
        return this.context;
    }
}
