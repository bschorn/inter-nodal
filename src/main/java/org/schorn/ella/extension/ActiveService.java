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
package org.schorn.ella.extension;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.repo.RepoSupport.ActiveCondition;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.QueryData;
import org.schorn.ella.sql.ActiveSQL;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;
import org.schorn.ella.util.Functions;

/**
 * This extension sits directly on the context and provides methods for handle
 * many of the service requests that come from a service layer.
 *
 *
 * @author schorn
 *
 */
public interface ActiveService {

    static final Logger LGR = LoggerFactory.getLogger(ActiveService.class);

    public enum Issue {

        /**
         *
         */
        UnknownContext,
        UnknownObjectType,
        UnknownValueType,
        InvalidValueType,;
    }

    /**
     * Run Query is simply an ArrayData of ObjectData where ObjectData.name() ==
     * object_type and ObjectData[value_type] == value
     *
     *
     * @param object_type
     * @param value_type
     * @param value
     * @return
     * @throws Exception
     */
    default ArrayData runQuery(String object_type, String value_type, String value) throws Exception {
        if (this instanceof AppContext) {
            AppContext context = (AppContext) this;
            ActiveQuery.Builder builder = ActiveQuery.builder(context, "runQuery");
            builder.from(object_type);
            if (value != null && !value.equals("*")) {
                ObjectType objectType = ObjectType.get(context, object_type);
                ValueType valueType = ValueType.get(context, value_type);
                /*
				 * when creating ActiveCondition any ActiveOperator implementation should work. The implementation
				 * should only effect the display of the query in toString(). Since ActiveQuery emulates an
				 * SQL statement I chose the SQLOperators so the toString() displays an SQL version of the query
				 * for informational purposes only.
                 */
                builder.where(ActiveCondition.create(ActiveSQL.SQLOperators.EQUALS, objectType, valueType, value));
            }
            QueryData queryData = context.repo().query(builder.build());
            queryData.throwException();
            return queryData.arrayData();

        }
        String errorMessage = String.format("%s.runQuery('%s','%s','%s') - call failed.",
                ArrayData.class.getSimpleName(), object_type, value_type, value);
        return AppContext.Common.createErrorArray(new String[]{errorMessage});
    }

    /**
     * Run Request takes a JSON string that can be parsed in ActiveNode. The
     * ActiveNode is used as a query.
     *
     * This method is open-ended and depends on the implementation.
     *
     *
     * @param request
     * @return
     * @throws Exception
     */
    default ArrayData runRequest(String request) throws Exception {
        if (this instanceof AppContext) {
            return SupportSerivce.runRequest((AppContext) this, request);
        }
        String errorMessage = String.format("%s.runRequest('%s') - call failed.",
                ArrayData.class.getSimpleName(), request);
        return AppContext.Common.createErrorArray(new String[]{errorMessage});
    }

    /**
     *
     * @param request
     * @return
     * @throws Exception
     */
    default ArrayData runEventRequest(String request) throws Exception {
        if (this instanceof AppContext) {
            return SupportSerivce.runEventsRequest((AppContext) this, request);
        }
        String errorMessage = String.format("%s.runRequest('%s') - call failed.",
                ArrayData.class.getSimpleName(), request);
        return AppContext.Common.createErrorArray(new String[]{errorMessage});
    }

    /**
     *
     * @param object_type
     * @param value_type
     * @return
     * @throws Exception
     */
    default ArrayData runList(String object_type, String value_type) throws Exception {
        if (this instanceof AppContext) {
            AppContext context = (AppContext) this;
            String[] selectables = value_type.split(",");
            //List<String> selectList = Arrays.asList(new String[]{value_type});
            List<String> selectList = Arrays.asList(selectables);
            ActiveQuery.Builder builder = ActiveQuery.builder(context, "runList");
            builder
                    .select(selectList)
                    .distinct()
                    .from(object_type);
            QueryData queryData = context.repo().query(builder.build());
            queryData.throwException();
            return queryData.arrayData();
        }
        String errorMessage = String.format("%s.runList('%s','%s') - call failed.",
                ArrayData.class.getSimpleName(), object_type, value_type);
        return AppContext.Common.createErrorArray(new String[]{errorMessage});
    }

    /**
     *
     * @param object_type
     * @param json
     * @return
     * @throws Exception
     */
    default ObjectData saveObject(String object_type, String json) throws Exception {
        if (this instanceof AppContext) {
            return SupportSerivce.saveObject((AppContext) this, object_type, json);
        }
        return AppContext.Common.createErrorObject("Error: ActiveService.saveObject()");
    }

    /**
     *
     * @param file_type
     * @param json
     * @return
     * @throws Exception
     */
    default ActiveData saveFile(String file_type, String json) throws Exception {
        if (this instanceof AppContext) {
            return SupportSerivce.saveFile((AppContext) this, file_type, json);
        }
        return AppContext.Common.createErrorObject("Error: ActiveService.saveObject()");
    }

    @SuppressWarnings("rawtypes")
    default Transform getTransform(Format source, Format target) {
        if (this instanceof AppContext) {
            return TransformProvider.provider().getTransform((AppContext) this, source, target);
        }
        return null;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    *
    *								IMPLEMENTATION
    *
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    static class SupportSerivce {

        private static final Logger LGR = LoggerFactory.getLogger(SupportSerivce.class);

        static void throwException(Issue issue) throws Exception {
            throw new Exception(issue.toString());
        }

        static void checkObjectType(AppContext nodeContext, String object_type) throws Exception {
            if (ObjectType.get(nodeContext, object_type) == null) {
                throwException(Issue.UnknownObjectType);
            }
        }

        static void checkValueType(AppContext nodeContext, String value_type) throws Exception {
            if (ValueType.get(nodeContext, value_type) == null) {
                throwException(Issue.UnknownValueType);
            }
        }

        /**
         *
         * @param nodeContext
         * @param object_type
         * @param value_type
         * @param value
         * @return
         * @throws Exception
         */
        static ArrayData runQuery(AppContext nodeContext, String object_type, String value_type, String value)
                throws Exception {
            ActiveQuery.Builder queryBuilder = ActiveQuery.builder(nodeContext, "runQuery");
            queryBuilder.from(object_type);
            if (!value.equals("*")) {
                ObjectType parentType = ObjectType.get(nodeContext, object_type);
                ValueType valueType = ValueType.get(nodeContext, value_type);
                queryBuilder.where(ActiveCondition.create(ActiveSQL.SQLOperators.EQUALS, parentType, valueType, value));
            }

            return nodeContext.repo().query(queryBuilder.build());
        }

        /**
         *
         * @param nodeContext
         * @param request
         * @return
         * @throws Exception
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        static ArrayData runRequest(AppContext nodeContext, String request, Object... params) throws Exception {
            ArrayType resultType = ArrayType.dynamic(nodeContext, "Results");
            ArrayData results = resultType.create();
            Transform transform = TransformProvider.provider().getTransform(nodeContext, Format.JSON, Format.OpenNode);

            OpenNode openNode = (OpenNode) transform.apply(request);
            OpenNode.OpenObject openObject = (OpenNode.OpenObject) openNode;
            ActiveQuery.Builder builder = ActiveQuery.builder(nodeContext, openObject.name());
            builder.from(openObject.name());

            ObjectType parentType = ObjectType.get(nodeContext, openNode.name());
            for (OpenNode on : openObject.nodes()) {
                if (on.role().equals(Role.Value)) {
                    ValueType valueType = ValueType.get(nodeContext, on.name());
                    if (valueType != null) {
                        ValueData valueData = valueType.create(on.value());
                        if (valueData != null) {
                            try {
                                builder.and(ActiveCondition.create(ActiveSQL.SQLOperators.EQUALS, parentType, valueData.valueType(),
                                        valueData.activeValue()));
                            } catch (Exception ex) {
                                LGR.error(Functions.getStackTraceAsString(ex));
                            }
                        }
                    }
                }
            }
            if (params.length > 0 && params[0] instanceof Boolean && ((Boolean) params[0]).equals(Boolean.TRUE)) {
                results = nodeContext.repo().queryEvents(builder.build());
            } else {
                results = nodeContext.repo().query(builder.build());
            }
            return results;
        }

        static ArrayData runEventsRequest(AppContext nodeContext, String request) throws Exception {
            return runRequest(nodeContext, request, true);
        }

        /**
         *
         * @param nodeContext
         * @param object_type
         * @param json
         * @return
         * @throws Exception
         */
        static ObjectData saveObject(AppContext nodeContext, String object_type, String json) throws Exception {
            checkObjectType(nodeContext, object_type);

            ObjectType objectType = ObjectType.get(nodeContext, object_type);
            ObjectData objectData = objectType.create(json);

            nodeContext.repo().submit(objectData);

            return objectData;
        }

        /**
         *
         * @param nodeContext
         * @param object_type
         * @param json
         * @return
         * @throws Exception
         */
        static ObjectData saveFile(AppContext nodeContext, String file_type, String json) throws Exception {
            checkObjectType(nodeContext, file_type);

            ObjectType objectType = ObjectType.get(nodeContext, file_type);
            ObjectData objectData = objectType.create(json);

            nodeContext.repo().submit(objectData);

            return objectData;
        }
    }
}
