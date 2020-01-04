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
package org.schorn.ella.impl.io;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.schorn.ella.Component;
import org.schorn.ella.app.ActiveApp;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.ActiveIO;

/**
 *
 * @author bschorn
 */
public class IOConfigImpl extends AbstractContextual implements ActiveIO.Config {

    private final String activityFile;
    private final URI activityURI;

    public IOConfigImpl(AppContext context) throws URISyntaxException {
        super(context);
        Map<String, Object> configMap = Component.ActiveIO.configMap(context.name());
        String activityFile = null;
        URI activityURI = null;
        if (configMap.containsKey("activity")) {
            activityFile = (String) configMap.get("activity");
            activityFile = activityFile.replace("{Date}",
                    ActiveApp.Config.get().date().format(DateTimeFormatter.BASIC_ISO_DATE));
            activityFile = activityFile.replace("{Context}",
                    context.name());
            activityURI = URI.create(activityFile);
        }
        this.activityFile = activityFile;
        this.activityURI = activityURI;
    }

    @Override
    public String activityFile() {
        return this.activityFile;
    }

    @Override
    public URI activityURI() {
        return this.activityURI;
    }

}
