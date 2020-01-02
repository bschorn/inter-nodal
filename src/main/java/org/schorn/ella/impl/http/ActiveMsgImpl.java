/* 
 * The MIT License
 *
 * Copyright 2019 Bryan Schorn.
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
package org.schorn.ella.impl.http;

import java.net.URI;
import java.net.URL;
import org.schorn.ella.http.ActiveHTTP.ActiveMsg;
import org.schorn.ella.http.ActiveHTTP.ResponseParser;
import org.schorn.ella.io.EndPoint.URIPoint;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.OpenNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class ActiveMsgImpl<T> implements ActiveMsg<T> {

    static private final Logger LGR = LoggerFactory.getLogger(ActiveMsgImpl.class);

    private MsgAction msgAction;
    private MsgStatus msgStatus;
    private T inputData;
    private Object outputData = null;
    private Exception exception;
    private String context;
    private Object response;
    private String aggregate_type;

    ActiveMsgImpl(MsgAction msgAction, String aggregate_type, T inputData) {
        this.msgAction = msgAction;
        this.msgStatus = MsgStatus.UNSUBMITTED;
        this.aggregate_type = aggregate_type;
        this.inputData = inputData;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E getOutput(ResponseParser<E> responseParser) throws Exception {
        if (this.outputData == null) {
            if (this.response == null) {
                this.throwException();
                return null;
            }
            if (responseParser == null) {
                throw new Exception(String.format("%s.getOutput() - there is no ResponseParser implementation provided.",
                        this.getClass().getSimpleName()));
            }
            E outputData = (E) responseParser.apply(this.response);
            if (outputData == null) {
                this.throwException();
                throw new Exception(String.format("%s.getOutput() - the ResponseParser implementation was unable to process:\n%s",
                        this.getClass().getSimpleName(), this.response.toString()));
            }
            this.outputData = outputData;
        }
        return (E) this.outputData;
    }

    @Override
    public MsgStatus msgStatus() {
        return this.msgStatus;
    }

    @Override
    public MsgAction msgAction() {
        return this.msgAction;
    }

    @Override
    public void throwException() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
    }

    void setContext(String context) {
        this.context = context;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public void setStatus(MsgStatus status) {
        this.msgStatus = status;
    }

    public byte[] getBytes() {
        if (this.inputData instanceof StructData) {
            StructData structData = (StructData) this.inputData;
            return structData.asJsonString().getBytes();
        } else if (this.inputData instanceof OpenNode) {
            OpenNode openNode = (OpenNode) this.inputData;
            return openNode.asJsonString().getBytes();
        }
        return new byte[0];
    }

    public String aggregate_type() {
        return this.aggregate_type;
    }

    public URL url(URIPoint rootURI) throws Exception {
        String templatePath = this.msgAction.getPath();
        String object_type = this.aggregate_type();
        if (object_type == null) {
            throw new Exception(String.format("%s.url() - unable to determine the 'ObjectType' of this message",
                    this.getClass().getSimpleName()));
        }
        String msgPath = templatePath.replace("{context}", this.context).replace("{object_type}", object_type);
        URI root = rootURI.get();
        String uriPath = String.format("%s/%s", root.getPath(), msgPath);
        URI uri = new URI(root.getScheme(), null, root.getHost(), root.getPort(), uriPath, null, null);
        return uri.toURL();
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String toString() {
        return String.format("[context] %s [aggregate] %s [MsgAction] %s [MsgStatus] %s\n[InputData]\n%s\n[OutputData]\n%s",
                this.context,
                this.aggregate_type,
                this.msgAction.name(),
                this.msgStatus.name(),
                this.inputData.toString(),
                this.outputData.toString());
    }
}
