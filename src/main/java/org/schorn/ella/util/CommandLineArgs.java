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
package org.schorn.ella.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bschorn
 */
public class CommandLineArgs {

    private final String[] args;
    private final Map<String, String> parameters = new HashMap<>();
    private final Set<String> flags = new HashSet<>();

    public CommandLineArgs(String[] args) {
        this.args = args;
        String paramName = null;
        String paramValue = null;
        for (int i = 0; i < this.args.length; i++) {
            String arg = this.args[i];
            if (arg.startsWith("-")) {
                if (paramName != null && paramValue != null) {
                    parameters.put(paramName, paramValue);
                    paramName = null;
                    paramValue = null;
                } else if (paramName != null) {
                    this.flags.add(paramName);
                    paramName = null;
                }
            }
            if (arg.startsWith("--")) {
                paramName = arg.substring(2);
            } else if (arg.startsWith("-")) {
                paramName = arg.substring(1);
            } else {
                paramValue = arg;
            }
        }
        if (paramName != null && paramValue != null) {
            parameters.put(paramName, paramValue);
        }
    }

    public void loadIntoSystemProperties() {
        for (String parameterName : this.parameters.keySet()) {
            System.setProperty(parameterName, this.parameters.get(parameterName));
        }
    }

    public Set<String> getParameterNames() {
        return this.parameters.keySet();
    }

    public String getParameterValue(String parameterName) {
        return this.parameters.get(parameterName);
    }

    public boolean hasParameterFlag(String parameterFlag) {
        return this.flags.contains(parameterFlag);
    }
}
