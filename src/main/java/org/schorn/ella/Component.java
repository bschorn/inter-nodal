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
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    ActiveNode,
    ActiveIO,
    ActiveRepo,
    ActiveServer,
    ActiveServices,
    ActiveHtml;

    static private final Logger LGR = LoggerFactory.getLogger(Component.class);
    static private final Properties PROPERTIES = new Properties();
    static private final Map<Component, Map<String, Object>> COMPONENTS = new HashMap<>();

    private final Properties properties = new Properties();
    private Path resourcesPath = null;
    private ClassLocator classLocator = null;

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

    /*
    public Path getResourcePath() {
        return this.resourcesPath;
    }
     */

 /*
    public Path getResourcedPath(String... dirsFile) throws FileNotFoundException {
        String filePath = this.resourcesPath.toString();
        StringJoiner pathJoiner = new StringJoiner(File.separator, "", "");
        pathJoiner.add(this.resourcesPath.toString());
        for (String dirFile : dirsFile) {
            pathJoiner.add(dirFile);
        }
        filePath = pathJoiner.toString();
        Path resourcedPath = Paths.get(filePath);
        if (Files.exists(resourcedPath)) {
            return resourcedPath;
        }
        throw new FileNotFoundException(String.format("File not found: '%s'", filePath));
    }
     */

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

    static public void init(Properties properties) throws Exception {
        readFromProperties(properties);
        String resourcePath = (String) ActiveApp.configMap().get("resources");
        String environment = (String) ActiveApp.configMap().get("environment");
        String context = (String) ActiveApp.configMap().get("context");
        Path activeConfig = Paths.get(resourcePath, "config", "active.config");
        Path environmentConfig = Paths.get(resourcePath, "config",
                String.format("active-%s.config", environment));
        Path contextConfig = Paths.get(resourcePath, "config",
                String.format("active.%s.config", context));
        Path environmentContextConfig = Paths.get(resourcePath, "config",
                String.format("active-%s.%s.config", environment, context));
        if (Files.exists(activeConfig)) {
            readFromURI(activeConfig.toUri());
        }
        if (Files.exists(environmentConfig)) {
            readFromURI(environmentConfig.toUri());
        }
        if (Files.exists(contextConfig)) {
            readFromURI(contextConfig.toUri());
        }
        if (Files.exists(environmentContextConfig)) {
            readFromURI(environmentContextConfig.toUri());
        }
    }

    static public void readFromProperties(Properties properties) throws Exception {
        HashMap<String, Object> configMap = new HashMap<>();
        for (final String name : properties.stringPropertyNames()) {
            configMap.put(name, properties.getProperty(name));
        }
        readFromMap(configMap);
    }

    static public void readFromURI(URI uri) throws Exception {
        StringJoiner joiner = new StringJoiner(System.lineSeparator(), "", "");
        ResourceReader.readLines(uri.toURL(), line -> joiner.add(line));
        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        mapper.findAndRegisterModules();
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {
        };
        HashMap<String, Object> map = mapper.readValue(joiner.toString(), typeRef);
        readFromMap(map);
    }

    static void readFromMap(HashMap<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            try {
                Component component = Component.parse(entry.getKey());
                if (COMPONENTS.containsKey(component)) {
                    Map<String, Object> current = COMPONENTS.get(component);
                    Map<String, Object> override = (HashMap<String, Object>) entry.getValue();
                    Map<String, Object> cumulative = new HashMap<>(current);
                    override.forEach((key, value) -> cumulative.merge(key, value, (v1, v2) -> v2));
                    COMPONENTS.put(component, cumulative);
                } else {
                    COMPONENTS.put(component, (HashMap<String, Object>) entry.getValue());
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

    static String[] indent = new String[]{" ", "  ", "   ", "    "};

    static void dump(Map<String, Object> map, int depth) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                dump((Map<String, Object>) entry.getValue(), depth + 1);
            } else if (entry.getValue() instanceof ArrayList) {
                List list = (List) entry.getValue();
                for (Object object : list) {
                    if (object instanceof Map) {
                        dump((Map<String, Object>) object, depth + 1);
                    } else if (object instanceof String) {
                        System.out.println(String.format("%s%s: %s",
                                indent[depth],
                                entry.getKey().toString(),
                                object == null ? "null" : object.toString()));
                    }
                }
            } else {
                System.out.println(String.format("%s%s: %s <- (%s)",
                        indent[depth],
                        entry.getKey().toString(),
                        entry.getValue() == null ? "null" : entry.getValue().toString(),
                        entry.getValue() == null ? "" : entry.getValue().getClass().getSimpleName()));
            }
        }
    }

    protected void init0() {
        Properties properties0 = null;
        try {
            String root = System.getProperty("Active.Resources");
            if (root != null) {
                this.resourcesPath = Paths.get(root);
            } else {
                this.resourcesPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            }
            /*
                x.cfg
             */
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
            /*
                x.dev.cfg
             */
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
            /*
                command line overrides
             */
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

}
