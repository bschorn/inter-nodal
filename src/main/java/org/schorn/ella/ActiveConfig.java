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

import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.schorn.ella.app.ActiveApp;

/**
 *
 * @author bschorn
 */
public class ActiveConfig {

    static private final String SUBDIR = "config";
    static private final String ACTIVE_CONFIG = "active.config";
    static private final String ACTIVE_ENVIRONMENT_CONFIG = "active-<environment>.config";
    static private final String ACTIVE_CONTEXT_CONFIG = "active.<context>.config";
    static private final String ACTIVE_ENVIRONMENT_CONTEXT_CONFIG = "active-<environment>.<context>.config";

    private final URI resourcesURI;

    private final String[] templates = new String[]{
        ACTIVE_CONFIG, ACTIVE_ENVIRONMENT_CONFIG
    };

    private final String[] contextTemplates = new String[]{
        ACTIVE_CONTEXT_CONFIG,
        ACTIVE_ENVIRONMENT_CONTEXT_CONFIG
    };

    public ActiveConfig(Path resourcesPath) {
        this.resourcesURI = resourcesPath.toUri();
    }

    public ActiveConfig(URI resourcesURI) {
        this.resourcesURI = resourcesURI;
    }

    public ActiveConfig() {
        this.resourcesURI = URI.create(ActiveApp.Config.get().configPath());
    }

    public List<URI> cascading() {
        List<URI> files = new ArrayList<>();
        for (String template : this.templates) {
            files.add(URI.create(create(template).replace("<environment>", ActiveApp.Config.get().environment())));
        }
        for (String contextName : ActiveApp.Config.get().contexts()) {
            for (String template : this.contextTemplates) {
                files.add(URI.create(create(template)
                        .replace("<environment>", ActiveApp.Config.get().environment())
                        .replace("<context>", contextName)));
            }
        }
        return files;
    }

    private String create(String template) {
        return String.format("file:/%s/%s/%s",
                this.resourcesURI.toString(),
                SUBDIR, template);

    }
}
