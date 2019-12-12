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
package org.schorn.ella.schema;

import java.util.Map;

/**
 *
 * @author bschorn
 */
public class OtherType {

    private final String name;
    private final Map<String, String> methods;

    public OtherType(String name, Map<String, String> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("package org.schorn.ella.spec;\n");
        builder.append("\n");
        builder.append("import java.util.*;\n");
        builder.append("import org.schorn.ella.node.BondType;\n");
        builder.append("import org.schorn.ella.node.DataGroup;\n");
        builder.append("\n");
        builder.append(String.format("public class %s {\n", this.name));
        for (String key : this.methods.keySet()) {
            String value = this.methods.get(key);
            builder.append(String.format("\tprivate %s %s;\n", key, value));
        }
        builder.append("}\n");
        return builder.toString();
    }
}
