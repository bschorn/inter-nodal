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
package org.schorn.ella.impl.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.ActiveIO;
import org.schorn.ella.io.ActiveIO.ActivityLogWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public class ActivityLogWriterImpl extends AbstractContextual implements ActivityLogWriter<String> {

    private static final Logger LGR = LoggerFactory.getLogger(ActivityLogWriterImpl.class);

    private final BufferedWriter writer;

    /**
     *
     * @param context
     * @param endPoint
     * @throws Exception
     */
    public ActivityLogWriterImpl(AppContext context) {
        super(context);
        BufferedWriter bufferedWriter = null;
        Path path = ActiveIO.ActivityFile.get(context).asPath();
        if (path != null) {
            try {
                if (Files.exists(path)) {
                    bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND);
                } else {
                    bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.CREATE_NEW);
                }
            } catch (IOException ex) {
                this.setException(ex);
            }
        } else {
            String errMsg = String.format("%s - there was no path specified for the endpoint.",
                    this.getClass().getSimpleName());
            LGR.error(errMsg);
            this.setException(new Exception(errMsg));
        }
        this.writer = bufferedWriter;
    }

    @Override
    public boolean isReady() {
        return (this.writer != null);
    }

    /**
     *
     * @param line
     */
    @Override
    public void accept(String line) {
        try {
            if (!this.isReady()) {
                return;
            }
            if (line != null) {
                this.writer.write(line);
                this.writer.newLine();
                this.writer.flush();
            }
        } catch (Exception ex) {
            LGR.error(ex.getMessage());
        }
    }

    @Override
    public void close() throws Exception {
        if (this.writer != null) {
            this.writer.flush();
            this.writer.close();
        }
    }

    @Override
    public void throwException() throws Exception {
        // TODO Auto-generated method stub

    }
}
