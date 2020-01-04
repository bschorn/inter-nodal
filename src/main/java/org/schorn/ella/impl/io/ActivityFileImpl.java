/*
 * The MIT License
 *
 * Copyright 2020 bschorn.
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
import java.nio.file.Files;
import java.nio.file.Path;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.ActiveIO;

/**
 *
 * @author bschorn
 */
public class ActivityFileImpl extends AbstractContextual implements ActiveIO.ActivityFile {

    private final String activityFile;
    private final Path activityPath;
    private final URI activityURI;

    public ActivityFileImpl(AppContext context) throws Exception {
        super(context);
        String tmpActivityFile = ActiveIO.Config.get(this.context()).activityFile();
        if (tmpActivityFile == null) {
            throw new Exception(
                    String.format(
                            "%s.ctor(%s) - missing ActiveIO.Config.activityFile()",
                            this.getClass().getSimpleName(),
                            context.name())
            );
        }
        this.activityURI = URI.create(tmpActivityFile);
        this.activityPath = Path.of(this.activityURI);
        this.activityFile = this.activityPath.toString();
    }

    @Override
    public boolean hasActivity() {
        return Files.exists(this.activityPath);
    }

    @Override
    public String asString() {
        return this.activityFile;
    }

    @Override
    public Path asPath() {
        return this.activityPath;
    }

    @Override
    public URI asURI() {
        return this.activityURI;
    }
}
