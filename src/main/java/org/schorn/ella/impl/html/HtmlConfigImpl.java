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
package org.schorn.ella.impl.html;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Map;
import org.schorn.ella.Component;
import org.schorn.ella.app.ActiveApp;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.html.ActiveHtml;

/**
 *
 * @author bschorn
 */
public class HtmlConfigImpl extends AbstractContextual implements ActiveHtml.Config {

    private String labelsFile;
    private URI labelsURI;
    private String htmlFormClass = "node-form";
    private String htmlFormLabelClass = "node-form-label";
    private String htmlInputClass = "node-input";
    private String htmlInputLabelClass = "node-input-label";
    private String htmlSelectClass = "node-select";
    private String htmlSelectLabelClass = "node-select-label";
    private String htmlTableClass = "node-table";
    private String htmlTableTHeadClass = "node-table-thead";
    private String htmlTableTFootClass = "node-table-tfoot";
    private String htmlTableTBodyClass = "node-table-tbody";
    private String htmlTableTRowClass = "node-table-trow";
    private String htmlTableTColClass = "node-table-tcol";
    private String htmlTableSmallCaptionClass = "active-table-caption-small";

    public HtmlConfigImpl(AppContext context) throws URISyntaxException, ClassNotFoundException {
        super(context);
        Map<String, Object> map = Component.ActiveHtml.configMap(context.name());
        String labelsFile0 = null;
        URI labelsURI0 = null;
        if (map.containsKey("labels")) {
            labelsFile0 = (String) map.get("labels");
            labelsFile0 = labelsFile0.replace("{RootPath}",
                    ActiveApp.Config.get().rootPath());
            labelsFile0 = labelsFile0.replace("{Language}",
                    ActiveApp.Config.get().language());
            labelsFile0 = labelsFile0.replace("{Context}",
                    context.name());
            labelsURI0 = URI.create(labelsFile0);
        }
        if (labelsURI0 != null) {
            this.labelsFile = Paths.get(labelsURI0).toString();
        } else {
            this.labelsFile = null;
        }
        this.labelsURI = labelsURI0;
    }

    @Override
    public URI htmlLabels() {
        return this.labelsURI;
    }

    @Override
    public String htmlFormClass() {
        return this.htmlFormClass;
    }

    @Override
    public String htmlFormLabelClass() {
        return this.htmlFormLabelClass;
    }

    @Override
    public String htmlInputClass() {
        return this.htmlInputClass;
    }

    @Override
    public String htmlInputLabelClass() {
        return this.htmlInputLabelClass;
    }

    @Override
    public String htmlSelectClass() {
        return this.htmlSelectClass;
    }

    @Override
    public String htmlSelectLabelClass() {
        return this.htmlSelectLabelClass;
    }

    @Override
    public String htmlTableClass() {
        return this.htmlTableClass;
    }

    @Override
    public String htmlTableTHeadClass() {
        return this.htmlTableTHeadClass;
    }

    @Override
    public String htmlTableTFootClass() {
        return this.htmlTableTFootClass;
    }

    @Override
    public String htmlTableTBodyClass() {
        return this.htmlTableTBodyClass;
    }

    @Override
    public String htmlTableTRowClass() {
        return this.htmlTableTRowClass;
    }

    @Override
    public String htmlTableTColClass() {
        return this.htmlTableTColClass;
    }

    @Override
    public String htmlTableSmallCaptionClass() {
        return this.htmlTableSmallCaptionClass;
    }

}
