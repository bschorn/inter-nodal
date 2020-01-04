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

import org.schorn.ella.AbstractProvider;
import org.schorn.ella.io.ActiveIO;
import org.schorn.ella.io.EndPoint;
import org.schorn.ella.io.IOProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class IOProviderImpl extends AbstractProvider implements IOProvider {

    private static final Logger LGR = LoggerFactory.getLogger(IOProviderImpl.class);

    @Override
    public void init() throws Exception {
        this.mapInterfaceToImpl(ActiveIO.Config.class, IOConfigImpl.class);
        this.mapInterfaceToImpl(ActiveIO.ActivityFile.class, ActivityFileImpl.class);
        this.mapInterfaceToImpl(ActiveIO.ActivityRecovery.class, ActivityRecoveryImpl.class);
        this.mapInterfaceToImpl(ActiveIO.ActivityRecord.class, ActivityRecordImpl.class);
        this.mapInterfaceToImpl(EndPoint.URIPoint.class, EndPoints.URIPointImpl.class);
        this.mapInterfaceToImpl(EndPoint.FilePoint.class, EndPoints.FilePointImpl.class);
    }

}
