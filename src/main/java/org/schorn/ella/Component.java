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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

/**
 *
 * @author bschorn
 */
public enum Component implements ActiveProperties, ClassLocator {
    PROVIDER,
    APP,
    NODE,
    IO,
    REPO,
    SERVER,
    WEB,
    HTML;

    static private final Properties OVERRIDES = new Properties();

    private final Properties properties = new Properties();
    private Path resourcesPath = null;
    private ClassLocator classLocator = null;
    private Exception exception = null;

    static public void init(Properties overrides) {
        for (Map.Entry<Object, Object> entry : overrides.entrySet()) {
            OVERRIDES.setProperty(entry.getKey().toString(), entry.getValue().toString());
        }
        for (Component componentProperty : Component.values()) {
            componentProperty.init0();
        }
    }

    static public Component parse(String name) {
        for (Component cp : Component.values()) {
            if (cp.name().equalsIgnoreCase(name)) {
                return cp;
            }
        }
        return null;
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
            for (Map.Entry<Object, Object> entry : OVERRIDES.entrySet()) {
                String[] keyParts = entry.getKey().toString().split("\\.");
                if (keyParts[0].equalsIgnoreCase(this.name())) {
                    StringJoiner keyJoiner = new StringJoiner(".", "", "");
                    for (int i = 1; i < keyParts.length; i++) {
                        keyJoiner.add(keyParts[i]);
                    }
                    this.properties.setProperty(keyJoiner.toString(), entry.getValue().toString());
                }
            }
        } catch (Exception ex) {
            this.exception = ex;
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

    public Path getResourcePath() {
        return this.resourcesPath;
    }

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

    @Override
    public Properties properties() {
        return this.properties;
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

    @Override
    public void checkForException() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
    }

}
