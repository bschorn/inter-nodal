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
package org.schorn.ella;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.schorn.ella.node.DataGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bschorn
 */
public class Config {

    /**
     *
     * @param component
     * @param index
     * @param tag
     * @param classFor
     * @param defaultValue
     * @param dataGroups
     * @return
     */
    static public Def define(Component component, Integer index, String tag,
            Class<?> classFor, Object defaultValue, DataGroup... dataGroups) {
        List<Def> definitions = DEFINITIONS.get(component);
        if (definitions == null) {
            definitions = new ArrayList<>();
            DEFINITIONS.put(component, definitions);
        }
        Def definition = new Def(component, index, tag, classFor, defaultValue, dataGroups);
        definitions.add(definition);
        return definition;
    }

    static private final Logger LGR = LoggerFactory.getLogger(Config.class);

    static private final Map<Component, List<Def>> DEFINITIONS = new HashMap<>();

    /**
     *
     */
    static public class Def {

        private final Component component;
        private final Integer index;
        private final String tag;
        private final Class<?> classFor;
        private final Object defaultValue;
        private final DataGroup[] dataGroups;

        Def(Component component, Integer index, String tag, Class<?> classFor,
                Object defaultValue, DataGroup... dataGroups) {
            this.component = component;
            this.index = index;
            this.tag = tag;
            this.classFor = classFor;
            this.defaultValue = defaultValue;
            this.dataGroups = dataGroups;
        }

        public Component component() {
            return this.component;
        }

        public Integer index() {
            return this.index;
        }

        public String tag() {
            return this.tag;
        }

        public Class<?> classFor() {
            return this.classFor;
        }

        public Object defaultValue() {
            return this.defaultValue;
        }

        public DataGroup[] dataGroups() {
            return this.dataGroups;
        }

    }

}
