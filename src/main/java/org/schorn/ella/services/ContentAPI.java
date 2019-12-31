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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.repo.RepoSupport;
import org.schorn.ella.repo.RepoSupport.ActiveCondition;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.ConditionStatementParser;
import org.schorn.ella.repo.RepoSupport.QueryData;
import org.schorn.ella.server.ActiveServer.AdminServer;
import org.schorn.ella.server.ActiveServer.ContextServer;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
public interface ContentAPI {

    /**
     * Get ContentAPI implementation for the given context_server name.
     *
     * @param context_server
     * @return
     */
    static public ContentAPI get(String context_server) {
        return AdminServer.instance().getContentAPI(context_server);
    }

    /**
     * The Context Server that this API represents.
     *
     * @return
     */
    public ContextServer contextServer();

    /**
     * Translate name into object
     *
     * @param queryName
     * @return
     */
    public NamedQuery getNamedQuery(String queryName);

    /**
     * Default Parser
     *
     * @return
     */
    default ConditionStatementParser getParser(AppContext context) throws Exception {
        return RepoSupport.ConditionStatementParser.create(context, QueryString.QueryStringOperators.values());
    }

    /**
     * Default Implementation
     *
     * Runs the query that the queryName represents. Much like a stored
     * procedure.
     *
     * @param queryName
     * @return
     */
    @SuppressWarnings("unchecked")
    default Object runNamedQuery(String queryName, Map<String, Object> optParams) {
        try {
            NamedQuery namedQuery = getNamedQuery(queryName);
            ActiveQuery.Builder queryBuilder = namedQuery.builder();

            List<String> whereStrings = new ArrayList<>();
            whereStrings.addAll(namedQuery.queryFilters());

            /*
             * where clause conditions can be added to queryBuilder at this point before
             * calling build()
             */
            if (optParams != null) {
                Object objParam = optParams.get("where");
                if (objParam instanceof List) {
                    List<String> whereParams = (List<String>) objParam;
                    if (!whereParams.isEmpty()) {
                        for (String whereParamStr : whereParams) {
                            if (!whereParamStr.isEmpty()) {
                                whereStrings.add(whereParamStr);
                            }
                        }
                    }
                }
                Object order_by = optParams.get("order_by");
                if (order_by instanceof List) {
                    List<String> orderBy = (List<String>) order_by;
                    if (orderBy != null && !orderBy.isEmpty()) {
                        queryBuilder.order_by(orderBy.stream().filter(str -> !str.isEmpty()).collect(Collectors.toList()).toArray(new String[0]));
                    }
                }
            }

            if (whereStrings.size() > 0) {
                List<ActiveCondition> conditions = new ArrayList<>();
                ConditionStatementParser parser = this.getParser(this.contextServer().context());
                for (String whereString : whereStrings) {
                    ActiveCondition condition = parser.apply(whereString);
                    if (condition != null) {
                        conditions.add(condition);
                    } else {
                        parser.throwException();
                    }
                }
                queryBuilder.where(conditions.toArray(new ActiveCondition[0]));
            }

            ActiveQuery activeQuery = queryBuilder.build();
            QueryData queryData = activeQuery.context().repo().query(activeQuery);
            queryData.throwException();
            return queryData;
        } catch (Exception ex) {
            String errMsg = String.format("%s.runNamedQuery() - failed running active_query: %s\nException: %s",
                    this.getClass().getSimpleName(),
                    queryName,
                    Functions.getStackTraceAsString(ex)
            );
            return this.contextServer().context().createErrorArray(new String[]{errMsg});
        }
    }

}
