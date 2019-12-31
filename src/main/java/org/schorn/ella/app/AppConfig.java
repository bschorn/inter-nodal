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
package org.schorn.ella.app;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.schorn.ella.Component;

/**
 *
 * @author bschorn
 */
public class AppConfig implements ActiveApp.Config {

    static public AppConfig create(Map<String, Object> params) {

        Map<String, Object> configMap = Component.ActiveApp.configMap();
        String environment = (String) configMap.get("environment");
        LocalDate date = (LocalDate) configMap.get("date");
        String language = (String) configMap.get("language");
        URI resources = URI.create((String) configMap.get("resources"));
        String context = (String) configMap.get("context");
        List<String> contexts = (List<String>) configMap.get("contexts");

        if (params != null && !params.isEmpty()) {
            if (params.containsKey("environment")) {
                environment = (String) params.get("environment");
            }
            if (params.containsKey("date")) {
                date = (LocalDate) params.get("date");
            }
            if (params.containsKey("language")) {
                language = (String) params.get("language");
            }
            if (params.containsKey("resources")) {
                resources = URI.create((String) params.get("resources"));
            }
            if (params.containsKey("context")) {
                context = (String) params.get("context");
            }
            if (params.containsKey("contexts")) {
                contexts = (List<String>) params.get("contexts");
            }
        }
        return new AppConfig(environment, date, language, resources, context, contexts);
    }

    private final LocalDate date;
    private final String language;
    private final String environment;
    private final URI resources;
    private final String context;
    private final List<String> contexts;

    private AppConfig(String environment, LocalDate date, String language, URI resources, String context, List<String> contexts) {
        this.environment = environment;
        this.date = date;
        this.language = language;
        this.resources = resources;
        this.context = context;
        this.contexts = contexts;
    }

    @Override
    public LocalDate date() {
        return this.date;
    }

    @Override
    public String environment() {
        return this.environment;
    }

    @Override
    public String language() {
        return this.language;
    }

    @Override
    public URI resources() {
        return this.resources;
    }

    @Override
    public String context() {
        return this.context;
    }

    @Override
    public List<String> contexts() {
        return this.contexts;
    }
}
