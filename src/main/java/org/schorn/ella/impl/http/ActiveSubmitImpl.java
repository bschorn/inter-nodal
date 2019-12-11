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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.http.ActiveHTTP.ActiveMsg;
import org.schorn.ella.http.ActiveHTTP.ActiveSubmit;
import org.schorn.ella.http.ActiveHTTP.ActiveMsg.MsgStatus;
import org.schorn.ella.io.EndPoint.URIPoint;
import org.schorn.ella.util.HTTPStatusCode;

/**
 *
 * @author schorn
 *
 */
class ActiveSubmitImpl implements ActiveSubmit {

    static private final Logger LGR = LoggerFactory.getLogger(ActiveSubmitImpl.class);

    private final URIPoint rootURI;
    private final Queue<ActiveMsg<?>> queue = new LinkedList<>();

    public ActiveSubmitImpl(URIPoint rootURI) throws MalformedURLException {
        this.rootURI = rootURI;
    }

    @Override
    public void accept(ActiveMsg<?> activeMsg) {
        this.queue.add(activeMsg);
    }

    @Override
    public ActiveMsg<?> call() {
        ActiveMsgImpl<?> activeMsg = (ActiveMsgImpl<?>) this.queue.poll();
        try {
            activeMsg.setStatus(MsgStatus.ACCEPTED);
            HttpURLConnection con = (HttpURLConnection) activeMsg.url(this.rootURI).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(activeMsg.getBytes());
            os.flush();
            os.close();
            activeMsg.setStatus(MsgStatus.SUBMITTED);

            HTTPStatusCode httpStatusCode = HTTPStatusCode.get(con.getResponseCode());
            switch (httpStatusCode) {
                case HTTP_OK:
                case HTTP_CREATED:
                case HTTP_ACCEPTED:
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    for (String line = in.readLine(); line != null; line = in.readLine()) {
                        responseBuilder.append(line);
                    }
                    in.close();
                    activeMsg.setResponse(responseBuilder.toString());
                    activeMsg.setStatus(MsgStatus.COMPLETE);
                    break;
                case HTTP_NOT_AUTHORITATIVE:
                case HTTP_NO_CONTENT:
                case HTTP_RESET:
                    activeMsg.setResponse(httpStatusCode.name());
                    activeMsg.setStatus(MsgStatus.REJECTED);
                    break;
                case HTTP_PARTIAL:
                    activeMsg.setStatus(MsgStatus.PARTIAL);
                default:
                    activeMsg.setStatus(MsgStatus.ERROR);
                    LGR.error("{}.call() - request: {}\nresponse: {}[{}]",
                            this.getClass().getSimpleName(),
                            activeMsg.toString(),
                            httpStatusCode.name(),
                            httpStatusCode.statusCode()
                    );
                    break;
            }
        } catch (Exception ex) {
            activeMsg.setException(ex);
        }
        return activeMsg;
    }
}
