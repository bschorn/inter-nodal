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
package org.schorn.ella.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.schorn.ella.Mingleton;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.server.AdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is hastily done Franken-design. Leftovers from a previous
 * implementation.
 *
 * Its the interface that the WebService was implemented against, so it serves
 * that purpose.
 *
 *
 *
 * @author schorn
 *
 */
public interface ActiveServices extends Mingleton {

    static final Logger LGR = LoggerFactory.getLogger(ActiveServices.class);

    /**
     * This one was added later and didn't fit in the functional categories.
     *
     * @return
     */
    static public List<String> contextNames() {
        return AppContext.values().stream().map(nc -> nc.name()).collect(Collectors.toList());
    }

    /**
     * There is one implementation that implements all the different interfaces,
     * but the WebService is still coded to a multi interface:
     * (Update,Query,Meta,...)
     *
     * @return
     */
    static ActiveServices provider() {
        return ServicesProvider.provider().getMingleton(ActiveServices.class, AppContext.Common);
    }

    /**
     * Backwards compatibility method
     *
     * @return
     */
    static public RepoWriter repoWriter() {
        return (RepoWriter) provider();
    }

    /**
     * Backwards compatibility method
     *
     * @return
     */
    static public RepoReader repoReader() {
        return (RepoReader) provider();
    }

    /**
     * Backwards compatibility method
     *
     * @return
     */
    static public RepoMetaData metaData() {
        return (RepoMetaData) provider();
    }

    /**
     * Backwards compatibility method
     *
     * @return
     */
    static public ContentTypeOutput contentTypeOutput() {
        return (ContentTypeOutput) provider();
    }

    /**
     * Hook into API for Custom Context Servers
     *
     * @param context_server
     * @return
     */
    static public ContentAPI contextAPI(String context_server) {
        return AdminServer.instance().getContentAPI(context_server);
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * 
	 * Interfaces are hold overs from a previous design but the WebService
	 * is dependent on them.
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    /**
     *
     */
    public interface RepoWriter {

        public enum UploadFileType {
            SINGLE_JSON, //single JSON encapsulates entire dataset
            MULTI_JSON, //multiple JSON delimited by line feed
            ;
        }

        Object setCurrentState(String context_str, String update_json, String target_type) throws Exception;

        String fileUpload(String context_str, String file_type, String entity_type, String file_content, Map<String, String> properties) throws Exception;
    }

    /**
     *
     */
    public interface RepoReader {

        Object getRepo(String context_str, String view);

        String getRepoAsString(String context_str, String view);

        Object getList(String context_str, String object_type, String value_type) throws Exception;

        Object getListAsString(String context_str, String object_type, String value_type) throws Exception;

        Object getQuery(String context_str, String object_type, String value_type, String value) throws Exception;

        String getQueryAsString(String context_str, String object_type, String value_type, String value) throws Exception;

        Object execSubscribeAndQuery(String context_str, String query_json, Boolean event_history) throws Exception;
    }

    /**
     *
     */
    public interface RepoMetaData {

        String get(String context_str);
        // What are the contexts?

        Object getContexts();
        // What are the object types for this context?

        Object getObjectTypes(String context_str);
        // What are the entity types for this context?

        Object getEntityTypes(String context_str);
        // What are the member types for this object type?

        Object getMemberTypes(String context_str, String object_type);
        // What are full details about each member type (name, role, bond, constraint)

        Object getMemberTypeDetails(String context_str, String object_type);

        Object getMemberTypeDetails(String context_str, String object_type, String member_type);
        // What are the container types of this member type?

        Object getContainerTypes(String context_str, String member_type);
        // What is the object type that has member_type as a key? 

        /**
         *
         * @param context_str
         * @param object_type
         * @param member_type
         * @return
         */
        String getKeyOwnerType(String context_str, String object_type, String member_type);
    }

    /**
     *
     */
    public interface ContentTypeOutput {

        String getHTMLPre(String content);

        String getHTMLForm(String context_str, String object_type);

        String getHTMLSelect(Object object, String composite_type, String value_type);

        String getHTMLTable(String context_str, Object object);

        String getHTMLTables(String context_str, Object object);

        String getHTMLFormInPage(String context_str, String object_type);


        /**
         *
         * @param object
         * @return
         */
        String getJSONString(Object object);

        String getHTMLInputDiv(String context_str, String composite_type, String value_type, Object value);
    }

}
