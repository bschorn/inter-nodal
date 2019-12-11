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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 *
 * @author bschorn
 */
public enum ComponentProperties implements ActiveProperties, ClassLocator {
    PROVIDER,
    NODE,
    SERVER,
    WEB,
    HTML;

    protected Path rootPath = null;
    protected Exception exception = null;
    protected Properties properties = null;
    protected ClassLocator classLocator = null;

    ComponentProperties() {
        Properties properties0 = null;
        try {
            String root = System.getProperty("Active.Root");
            if (root != null) {
                this.rootPath = Paths.get(root);
            } else {
                this.rootPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            }
            String environment = System.getProperty("Active.Environment");
            String propertiesFilePath = String.format("%s%s%s%s%s-%s.properties",
                    this.rootPath.toString(), File.separator, "props", File.separator, this.name().toLowerCase(), environment);
            Path path = Paths.get(propertiesFilePath);
            if (Files.exists(path)) {
                properties0 = new Properties();
                properties0.load(new FileInputStream(propertiesFilePath));
            } else {
                throw new Exception(String.format("File not found: %s", propertiesFilePath));
            }
        } catch (Exception ex) {
            this.exception = ex;
            ex.printStackTrace();
        } finally {
            this.properties = properties0;
            this.classLocator = ClassLocator.create(this.properties);
        }
    }

    public Path getRootPath() {
        return this.rootPath;
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
