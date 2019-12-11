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
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

/**
 *
 * @author bschorn
 */
public class Resources {

    private final Path resourcesPath;

    public Resources() throws URISyntaxException {
        this.resourcesPath = Paths.get(Thread.currentThread().getContextClassLoader().getResource("").toURI());
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
}
