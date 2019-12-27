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
package org.schorn.ella.server;

import org.schorn.ella.ActiveConfig;
import java.util.Properties;
import java.util.StringJoiner;
import org.schorn.ella.Component;
import org.schorn.ella.app.*;
import org.schorn.ella.context.ActiveContext;
import org.schorn.ella.node.DataGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * CONFIG_ENUM(PropertyOwner, PropertyKey, DefaultValue)
 *
 * PropertyOwner - the class from which the property will be accessed/used
 * (error will be logged if accessed by non-owner class) PropertyKey - the KEY
 * in System.getProperty(KEY,default); DefaultValue - the DEFAULT in
 * System.getProperty(key,DEFAULT);
 *
 * @author schorn
 *
 */
public enum ServerConfig implements ActiveConfig {
    ACTIVE_MASTER_SERVER(ActiveContext.Action.class, "Active.MasterServer", DataGroup.TEXT, ",", null),
    ACTIVE_MASTER_SERVER_ADDRESS(ActiveContext.Data.class, "Active.MasterServerAddress", DataGroup.URL, ",", null),
    ACTIVE_SERVERS(AdminServer.class, "Active.Servers", DataGroup.TEXT, ",", null),
    ADMIN_SERVER_CFG_MAX_IO_THREADS(AdminServer.Cfg.class, "AdminServer.Cfg.MaxIOThreads", DataGroup.NUMBER, null, "5");

    private static final Logger LGR = LoggerFactory.getLogger(ServerConfig.class);

    private final Class<?> propertyOwner;
    private final String propertyKey;
    private final String defaultValue;
    private final DataGroup dataGroup;
    private final String delimiter;

    ServerConfig(Class<?> propertyOwner, String propertyKey, DataGroup dataGroup, String delimiter, String defaultValue) {
        this.propertyOwner = propertyOwner;
        this.propertyKey = propertyKey;
        this.dataGroup = dataGroup;
        this.delimiter = delimiter;
        this.defaultValue = defaultValue;
    }

    @Override
    public DataGroup dataGroup() {
        return this.dataGroup;
    }

    /*
    @Override
    public boolean isMultiValue() {
        return this.delimiter != null;
    }
     */

    @Override
    public String delimiter() {
        return this.delimiter;
    }

    /*
    @Override
    public String propertyName() {
        return this.name();
    }
     */

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
        return Component.SERVER.properties();
    }

    static public String dump() {
        StringJoiner joiner = new StringJoiner("\n\t", "[\n\t", "\n]\n");
        for (ServerConfig config : ServerConfig.values()) {
            joiner.add(String.format("%-35s: %-40s %-60s", config.name(), config.propertyKey, config.asString()));
        }
        return joiner.toString();
    }

}
