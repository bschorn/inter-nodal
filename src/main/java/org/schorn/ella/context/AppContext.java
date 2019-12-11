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
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.schorn.ella.Mingleton;
import org.schorn.ella.extension.ActiveQuick;
import org.schorn.ella.extension.ActiveService;
import org.schorn.ella.extension.AppContextExt;
import org.schorn.ella.extension.TransformExt;
import org.schorn.ella.node.MetaReader;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public interface AppContext extends Mingleton, ActiveContext.Action, ActiveContext.Activity, ActiveContext.Attribute, ActiveContext.Data, ActiveContext.Error, ActiveContext.Meta, ActiveContext.Property, AppContextExt, ActiveQuick, ActiveService, TransformExt {

    static final Logger LGR = LoggerFactory.getLogger(AppContext.class);

    public int contextIdx();

    /**
     *
     *
     *
     */
    public enum ContextRole {
        INTERNAL(false, true),
        COMMON(false, true),
        MODEL(false, true),
        DEFAULT(true, false);

        boolean hasRepo;
        boolean isInternal;

        ContextRole(boolean hasRepo, boolean isInternal) {
            this.hasRepo = hasRepo;
            this.isInternal = isInternal;
        }

        public boolean hasRepo() {
            return this.hasRepo;
        }

        public boolean isInternal() {
            return this.isInternal;
        }
    }

    /**
     * Common Context for Built-in datatypes
     */
    static public final AppContext Common = AppContext.createSystemContext("Common", ContextRole.COMMON);
    static public final AppContext Internal = AppContext.createSystemContext("Internal", ContextRole.INTERNAL);
    static public final AppContext Model = AppContext.createSystemContext("Model", ContextRole.MODEL);

    /**
     *
     * @return
     */
    static public List<AppContext> values() {
        Predicate<AppContext> appContexts = nc -> nc.contextMode().equals(ContextRole.DEFAULT);
        return ContextRegistry.values().stream().filter(appContexts).collect(Collectors.toList());
    }

    /**
     *
     * @param name
     * @return
     */
    static public Optional<AppContext> valueOf(String name) {
        return ContextRegistry.valueOf(name);
    }

    /**
     *
     * @param name
     * @return
     * @throws Exception
     */
    static public AppContext create(String name) throws Exception {
        AppContext context = ContextProvider.provider().createContext(AppContext.class, name, ContextRole.DEFAULT);
        //String activityFile = System.getProperty(AppContext.class.getSimpleName() + ".ActivityFile").replace("{CONTEXT}", context.name());
        //File f = new File(activityFile);
        //context.setEndPoint(EndPoint.URIPoint.create(f.toURI()));
        return context;
    }

    /**
     *
     * @param name
     * @param mode
     * @return
     */
    static public AppContext createSystemContext(String name, ContextRole mode) {
        try {
            return ContextProvider.provider().createContext(AppContext.class, name, mode);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    /**
     * Init Context by Reading Meta Files
     * @param metaMaps
     */
    static public void meta(Map<String, MetaReader.MetaSupplier> metaMaps) {
        for (String context : metaMaps.keySet()) {
            try {
                MetaReader.MetaSupplier metaSupplier = metaMaps.get(context);
                MetaReader metaReader = MetaReader.create(metaSupplier);
                if (metaReader != null) {
                    metaReader.register();
                }
            } catch (Exception ex) {
                LGR.error("{}.meta() - Registering Meta Data: '{}' caught Exception: {}",
                        AppContext.class.getSimpleName(),
                        Functions.getStackTraceAsString(ex));
            }
        }
    }

    static public void recover() {
        for (AppContext context : AppContext.values()) {
            try {
                if (context.hasActivity()) {
                    context.reloadActivity();
                }
            } catch (Exception ex) {
                LGR.error("{}.init() - Reloading Activity: '{}' caught Exception: {}",
                        AppContext.class.getSimpleName(), Functions.getStackTraceAsString(ex));
            }
        }
    }

    static public void record() {
        for (AppContext context : AppContext.values()) {
            try {
                if (context.hasRepo()) {
                    context.recordActivity();
                }
            } catch (Exception ex) {
                LGR.error("{}.init() - Recording Activity: '{}' caught Exception: {}",
                        AppContext.class.getSimpleName(), Functions.getStackTraceAsString(ex));
            }
        }
    }

}
