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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringJoiner;
import org.schorn.ella.app.ActiveApp.Config;
import org.schorn.ella.io.ResourceReader;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bschorn
 */
public enum Component implements ClassLocator {
    Provider,
    ActiveApp,
    ActiveContext,
    ActiveEvent,
    ActiveHtml,
    ActiveHTTP,
    ActiveIO,
    ActiveNode,
    ActiveRepo,
    ActiveServer,
    ActiveServices,
    ActiveSQL,
    ActiveTransform;

    static private final Logger LGR = LoggerFactory.getLogger(Component.class);
    static private final Map<Component, Map<String, Object>> COMPONENTS = new HashMap<>();

    private Path resourcesPath = null;
    private ClassLocator classLocator = null;

    public Properties classPathProperties() {
        Map<String, Object> classMap = COMPONENTS.get(this);
        if (classMap != null && classMap.containsKey("ClassPath")) {
            Object object = classMap.get("ClassPath");
            if (object != null && object instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) object;
                Properties properties = new Properties();
                for (Entry<String, Object> entry : map.entrySet()) {
                    if (entry.getValue() != null) {
                        properties.setProperty(entry.getKey(), entry.getValue().toString());
                    }
                }
                return properties;
            }
        }
        return new Properties();
    }

    public Map<String, Object> configMap() {
        Map<String, Object> componentMap = COMPONENTS.get(this);
        if (componentMap.containsKey("Config")) {
            Object object = componentMap.get("Config");
            if (object instanceof Map) {
                return (Map<String, Object>) object;
            }
        }
        return componentMap;
    }

    public Map<String, Object> configMap(String contextName) {
        Map<String, Object> componentMap = COMPONENTS.get(this);
        if (componentMap.containsKey("Config")) {
            Object object = componentMap.get("Config");
            if (object instanceof List) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) object;
                for (Map<String, Object> map : list) {
                    String value = (String) map.get("context");
                    if (value != null && value.equalsIgnoreCase(contextName)) {
                        return map;
                    }
                }
            }
        }
        return componentMap;
    }

    @Override
    public Class<?> getImplClass(String interfaceName) throws Exception {
        return this.classLocator.getImplClass(interfaceName);
    }

    @Override
    public Class<?> getImplClass(Class<?> interfaceClass) throws Exception {
        return this.classLocator.getImplClass(interfaceClass);
    }

    @Override
    public <T> T newInstance(Class<T> interfaceClass) {
        return this.classLocator.newInstance(interfaceClass);
    }

    @Override
    public Object newInstance(String interfaceName) throws Exception {
        return this.classLocator.newInstance(interfaceName);
    }

    static public void bootstrap(Properties properties) throws Exception {
        loadFromMap(readFromProperties(properties));
    }

    static private Map<String, Object> readFromProperties(Properties properties) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        for (String name : properties.stringPropertyNames()) {
            map.put(name, properties.getProperty(name));
        }
        return convert(map);
    }

    static private Map<String, Object> convert(Map<String, Object> map) {
        Map<String, Object> newMap = new HashMap<>();
        for (String key : map.keySet()) {
            String[] keys = key.split("\\.");
            Component component = Component.parse(keys[0]);
            if (component == null) {
                // not a component
                newMap.put(key, map.get(key));
            } else {
                // component
                Map<String, Object> componentMap = (Map<String, Object>) newMap.get(component.name());
                if (componentMap == null) {
                    // new component
                    componentMap = new HashMap<>();
                    newMap.put(component.name(), componentMap);
                }
                if (keys.length == 3) {
                    // Component.Config.date : 20191231
                    String configKey = keys[1];
                    Map<String, Object> configMap = (Map<String, Object>) componentMap.get(configKey);
                    if (configMap == null) {
                        configMap = new HashMap<>();
                        componentMap.put(configKey, configMap);
                    }
                    configMap.put(keys[2], map.get(key));
                }
            }
        }
        return newMap;
    }

    static public void init() throws Exception {
        loadFromMap(mergeConfigs());
        for (Component component : Component.values()) {
            component.classLocator = ClassLocator.create(component.classPathProperties());
        }
    }

    static private Map<String, Object> mergeConfigs() throws Exception {
        Map<String, Object> mergedMap = null;
        ActiveConfig activeConfig = new ActiveConfig();
        for (URI configURI : activeConfig.cascading()) {
            Map<String, Object> currentMap = readFromURI(configURI);
            if (mergedMap == null) {
                mergedMap = currentMap;
            } else {
                Map<String, Object> tempMap = new HashMap<>(mergedMap);
                currentMap.forEach((key, value) -> tempMap.merge(key, value, (v1, v2) -> v2));
                mergedMap = tempMap;
            }
        }
        return mergedMap;
    }

    static private Map<String, Object> readFromURI(URI uri) throws Exception {
        StringJoiner joiner = new StringJoiner(System.lineSeparator(), "", "");
        ResourceReader.readLines(uri.toURL(), line -> joiner.add(line));
        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        mapper.findAndRegisterModules();
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {
        };
        return mapper.readValue(joiner.toString(), typeRef);
    }

    static private void loadFromMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                Component component = Component.parse(entry.getKey());
                if (component == null) {
                    // TODO log what is being skipped b/c there is no component
                    continue;
                }
                if (COMPONENTS.containsKey(component)) {
                    Map<String, Object> current = COMPONENTS.get(component);
                    Map<String, Object> override = (Map<String, Object>) entry.getValue();
                    Map<String, Object> cumulative = new HashMap<>(current);
                    override.forEach((key, value) -> cumulative.merge(key, value, (v1, v2) -> v2));
                    COMPONENTS.put(component, cumulative);
                } else {
                    COMPONENTS.put(component, (Map<String, Object>) entry.getValue());
                }
            } catch (Exception ex) {
                LGR.error("{}.reformat() - entry.key: {} value: {} - Caught Exception: {}",
                        Config.class.getSimpleName(),
                        entry.getKey(), entry.getValue(),
                        Functions.stackTraceToString(ex));
            }
        }
    }

    static public Component parse(String name) {
        for (Component cp : Component.values()) {
            if (cp.name().equalsIgnoreCase(name)) {
                return cp;
            }
        }
        for (Component cp : Component.values()) {
            if (cp.name().endsWith(name)) {
                return cp;
            }
        }
        return null;
    }

/*
    protected void init0() {
        Properties properties0 = null;
        try {
            String root = System.getProperty("Active.Resources");
            if (root != null) {
                this.resourcesPath = Paths.get(root);
            } else {
                this.resourcesPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            }
            {
                String propertiesFilePath = String.format("%s%s%s%s%s.cfg",
                        this.resourcesPath.toString(), File.separator, "props", File.separator, this.name().toLowerCase());
                Path path = Paths.get(propertiesFilePath);
                if (Files.exists(path)) {
                    properties0 = new Properties();
                    properties0.load(new FileInputStream(propertiesFilePath));
                } else {
                    throw new Exception(String.format("File not found: %s", propertiesFilePath));
                }
            }
            {
                String environment = System.getProperty("Active.Environment");
                String propertiesFilePath = String.format("%s%s%s%s%s.%s.cfg",
                        this.resourcesPath.toString(), File.separator, "props", File.separator, this.name().toLowerCase(), environment);
                Path path = Paths.get(propertiesFilePath);
                if (Files.exists(path)) {
                    properties0 = new Properties();
                    properties0.load(new FileInputStream(propertiesFilePath));
                } else {
                    throw new Exception(String.format("File not found: %s", propertiesFilePath));
                }
            }
            for (Map.Entry<Object, Object> entry : PROPERTIES.entrySet()) {
                String[] keyParts = entry.getKey().toString().split("/.");
                if (keyParts[0].equalsIgnoreCase(this.name())) {
                    StringJoiner keyJoiner = new StringJoiner(".", "", "");
                    for (int i = 1; i < keyParts.length; i++) {
                        keyJoiner.add(keyParts[i]);
                    }
                    this.properties.setProperty(keyJoiner.toString(), entry.getValue().toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            for (Map.Entry<Object, Object> entry : properties0.entrySet()) {
                String key = entry.getKey().toString();
                if (this.properties.containsKey(key)) {
                    String value = entry.getValue().toString();
                    this.properties.setProperty(entry.getKey().toString(), entry.getValue().toString());
                }
            }
            this.classLocator = ClassLocator.create(this.properties);
        }
    }

    static public void main(String[] args) {
        URI uri = URI.create("file:///D:/Users/bschorn/documents/GitHub/jane-bank/src/main/resources/config/active.config");
        try {
            Component.readFromURI(uri);
            for (Component component : COMPONENTS.keySet()) {
                System.out.println(component.name());
                dump(COMPONENTS.get(component), 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    */

}
