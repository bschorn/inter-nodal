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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.schorn.ella.Component;

/**
 *
 * @author bschorn
 */
public class AppConfig implements ActiveApp.Config {

    static public AppConfig create(Map<String, Object> params) throws Exception {

        Map<String, Object> configMap = Component.ActiveApp.configMap();
        String environment = (String) configMap.get("environment");
        LocalDate date = null;
        {
            Object dateObj = configMap.get("date");
            if (dateObj instanceof LocalDate) {
                date = (LocalDate) dateObj;
            } else if (dateObj instanceof String) {
                String dateStr = (String) dateObj;
                dateStr = dateStr.replace("-", "");
                date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
            }
        }
        String language = (String) configMap.get("language");
        //URI resources = URI.create((String) configMap.get("resources"));
        String rootPath = (String) configMap.get("rootPath");
        String configPath = (String) configMap.get("configPath");
        String context = (String) configMap.get("context");
        List<String> contexts = (List<String>) configMap.get("contexts");

        if (params != null && !params.isEmpty()) {
            if (params.containsKey("environment")) {
                environment = (String) params.get("environment");
            }
            if (params.containsKey("date")) {
                Object dateObj = params.get("date");
                if (dateObj instanceof LocalDate) {
                    date = (LocalDate) dateObj;
                } else if (dateObj instanceof String) {
                    String dateStr = (String) dateObj;
                    dateStr = dateStr.replace("-", "");
                    date = LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
                }
            }
            if (params.containsKey("language")) {
                language = (String) params.get("language");
            }
            if (params.containsKey("rootPath")) {
                rootPath = (String) params.get("rootPath");
            }
            if (params.containsKey("configPath")) {
                rootPath = (String) params.get("configPath");
            }
            if (params.containsKey("context")) {
                context = (String) params.get("context");
            }
            if (params.containsKey("contexts")) {
                contexts = (List<String>) params.get("contexts");
            }
            if (contexts == null) {
                contexts = new ArrayList<>();
                if (context != null) {
                    contexts.add(context);
                }
            }
        }
        return new AppConfig(environment, date, language, rootPath, configPath, context, contexts);
    }

    private final LocalDate date;
    private final String language;
    private final String environment;
    private final String rootPath;
    private final String configPath;
    private final String context;
    private final List<String> contexts;

    private AppConfig(String environment, LocalDate date, String language,
            String rootPath, String configPath, String context, List<String> contexts) throws Exception {
        this.environment = environment == null ? "noenvironment" : environment;
        this.date = date == null ? LocalDate.now() : date;
        this.language = language == null ? "en" : language;
        this.rootPath = rootPath == null ? "." : rootPath;
        this.configPath = configPath == null ? "." : configPath;
        this.context = context == null ? "nocontext" : context;
        this.contexts = contexts == null ? new ArrayList<>() : contexts;
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
    public String rootPath() {
        return this.rootPath;
    }

    @Override
    public String configPath() {
        return this.configPath;
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
