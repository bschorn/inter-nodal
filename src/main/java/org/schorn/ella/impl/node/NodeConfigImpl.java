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
package org.schorn.ella.impl.node;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.schorn.ella.Component;
import org.schorn.ella.app.ActiveApp;
import org.schorn.ella.node.ActiveNode;


/**
 *
 * @author bschorn
 */
public class NodeConfigImpl implements ActiveNode.Config {

    private String context;
    private URI metadataURI = null;
    private Boolean autoDynamicType = false;
    private Boolean autoVersioning = true;

    public NodeConfigImpl(String contextName) throws URISyntaxException, ClassNotFoundException {
        this.context = contextName;
        Map<String, Object> map = Component.ActiveNode.configMap(contextName);
        if (map.containsKey("metadata")) {
            String metadataStr = (String) map.get("metadata");
            if (metadataStr != null) {
                metadataStr = metadataStr.replace("{RootPath}", ActiveApp.Config.get().rootPath());
                this.metadataURI = new URI(metadataStr);
            }
        }
        if (map.containsKey("autoDynamicType")) {
            this.autoDynamicType = (Boolean) map.get("autoDynamicType");
        }
        if (map.containsKey("autoVersioning")) {
            this.autoVersioning = (Boolean) map.get("autoVersioning");
        }
    }

    @Override
    public URI metadata() {
        return this.metadataURI;
    }

    @Override
    public boolean autoDynamicType() {
        return this.autoDynamicType;
    }

    @Override
    public boolean autoVersioning() {
        return this.autoVersioning;
    }
}
