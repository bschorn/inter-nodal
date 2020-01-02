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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.Role;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.node.OpenNode.OpenArray;
import org.schorn.ella.node.OpenNode.OpenObject;
import org.schorn.ella.node.OpenNode.OpenValue;
import org.schorn.ella.repo.RepoSupport.ActiveCondition;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.ConditionStatementParser;
import org.schorn.ella.repo.RepoSupport.QueryNodeParser;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class QueryNodeParserImpl extends AbstractContextual implements QueryNodeParser {

    private static final Logger LGR = LoggerFactory.getLogger(QueryNodeParserImpl.class);
    private static final String ID_TAG = "Id"; // optional
    private static final String SELECT_TAG = "SELECT";
    private static final String FROM_TAG = "FROM";
    private static final String WHERE_TAG = "WHERE";

    /*
	 * 
     */
    protected QueryNodeParserImpl(AppContext context) {
        super(context);
    }


    /*
	{
  		"Request": {
			"Id": "0123-4567890123-45678",
			"SELECT": {
				"object_type": [
					"value_type",
					"value_type"
				],
				"object_type": [
					"value_type",
					"value_type"
				]
			},
			"FROM": [
				"object_type"
			],
			"WHERE": [
				"object_type.value_type IN ('X','Y')",
				"object_type.value_type > 0.4995"
			]		
		"Request": {
			"Id": "xyz",
			"SELECT": [
				"MLST_FACILITY": [ "*" ]
			],
			"FROM": [
				"MLST_FACILITY"
			],
			"WHERE": [
				"MLST_FACILITY.TXN_CURRENCY_CODE = JPY"
			]
		}
			
		"QUERY": {
			"ID": "1234",
			"SELECT": [
				"MLST_DAILY_TRANSACTION": [
					"TRANSACTION_ID",
					"TXN_EFFECTIVE_DATE",
					"TXN_AMT",
					"TXN_CURRENCY_CODE",
					"LINE_OF_BUSINESS_TYPE",
					"OUTSTANDING_ID",
					"TXN_TYPE"
				],
				"MLST_LENDER_SHARE": [
					"LENDER_SHARE_ID",
					"PORTFOLIO_CODE",
					"LENDER_ID",
					"EXPENSE_CODE",
					"PORTFOLIO_ACCT_TYPE",
					"COMMITMENT_PCT",
					"WAC",
					"TRADER_MARK"
				],
				"MLST_FACILITY": [
					"FCN",
					"FACILITY_ID",
					"DEAL_ID",
					"FACILITY_MATURITY_DATE"
				]
			],
			"FROM": [
				"MLST_FACILITY"
			],
			"WHERE": [
				"MLST_DAILY_TRANSACTION.TXN_CURRENCY_CODE = JPY",
				"MLST_DAILY_TRANSACTION.TXN_CURRENCY_CODE = USD",
				"MLST_LENDER_SHARE.COMMITMENT_PCT > 0.4995"
			]		
		}
	}
     */
    @Override
    public ActiveQuery apply(OpenNode queryNode) {
        try {
            String clientId = UUID.randomUUID().toString();
            OpenNode idNode = queryNode.findFirst(ID_TAG);
            if (idNode != null && idNode.role() == Role.Value) {
                clientId = ((OpenNode.OpenValue) idNode).value().toString();
            }
            ValueTypeMember[] selectTypes = getSelectTypes(queryNode.findFirst(SELECT_TAG));
            ObjectType[] objectTypes = getFromTypes(queryNode.findFirst(FROM_TAG));
            ActiveCondition[] conditions = getConditions(queryNode.findFirst(WHERE_TAG));
            ActiveQuery.Builder builder = ActiveQuery.builder(this.context(), clientId);
            builder.select(selectTypes);
            builder.from(objectTypes);
            builder.where(conditions);
            return builder.build();
        } catch (Exception ex) {
            this.setException(ex);
        }
        return null;
    }

    /**
     *
     * @param whereNode
     * @return
     */
    ActiveCondition[] getConditions(OpenNode whereNode) throws Exception {
        ConditionStatementParser conditionsParser = this.getConditionStatementParser(this.context());
        /*
		 * Declare the collection (List)
         */
        List<String> statements = new ArrayList<>();
        /*
		 * Declare the collector (Consumer)
         */
        Consumer<OpenNode> consumer = openNode -> {
            OpenValue ovalue = (OpenValue) openNode;
            String statement = ovalue.value().toString();
            statements.add(statement);
        };
        /*
		 * Call the recursive method with start-node and collector as parameters
         */
        dissect(whereNode, consumer);
        /*
		 * Iterate and Parse
         */
        List<ActiveCondition> conditions = new ArrayList<>();
        for (String statement : statements) {
            ActiveCondition condition = conditionsParser.apply(statement);
            if (condition != null) {
                conditions.add(condition);
            } else {
                conditionsParser.throwException();
            }
        }
        /*
		 * Results
         */
        return conditions.toArray(new ActiveCondition[0]);
    }

    /**
     *
     * @param selectNode
     * @return
     */
    ValueTypeMember[] getSelectTypes(OpenNode selectNode) {
        /*
		 * Declare the collection (List)
         */
        List<ValueTypeMember> valueTypeMembers = new ArrayList<>();
        /*
		 * Declare the collector (Consumer)
         */
        Consumer<OpenNode> consumer = openNode -> {
            OpenValue ovalue = (OpenValue) openNode;
            try {
                if (ovalue.value().toString().equals("*")) {
                    ObjectType objectType = ObjectType.get(this.context(), ovalue.name());
                    if (objectType != null) {
                        valueTypeMembers.addAll(objectType.valueTypeMembers());
                    }
                } else {
                    valueTypeMembers.add(ValueTypeMember.parse(this.context(), String.format("%s.%s", ovalue.name(), ovalue.value().toString())));
                }
            } catch (Exception ex) {
                LGR.error("{}.getSelectTypes() - ",
                        this.getClass().getSimpleName(), openNode.toString(),
                        Functions.getStackTraceAsString(ex));
            }
        };
        /*
		 * Call
         */
        dissect(selectNode, consumer);
        return valueTypeMembers.toArray(new ValueTypeMember[0]);
    }

    /**
     *
     * @param selectNode
     * @return
     */
    ObjectType[] getFromTypes(OpenNode fromNode) {
        /*
		 * Declare the collection (List)
         */
        List<ObjectType> objectTypes = new ArrayList<>();
        /*
		 * Declare the collector (Consumer)
         */
        Consumer<OpenNode> consumer = openNode -> {
            OpenValue ovalue = (OpenValue) openNode;
            try {
                objectTypes.add(ObjectType.get(this.context(), ovalue.value().toString()));
            } catch (Exception ex) {
                LGR.error("{}.getFromTypes() - ",
                        this.getClass().getSimpleName(), openNode.toString(),
                        Functions.getStackTraceAsString(ex));
            }
        };
        /*
		 * Call
         */
        dissect(fromNode, consumer);
        return objectTypes.toArray(new ObjectType[0]);
    }

    /**
     * Recursive method for OpenNode
     *
     * @param openNode
     * @throws Exception
     */
    void dissect(OpenNode openNode, Consumer<OpenNode> consumer) {
        switch (openNode.role()) {
            case Value:
                consumer.accept(openNode);
                break;
            case Object:
                OpenObject oobj = (OpenObject) openNode;
                for (OpenNode onode : oobj.nodes()) {
                    dissect(onode, consumer);
                }
                break;
            case Array:
                OpenArray oary = (OpenArray) openNode;
                for (OpenNode onode : oary.nodes()) {
                    dissect(onode, consumer);
                }
                break;
            default:
                break;
        }
    }

}
