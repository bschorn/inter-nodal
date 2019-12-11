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
package org.schorn.ella.impl.services;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.html.ActiveHtml.HtmlElement;
import org.schorn.ella.html.HtmlProvider;
import org.schorn.ella.load.ActiveObjectLoad;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.Format;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.StructData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.BondType;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.repo.RepoSupport;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.ActiveUpdate;
import org.schorn.ella.repo.RepoSupport.QueryNodeParser;
import org.schorn.ella.services.ActiveServices;
import org.schorn.ella.transform.ActiveTransform.Transform;
import org.schorn.ella.transform.TransformProvider;
import org.schorn.ella.util.Functions;

/**
 *
 *
 * @author schorn
 *
 */
class ServiceProviderImpl extends AbstractContextual implements ActiveServices, ActiveServices.RepoWriter, ActiveServices.RepoReader, ActiveServices.RepoMetaData, ActiveServices.ContentTypeOutput {

    protected ServiceProviderImpl(AppContext context) {
        super(context);
    }

    private static final Logger LGR = LoggerFactory.getLogger(ActiveServices.class);

    /**
     * The entity_type parameter is to communicate what the 'root' type of the
     * JSON which is often not part of the communicated JSON string.
     *
     * {
     * "name": "Abigale Von Struck", "age": 3, "dob": "1924-04-15" }
     *
     * What is this? entity_type = "Person"
     *
     */
    @SuppressWarnings("rawtypes")
    @Override
    public String fileUpload(String context_str, String file_type, String entity_type, String file_content, Map<String, String> properties) throws Exception {
        Optional<AppContext> optContext = AppContext.valueOf(context_str);
        if (optContext.isPresent()) {
            AppContext context = optContext.get();
            ActiveServices.RepoWriter.UploadFileType fileType = ActiveServices.RepoWriter.UploadFileType.valueOf(file_type);
            switch (fileType) {
                case MULTI_JSON: {
                    for (String json : file_content.split(System.lineSeparator())) {
                        StructData structData = context.transformJSONtoActiveNode(json);
                        if (structData.role() == Role.Object) {
                            ObjectData objectData = (ObjectData) structData;
                            context.repo().submit(objectData);
                        }
                    }
                }
                break;
                case SINGLE_JSON: {
                    try {
                        Reader reader = ActiveObjectLoad.createBulkReader(context, file_content);
                        Predicate<OpenNode> repoFilter = context.getRepoFilter(OpenNode.class);
                        ActiveObjectLoad.LoadManager<OpenNode> loadManager = ActiveObjectLoad.getLoadManager(context);
                        ActiveObjectLoad.BulkProcessor bulkProcessor = ActiveObjectLoad.JsonBulkProcessor.create(context, reader, repoFilter, loadManager);
                        //bulkProcessor.setLimit(2500);
                        loadManager.setThreadCount(1);
                        loadManager.start();
                        new Thread(bulkProcessor).start();
                        return "loading...";
                    } catch (Exception ex) {
                        throw new Exception(String.format("%s.fileUpload() - [exception] %s [context] %s [file_type] %s [entity_type] %s [content_size] %d bytes",
                                this.getClass().getSimpleName(),
                                ex.getMessage(), context_str,
                                file_type, entity_type, file_content.length() * 2));
                    }
                }
            }
        }
        throw new Exception(String.format("%s.fileUpload() - Context: %s is not valid",
                this.getClass().getSimpleName(),
                context_str));
    }

    /**
     *
     */
    @Override
    public ArrayData getRepo(String context_str, String view) {
        Optional<AppContext> optActiveContext = AppContext.valueOf(context_str);
        if (optActiveContext.isPresent()) {
            AppContext context = optActiveContext.get();
            return context.repo().summary();
        }
        return null;
    }

    /**
     * Query for Table
     */
    @Override
    public ArrayData getQuery(String context_str, String object_type, String value_type, String value) throws Exception {
        Optional<AppContext> optNodeContext = AppContext.valueOf(context_str);
        if (optNodeContext.isPresent()) {
            AppContext context = optNodeContext.get();
            return context.runQuery(object_type, value_type, value);
        }
        return null;
    }

    /**
     *
     */
    @Override
    public ArrayData getList(String context_str, String object_type, String value_type) throws Exception {
        Optional<AppContext> optContext = AppContext.valueOf(context_str);
        if (optContext.isPresent()) {
            AppContext context = optContext.get();
            return context.runList(object_type, value_type);
        }
        throw new Exception(String.format("%s.getList() - Context: %s is not valid",
                this.getClass().getSimpleName(),
                context_str));
    }

    /**
     *
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public ArrayData execSubscribeAndQuery(String context_str, String query_json, Boolean eventHistory) throws Exception {
        Optional<AppContext> optContext = AppContext.valueOf(context_str);
        if (optContext.isPresent()) {
            AppContext context = optContext.get();
            Transform transform = TransformProvider.provider().getTransform(context, Format.JSON, Format.OpenNode);
            OpenNode openNode = (OpenNode) transform.apply(query_json);
            OpenNode.OpenObject queryNode = (OpenNode.OpenObject) openNode;
            ActiveQuery activeQuery = QueryNodeParser.get(context).apply(queryNode);
            /*
			 * If we supported pub/sub (bi-directional WebSockets)
             */
            //ObjectType aggregateType = ObjectType.get(context, requested_type);
            //RepoSupport.ActiveSubscription subscription = RepoSupport.ActiveSubscription.create(context, aggregateType, activeQuery);
            if (eventHistory) {
                return context.repo().queryEvents(activeQuery);
            } else {
                return context.repo().query(activeQuery);
            }
        } else {
            throw new Exception(String.format("%s.getList() - Context: %s is not valid",
                    this.getClass().getSimpleName(),
                    context_str));
        }
    }

    /**
     *
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Object setCurrentState(String context_str, String update_json, String target_type) throws Exception {
        Optional<AppContext> optContext = AppContext.valueOf(context_str);
        if (optContext.isPresent()) {
            AppContext context = optContext.get();
            Transform jsonToOpenNode = TransformProvider.provider().getTransform(context, Format.JSON, Format.OpenNode);
            OpenNode openNode = (OpenNode) jsonToOpenNode.apply(update_json);
            if (openNode != null) {
                OpenNode updateNode = openNode.findFirst(target_type);
                if (updateNode != null) {
                    ObjectType targetType = ObjectType.get(context, target_type);
                    ActiveUpdate activeUpdate = ActiveUpdate.create(context, targetType, updateNode);
                    if (!activeUpdate.unbundle()) {
                        activeUpdate.throwException();
                    } else {
                        activeUpdate.run();
                        activeUpdate.throwException();
                    }
                    /*
					ObjectData objectData = null;
					context.repo().submit(objectData);
					return objectData.asJsonString();
                     */
                } else {
                    throw new Exception(String.format("%s.save() - target update type '%s' was not found in update msg:\n%s\n",
                            this.getClass().getSimpleName(),
                            target_type, update_json));
                }
            } else {
                jsonToOpenNode.throwException();
            }
        }
        return null;
    }

    @Override
    public String getRepoAsString(String context, String view) {
        return getRepo(context, view).asJsonString();
    }

    @Override
    public String getQueryAsString(String context, String object_type, String value_type, String value) throws Exception {
        return getQuery(context, object_type, value_type, value).asJsonString();
    }

    @Override
    public String getListAsString(String context, String object_type, String value_type) throws Exception {
        return getList(context, object_type, value_type).asJsonString();
    }

    /**
     *
     */
    @Override
    public String get(String context) {
        Optional<AppContext> optNodeContext = AppContext.valueOf(context);
        if (optNodeContext.isPresent()) {
            AppContext nodeContext = optNodeContext.get();
            List<ObjectType> objectTypes = nodeContext.objectTypes();
            StringJoiner joiner = new StringJoiner(",\n", "", "");
            for (ObjectType compositeType : objectTypes) {
                joiner.add(compositeType.toString());
            }
            return joiner.toString();
        }
        return "";
    }

    /**
     *
     */
    @Override
    public ArrayData getContexts() {
        try {
            ValueType valueType = ValueType.dynamic(AppContext.Common, "Contexts", AppContext.Common.name());
            ArrayData contexts = ArrayType.create(AppContext.Internal, "Contexts", valueType, BondType.OPTIONAL).create();
            for (AppContext context : AppContext.values()) {
                contexts.add(valueType.create(context.name()));
            }
            return contexts;
        } catch (Exception ex) {
            LGR.error(Functions.getStackTraceAsString(ex));
        }
        return AppContext.Common.createErrorArray(new String[]{"Error: ServiceProvider.getContexts()"});
    }

    /**
     *
     */
    @Override
    public ArrayData getObjectTypes(String context_str) {
        Optional<AppContext> optContext = AppContext.valueOf(context_str);
        if (optContext.isPresent()) {
            AppContext context = optContext.get();
            List<ObjectType> objectTypes = context.objectTypes();
            List<Object> names = objectTypes.stream()
                    .map(ot -> ot.name())
                    .collect(Collectors.toList());
            return context.createArray("ObjectType", names);
        }
        return null;
    }

    /**
     *
     */
    @Override
    public ArrayData getEntityTypes(String context_str) {
        Optional<AppContext> optContext = AppContext.valueOf(context_str);
        if (optContext.isPresent()) {
            AppContext context = optContext.get();
            List<ObjectType> objectTypes = context.entityTypes();
            List<Object> names = objectTypes.stream()
                    .map(ot -> ot.name())
                    .collect(Collectors.toList());
            return context.createArray("EntityType", names);
        }
        return null;
    }

    /**
     *
     */
    @Override
    public ArrayData getMemberTypes(String context_str, String object_type) {
        Optional<AppContext> optNodeContext = AppContext.valueOf(context_str);
        if (optNodeContext.isPresent()) {
            AppContext context = optNodeContext.get();
            ObjectType objectType = ObjectType.get(context, object_type);
            List<Object> items = objectType.schema().memberDefs().stream()
                    .map(mt -> Object.class.cast(mt.activeType().name()))
                    .collect(Collectors.toList());
            return context.createArray("Members", items);
        }
        return null;
    }

    /**
     *
     */
    @Override
    public ObjectData getContainerTypes(String context_str, String active_type) {
        Optional<AppContext> optNodeContext = AppContext.valueOf(context_str);
        if (optNodeContext.isPresent()) {
            AppContext context = optNodeContext.get();
            ActiveType activeType = ActiveType.get(context, active_type);
            if (activeType != null) {
                return activeType.getContainerTypes();
            }
        }
        return null;
    }

    /**
     *
     */
    @Override
    public String getKeyOwnerType(String context_str, String object_type, String member_type) {
        Optional<AppContext> optNodeContext = AppContext.valueOf(context_str);
        if (optNodeContext.isPresent()) {
            AppContext context = optNodeContext.get();
            ObjectType objectType = ObjectType.get(context, object_type);
            ValueType valueType = ValueType.get(context, member_type);
            if (objectType != null && !objectType.isForeignKey(valueType)) {
                return null;
            }
            if (valueType != null) {
                List<ObjectType> objectTypes = context.objectTypes();
                List<ObjectType> potentialOwners = objectTypes.stream()
                        .filter(ot -> ot.isMember(valueType) && ot.isUniqueKey(valueType))
                        .collect(Collectors.toList());
                if (potentialOwners.size() == 1) {
                    return potentialOwners.get(0).name();
                }
            }
        }
        return null;
    }

    /**
     *
     */
    @Override
    public Object getMemberTypeDetails(String context_str, String object_type) {
        Optional<AppContext> optNodeContext = AppContext.valueOf(context_str);
        if (optNodeContext.isPresent()) {
            AppContext context = optNodeContext.get();
            ObjectType compositeType = ObjectType.get(context, object_type);
            return compositeType.getMetaObject();
        }
        return null;
    }

    /**
     *
     */
    @Override
    public Object getMemberTypeDetails(String context_str, String object_type, String member_type) {
        Optional<AppContext> optNodeContext = AppContext.valueOf(context_str);
        if (optNodeContext.isPresent()) {
            AppContext context = optNodeContext.get();
            ObjectType objectType = ObjectType.get(context, object_type);
            ObjectData metaObj = objectType.getMetaObject();
            Optional<ActiveData> optActiveData = metaObj.nodes().stream()
                    .filter(ad -> ad.name().equals(member_type))
                    .findFirst();
            if (optActiveData.isPresent()) {
                return optActiveData.get();
            }
        }
        return null;
    }

    final String HTML_FORMAT = "<html><body><pre id='lds-data'>%s</pre></body></html>";

    /**
     *
     */
    @Override
    public String getHTMLPre(String content) {
        return String.format(HTML_FORMAT, content);
    }

    /**
     *
     */
    @Override
    public String getHTMLForm(String context_str, String object_type) {
        //App app = App.valueOf(context);
        Optional<AppContext> optNodeContext = AppContext.valueOf(context_str);
        if (optNodeContext.isPresent()) {
            AppContext context = optNodeContext.get();
            ObjectType objectType = ObjectType.get(context, object_type);
            HtmlElement htmlElement = objectType.htmlForm();
            try {
                return htmlElement.render();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    public String getHTMLSelect(Object object, String object_type, String value_type) {
        ArrayData arrayData = (ArrayData) object;
        HtmlElement htmlElement = arrayData.htmlSelect(value_type, value_type);
        try {
            return htmlElement.render();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getHTMLInputDiv(String context_str, String composite_type, String value_type, Object value) {
        Optional<AppContext> optNodeContext = AppContext.valueOf(context_str);
        if (optNodeContext.isPresent()) {
            AppContext context = optNodeContext.get();
            ObjectType objectType = ObjectType.get(context, composite_type);
            if (objectType != null) {
                ValueType valueType = ValueType.get(context, value_type);
                if (valueType != null) {
                    HtmlElement htmlElement;
                    try {
                        if (valueType.fieldType().dataType().primitiveType().dataGroup().isEnumerable()) {
                            htmlElement = HtmlProvider.provider().html_list(objectType, valueType, value);
                        } else {
                            htmlElement = HtmlProvider.provider().html_input(objectType, valueType, value);
                        }
                        return htmlElement.render();
                    } catch (Exception ex) {
                        LGR.error(Functions.getStackTraceAsString(ex));
                    }
                }
            }
        }
        return "";
    }

    @Override
    public String getHTMLTable(String context_str, Object object) {
        StructData structData = (StructData) object;
        HtmlElement htmlElement = structData.htmlTable();
        try {
            return htmlElement.render();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getHTMLTables(String context_str, Object object) {
        ArrayData arrayData = (ArrayData) object;
        HtmlElement htmlElement = arrayData.htmlTable();
        try {
            return htmlElement.render();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getJSONString(Object object) {
        if (object instanceof RepoSupport.QueryData) {
            RepoSupport.QueryData queryData = (RepoSupport.QueryData) object;
            ArrayData arrayData = queryData.arrayData();
            return arrayData.asJsonString();
        }
        if (object instanceof StructData) {
            StructData structData = (StructData) object;
            return structData.asJsonString();
        }
        return "{}";
    }

}
