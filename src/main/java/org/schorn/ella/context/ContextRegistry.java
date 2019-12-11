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
package org.schorn.ella.context;

import java.util.List;
import java.util.Optional;

import org.schorn.ella.Singleton;

/**
 *
 * @author schorn
 *
 */
public interface ContextRegistry extends Singleton {

    static void register(AppContext context) throws Exception {
        ContextProvider.provider().getSingleton(ActiveContext.Registry.class).register(context);
    }

    static List<AppContext> values() {
        return ContextProvider.provider().getSingleton(ActiveContext.Registry.class).values();
    }

    static Optional<AppContext> valueOf(String name) {
        return ContextProvider.provider().getSingleton(ActiveContext.Registry.class).valueOf(name);
    }

    static boolean isRegistered(String name) {
        return ContextProvider.provider().getSingleton(ActiveContext.Registry.class).valueOf(name).isPresent();
    }

    static AppContext get(int contextIdx) {
        return ContextProvider.provider().getSingleton(ActiveContext.Registry.class).getContext(contextIdx);
    }

    static AppContext get(String name) throws Exception {
        if (ContextRegistry.isRegistered(name)) {
            return ContextRegistry.valueOf(name).get();
        }
        for (AppContext context : ContextRegistry.values()) {
            if (context.name().equalsIgnoreCase(name)) {
                return context;
            }
        }
        throw new Exception(String.format("'%s' is not a registered context."));
    }
}
