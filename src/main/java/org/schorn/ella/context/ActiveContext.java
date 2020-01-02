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
import java.util.function.Predicate;
import org.schorn.ella.Singleton;
import org.schorn.ella.context.AppContext.ContextRole;
import org.schorn.ella.http.ActiveHTTP;
import org.schorn.ella.io.EndPoint.URIPoint;
import org.schorn.ella.node.ActiveNode.ActiveRef;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Identity;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueType.FieldType;
import org.schorn.ella.repo.ActiveRepo;
import org.schorn.ella.server.ActiveServer;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The context (AppContext) plays a central role in the all of the Active
 * packages. To keep from the AppContext becoming a monolith itself it's broken
 * down into various components.
 *
 * <ul>
 * <li>Attribute</li>
 * <li>Meta</li>
 * <li>Data</li>
 * <li>Error</li>
 * <li>Property</li>
 * <li>Activity</li>
 * </ul>
 *
 * @author schorn
 *
 */
public interface ActiveContext {

    static final Logger LGR = LoggerFactory.getLogger(ActiveContext.class);

    /**
     * All interfaces that have context specific requirements should implement.
     */
    interface Contextual {

        AppContext context();
    }

    /**
     * Registry (of contexts)
     */
    interface Registry extends Singleton {

        void register(AppContext context) throws Exception;

        List<AppContext> values();

        Optional<AppContext> valueOf(String name);

        AppContext getContext(int contextIdx);

        int getContextIdx(AppContext context);

        /*
		 * Get Registry
         */
        static Registry get() {
            return ContextProvider.provider().getSingleton(Registry.class);
        }
    }

    /**
     * Attributes of a context
     */
    interface Attribute {

        String name();

        int ordinal();

        ContextRole contextMode();
    }

    /**
     * Meta Data of a context
     */
    interface Meta {

        List<ObjectType> objectTypes();

        List<FieldType> fieldTypes();

        List<ValueType> valueTypes();

        List<ArrayType> arrayTypes();

        FieldType getFieldType(String name);

        FieldType getFieldType(short activeIdx);

        ValueType getValueType(String name);

        ValueType getValueType(short activeIdx);

        ObjectType getObjectType(String name);

        ObjectType getObjectType(short activeIdx);

        ArrayType getArrayType(String name);

        ArrayType getArrayType(short activeIdx);

        ActiveType addType(ActiveType activeType);

        ActiveRef getActiveRef();

        List<Identity> identities();

        Identity addIdentity(Identity identity);
    }

    /**
     * Data (Repo) of a context
     */
    interface Data extends Contextual {

        boolean hasRepo();

        ActiveRepo repo();

        ActiveHTTP.ContextServer http();

        <T> void setRepoFilter(Class<T> classOfT, Predicate<T> repoFilter);

        <T> Predicate<T> getRepoFilter(Class<T> classForT);

        /**
         *
         * @return
         */
        default URIPoint httpAddress() {
            try {
                return URIPoint.create(ActiveServer.Config.get().masterServerAddress());
            } catch (Exception e) {
                LGR.error("{}.httpAddress() - unable to ascertain the address of the master server. Caught Exception: {}",
                        Data.class.getSimpleName(), Functions.getStackTraceAsString(e));
            }

            return null;
        }
    }

    /**
     * Error Objects of a context
     */
    interface Error {

        ValueType valueErrorType();

        ObjectType objectErrorType();

        ArrayType arrayErrorType();

        ObjectData createErrorObject(String errorMessage);

        ArrayData createErrorArray(String[] errorMessages);
    }

    /**
     * Properties of a context
     */
    interface Property {

        <T> T getPropertyT(Class<T> classForT, Object propertyId);

        /*
        String getProperty(String propertyKey);

        String getProperty(String propertyKey, String defaultValue);

        void setPropertyT(Object propertyId, Object propertyObj);

        void setProperty(String propertyKey, String propertyValue);
        */
    }

    /**
     * Activity of a context
     */
    interface Activity {

        //void setEndPoint(EndPoint<?> endPoint);

        //EndPoint<?> getEndPoint();

        boolean hasActivity();

        void reloadActivity();

        /**
         * Recording Activity will continue as long as the Context is
         * open.context.close() or context.exit() will stop the recording (and
         * release the file)
         *
         * @throws java.lang.Exception
         */
        void recordActivity() throws Exception;
    }

    /**
     * Actions of a context
     */
    interface Action extends Contextual {

        /**
         *
         *
         * @return
         */
        default boolean submitToRepo() {
            return ActiveServer.Config.get().masterServer();
        }

        void open();

        boolean isOpen();

        void exit(String why);

        /**
         * submit to the repo
         * <br>
         * If this code is being executed inside a master server instance then
         * submit to the repo directly.
         * <br>
         * If this code is being executed outside a master server instance then
         * submit via (http) to the master server instance
         *
         * @param objectData
         */
        void submit(ObjectData objectData);

        void load(ObjectData objectData, Identity identity);
    }

}
