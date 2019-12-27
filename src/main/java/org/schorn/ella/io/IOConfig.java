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
package org.schorn.ella.io;

import java.util.Properties;
import org.schorn.ella.ActiveConfig;
import org.schorn.ella.Component;
import org.schorn.ella.app.ActiveMain;
import org.schorn.ella.node.DataGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bschorn
 */
public enum IOConfig implements ActiveConfig {
    ACTIVITY_DIR(ActiveMain.class, "IO.ActivityDir", DataGroup.TEXT, null, "./activity"),
    ACTIVITY_FILE(ActiveMain.class, "IO.ActivityFile", DataGroup.TEXT, null, "activity.{DATE}.{CONTEXT}.log"),    ;
    private static final Logger LGR = LoggerFactory.getLogger(IOConfig.class);

    private final Class<?> propertyOwner;
    private final String propertyKey;
    private final String defaultValue;
    private final DataGroup dataGroup;
    private final String delimiter;

    IOConfig(Class<?> propertyOwner, String propertyKey, DataGroup dataGroup, String delimiter, String defaultValue) {
        this.propertyOwner = propertyOwner;
        this.propertyKey = propertyKey;
        this.dataGroup = dataGroup;
        this.delimiter = delimiter;
        this.defaultValue = defaultValue;
    }

    @Override
    public String delimiter() {
        return this.delimiter;
    }

    @Override
    public DataGroup dataGroup() {
        return this.dataGroup;
    }

    @Override
    public Class<?> propertyOwner() {
        return this.propertyOwner;
    }

    @Override
    public String propertyKey() {
        return this.propertyKey;
    }

    @Override
    public String defaultValue() {
        return this.defaultValue;
    }

    @Override
    public Logger logger() {
        return LGR;
    }

    @Override
    public Properties properties() {
        return Component.IO.properties();
    }

}
