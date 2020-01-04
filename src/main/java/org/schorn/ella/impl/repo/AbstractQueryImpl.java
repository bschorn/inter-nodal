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
package org.schorn.ella.impl.repo;

import java.util.List;
import java.util.stream.Collectors;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.repo.RepoActions;
import org.schorn.ella.repo.RepoData;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.QueryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Abstract Query Implementation
 *
 * Converts ActiveQuery -> QueryData
 *
 * There are two implementors that extend this abstract:
 * <ol>
 * <li>QueryExecutionState</li>
 * <li>QueryExecutionEvent</li>
 * </ol>
 *
 * These two are identical except for the underlying data. State is the latest
 * version. Event is all versions.
 *
 *
 * @author schorn
 *
 */
public abstract class AbstractQueryImpl extends AbstractContextual implements RepoActions.QueryExecution {

    private static final Logger LGR = LoggerFactory.getLogger(AbstractQueryImpl.class);

    private final RepoData.EventLogBroker.Agent dataAgent;

    public AbstractQueryImpl(AppContext context, RepoData.EventLogBroker.Agent dataAgent) {
        super(context);
        this.dataAgent = dataAgent;
    }

    /*
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * * *
	 * 
	 * Query Execution Code
	 * 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 * * * *
     */
    @Override
    public QueryData apply(ActiveQuery activeQuery) {
        LGR.info(String.format("%s.query() - %s", this.getClass(), activeQuery.toString()));

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		 * 
		 * ActiveQuery.from() - the target ObjectType to read through
		 *  
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        ObjectType fromType = activeQuery.from()[0];

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		 * 
		 * this.dataAgent is an EventLogBroker.Agent which is tapped into the EventLog
		 * The implementor of this class (AbstractQueryImpl) determines the Agent that will
		 * be used. 
		 * 
		 * EventLogBroker.Agent.read(ObjectType objectType, Predicate<ActiveData> filter)
		 *  
		 * FROM
		 * 		ObjectType
		 * WHERE
		 * 		Predicate<ActiveData>
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        List<ObjectData> rawData = this.dataAgent.read(fromType, activeQuery.where())
                .collect(Collectors.toList());

        /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
		 * 
		 * The creation of an empty ArrayData that has been created from an ArrayType
		 * that was created by ActiveQuery to hold the data from will be 
		 * 
		 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
        ArrayData results = null;
        try {
            switch (activeQuery.queryType()) {
                case LIST: {
                    ValueType valueType = activeQuery.select(0);
                    ArrayType resultType = valueType.arrayType();
                    ArrayData valueList = resultType.create();
                    rawData.stream().forEach(od -> valueList.add(od.get(valueType)));
                    return QueryData.create(valueList, activeQuery, activeQuery.name());
                }
                case OBJECT: {
                    results = activeQuery.resultData();
                    for (ObjectData matchData : rawData) {
                        results.add(matchData);
                    }
                    return QueryData.create(results, activeQuery, activeQuery.name());
                }
                case COPY_REDUCE:
                default: {
                    results = activeQuery.resultData();
                    if (rawData.isEmpty()) {
                        return QueryData.create(results, activeQuery, activeQuery.name());
                    }
                    CopyReduce copyReduce = new CopyReduce(activeQuery);
                    for (ObjectData objectData : rawData) {
                        results.add(copyReduce.apply(objectData));
                    }
                    return QueryData.create(results, activeQuery, activeQuery.name());
                }
            }
        } catch (Exception ex) {
            return QueryData.create(results, activeQuery, activeQuery.name(), ex);
        }
    }

}
