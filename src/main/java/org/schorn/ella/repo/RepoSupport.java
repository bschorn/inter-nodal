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
package org.schorn.ella.repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.schorn.ella.FunctionalException;
import org.schorn.ella.Mingleton;
import org.schorn.ella.Renewable;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.context.ActiveContext.Contextual;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ActiveOperator;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueTypeMember;
import org.schorn.ella.sql.ActiveSQL;
import org.schorn.ella.node.OpenNode;
import org.schorn.ella.node.OpenNode.OpenObject;

/**
 *
 * @author schorn
 *
 */
public interface RepoSupport {

    interface QueryNodeParser extends Function<OpenNode, ActiveQuery>, Mingleton {

        default ConditionStatementParser getConditionStatementParser(AppContext context) throws Exception {
            return RepoSupport.ConditionStatementParser.create(context, ActiveSQL.SQLOperators.values());
        }

        static public QueryNodeParser get(AppContext context) {
            return RepoProvider.provider().getMingleton(QueryNodeParser.class, context);
        }
    }

    /**
     * Query/Subscription
     *
     * ActiveSubscription subscription = ActiveSubscription.create(context,
     * aggregateType, requestNode); ActiveQuery activeQuery =
     * subscription.getQuery()
     *
     */
    interface ActiveSubscription extends Renewable<ActiveSubscription>, Consumer<ObjectData>, Predicate<ObjectData>, Supplier<ObjectData> {

        ActiveQuery getQuery();

        void unsubscribe();

        /**
         * Creates Query/Subscription
         *
         * @param context
         * @param objectType
         * @param criteria
         * @return
         */
        static public ActiveSubscription create(AppContext context, ObjectType objectType, ActiveQuery activeQuery) {
            ActiveSubscription template = RepoProvider.provider().getRenewable(ActiveSubscription.class, context);
            return template.renew(objectType, activeQuery);
        }
    }

    /**
     * Query Interface for querying the Repo
     *
     *
     */
    interface ActiveQuery {

        /**
         * The implementation will determine the QueryType
         */
        public enum QueryType {
            /**
             * Querying Object(s) directly from Repo (as is). This is the
             * simplest method for reading. This is the most reliable way to
             * update:
             * <ol>
             * <li>read object</li>
             * <li>update object</li>
             * <li>submit object</li>
             * </ol>
             */
            OBJECT,
            /**
             * Querying for a List (most often for a UI menu dropdown list). {
             * "region": [ "East", "West", "North", "South" ] }
             */
            LIST,
            /**
             * Querying an Aggregate (as in domain type) with specific fields
             * selected by qualified ValueType (i.e. ValueTypeMember) in the
             * select(). This will make a copy of the Aggregate instance but
             * only copy over those fields that answer true to
             * isSelected(ActiveType memberOfType, ActiveType memberType) which
             * is checking the select() statement for ValueTypeMember(s) that
             * match.
             *
             */
            COPY_REDUCE,
            OTHER;
        }

        AppContext context();

        /**
         *
         * @return
         */
        QueryType queryType();

        /**
         * Client discretion or generated to be informative
         *
         * @return
         */
        String name();

        /**
         * Array of ValueTypes to be returned
         *
         * @return
         */
        ValueType[] select();

        /**
         * The ValueType at position X (index)
         *
         * @param index
         * @return
         */
        ValueType select(int index);

        /**
         * Array of ValueTypes to be returned that are members of the
         * memberOfType.
         *
         * @param memberOfType
         * @return
         */
        ValueType[] select(ObjectType memberOfType);

        boolean hasFlag(QueryFlag queryFlag);

        ObjectType[] from();

        Predicate<ActiveData> where();

        ValueType[] orderBy();

        boolean isSelected(ActiveType memberOfType, ActiveType memberType);

        /**
         * The *implementor* will return the same ObjectType as from() unless
         * there are ValueTypes in the select(). This method will create a
         * dynamic ObjectType to reflect the ValueTypes returned by the select()
         * method. The name of the ObjectType will be that returned by name().
         *
         * The *client* of the ActiveQuery interface will use the ObjectType
         * returned by to() to ObjectType.create() each ObjectData (row).
         *
         * @return
         * @throws Exception
         */
        ObjectType[] to() throws Exception;

        /**
         * This should return an empty ArrayData to accept ObjectData objects
         * created by the ObjectType returned by to().
         *
         * ActiveQuery activeQuery = getActiveyQuery();
         * activeQuery.result().arrayType().memberType() == activeQuery.to()
         *
         * ** NOTE: This will create a NEW instance each time its called **
         *
         * @return
         * @throws Exception
         */
        ArrayData resultData() throws Exception;

        public interface QueryFlag {

            int flagId();
        }

        public enum QueryFlags implements QueryFlag {

            /**
             *
             */
            DISTINCT,
            INCLUDE_NULLS,
            SORT,

            /**
             *
             */
            UNKNOWN,;

            @Override
            public int flagId() {
                return this.ordinal();
            }

            static public QueryFlags fromFlagId(int flagId) {
                for (QueryFlags queryFlag : QueryFlags.values()) {
                    if (queryFlag.ordinal() == flagId) {
                        return queryFlag;
                    }
                }
                return UNKNOWN;
            }
        }

        /**
         * ObjectQuery.Builder
         * <p>
         *
         *
         *
         */
        interface Builder {

            Builder from(String from);

            Builder from(ObjectType[] objectType);

            Builder to(ObjectType[] objectType);

            Builder select(List<String> select);

            /**
             *
             * @param valueTypes
             * @return
             */
            Builder select(ValueType... valueTypes);

            Builder select(ValueTypeMember[] valueTypeMember);

            Builder distinct();

            /**
             * This will apply conditions so that contradictions are applied as
             * OR (the remaining will be AND)
             *
             * A.a=1 AND A.a=2 <- contradiction A.a=1 OR A.a=2 <- ok
             *
             *
             * @param conditions
             * @return
             */
            default Builder where(ActiveCondition[] conditions) {
                // will create a new array instead of reordering the caller's array
                List<ActiveCondition> list = new ArrayList<>(conditions.length);
                for (ActiveCondition condition : conditions) {
                    list.add(condition);
                }
                list.sort(ActiveCondition::sort);
                for (int i = 0; i < list.size(); i += 1) {
                    if (i == 0) {
                        this.where(conditions[i]);
                    } else if (conditions[i - 1].objectType().equals(conditions[i].objectType())
                            && conditions[i - 1].valueType().equals(conditions[i].valueType())) {
                        this.or(conditions[i]);
                    } else {
                        this.and(conditions[i]);
                    }
                }
                return this;
            }

            Builder where(Predicate<ActiveData> where);

            Builder and(Predicate<ActiveData> and);

            Builder or(Predicate<ActiveData> or);

            Builder order_by(String... fieldName);

            Builder order_by(ValueType... valueTypes);

            Builder order_by(ValueTypeMember... valueTypeMembers);

            Builder addFlags(QueryFlag... queryFlags);

            /**
             * Warning: ditto() resets the where() so whatever predicate had
             * been put into place prior to the ditto will need to be manually
             * copied over. Was unable to find an ideal way to correctly clone a
             * Predicate in all scenarios that a predicate may have been created
             * (and all of the combinations).
             *
             * <code>
             * Builder builder1 = getBuilder1();
             * builder1.where(filter);
             * Builder builder2 = builder1.ditto();
             * builder2.where(filter);
             * </code>
             *
             * @return Builder
             */
            Builder ditto();

            ActiveQuery build() throws Exception;

            boolean hasFlag(QueryFlag queryFlag);
        }

        static public Builder builder(AppContext context, String name) throws Exception {
            return RepoProvider.provider().createInstance(ActiveQuery.Builder.class, context, name);
        }
    }

    /**
     *
     * Query Result Container (extension of ArrayData)
     *
     */
    interface QueryData extends ArrayData, Renewable<QueryData>, FunctionalException {

        static public QueryData create(ArrayData arrayData, ActiveQuery activeQuery, String title, Object... params) {
            QueryData queryData = RepoProvider.provider().getRenewable(QueryData.class, arrayData.context());
            return queryData.renew(arrayData, activeQuery, title, params);
        }

        ActiveQuery activeQuery();

        String title();

        ArrayData arrayData();

        void setClientId(String clientId);

        String getClientId();
    }

    /**
     *
     *
     *
     */
    interface ActiveUpdate extends Renewable<ActiveUpdate>, Contextual, FunctionalException {

        static public ActiveUpdate create(AppContext context, ObjectType targetType, OpenNode updateNode) {
            ActiveUpdate template = RepoProvider.provider().getRenewable(ActiveUpdate.class, context);
            return template.renew(targetType, updateNode);
        }

        /**
         * Separate each instance into separate transaction units (UpdateData).
         * If returns false, then call ActiveUpdate.throwException() to get the
         * exception.
         *
         * @return
         */
        boolean unbundle();

        /**
         * Execute the update for all.
         *
         */
        void run();

        /**
         * Update Targeted Type
         *
         * @return
         */
        ObjectType targetType();

        /**
         * Target
         *
         * @return
         */
        UpdateData[] updateData();
    }

    public enum BuildScenario {
        UpdateObjectOnly,
        UpdateAggregate,
        Insert,;
    }

    /**
     *
     * Update Result Container
     *
     */
    interface UpdateData extends Renewable<UpdateData>, FunctionalException, Callable<UpdateData> {

        public enum Status {
            Inprocess,
            Updated,
            Rejected;
        }

        /**
         *
         */
        public enum Scenario {
            Insert,
            Update,
            Reject,
            Error,
            None;
        }

        /**
         *
         * @param context
         * @param targetType
         * @param openNode
         * @return
         */
        static public UpdateData create(AppContext context, ObjectType targetType, OpenNode openNode) {
            UpdateData updateData = RepoProvider.provider().getRenewable(UpdateData.class, context);
            return updateData.renew(targetType, openNode);
        }

        /**
         *
         * @return
         */
        ObjectType targetType();

        /**
         * Do you want the update to be atomic for the entire aggregate?
         *
         * @return
         */
        boolean isAggregateLevel();

        /**
         * The update information as it came from the source. This could be just
         * one of many instances if there were multiples submitted in a single
         * message.
         *
         * @return
         */
        OpenObject updateObj();

        Status status();

        String statusDescription();

        String keyStr();

        Integer keyInt();

        Integer version();

        ObjectData updateData();

        /**
         *
         * @param currentObjectData
         * @param currentValueData
         * @return
         */
        ValueData getValueData(ObjectData currentObjectData, ValueData currentValueData);

        ArrayData getArrayData(ObjectData currentObjectData, ArrayData currentArrayData);

        /**
         * This is the last known state in the repo for this instance's key.
         *
         * (this is live data, so it may not be the same between subsequent
         * calls)
         *
         * @return
         */
        ObjectData currentData();

        /**
         * This is the version of the instance return by currentData()
         *
         * (this is live data, so it may not be the same between subsequent
         * calls)
         *
         * @return
         */
        Integer currentVersion();

        /**
         * Scenario Logic
         *
         * @return
         */
        default Scenario scenario() {
            if (this.status() == Status.Inprocess) {
                if (this.updateObj() != null) {
                    if (this.keyStr() == null) {
                        return Scenario.Insert;
                    }
                    if (this.currentVersion() != null && this.version() != null) {
                        if (this.currentVersion().intValue() == this.version().intValue()) {
                            return Scenario.Update;
                        } else {
                            return Scenario.Reject;
                        }
                    }
                }
                return Scenario.Error;
            }
            return Scenario.None;
        }

    }

    /**
     * Filter Interface (used with FilterableStructData)
     */
    interface ActiveFilter extends Predicate<ActiveData>, Renewable<ActiveFilter> {

        static public ActiveFilter create(AppContext context, Predicate<ActiveData> predicate) {
            ActiveFilter activeFilter = RepoProvider.provider().getRenewable(ActiveFilter.class, context);
            return activeFilter.renew(predicate);
        }

        @Override
        boolean test(ActiveData activeData);
    }

    /**
     * FilterableObjectData
     *
     * ObjectData + ActiveFilter
     */
    interface FilteredObjectData extends ObjectData, Renewable<FilteredObjectData> {

        static public FilteredObjectData create(AppContext context, ObjectData objectData, ActiveFilter activeFilter) {
            FilteredObjectData filtered = RepoProvider.provider().getRenewable(FilteredObjectData.class, context);
            return filtered.renew(objectData, activeFilter);
        }
    }

    /**
     * Condition implemented through Predicate
     *
     */
    interface ActiveCondition extends Predicate<ActiveData> {

        String descriptive();

        ObjectType objectType();

        ValueType valueType();

        ActiveOperator operator();

        void setCondition(ActiveOperator operator, Object[] values);

        /**
         * This will sort an array of conditions by: 1. Table 2. Field
         *
         * Given two conditions, if Table + Field are the same then we -OR-
         * these conditions together otherwise use -AND-
         *
         *
         * @param lhs
         * @param rhs
         * @return
         */
        static public int sort(ActiveCondition lhs, ActiveCondition rhs) {
            if (lhs.objectType().equals(rhs.objectType())) {
                return lhs.valueType().compareTo(rhs.valueType());
            } else {
                return lhs.objectType().compareTo(rhs.objectType());
            }
        }

        static public ActiveCondition create(ActiveOperator operator, ValueTypeMember valueTypeMember, List<Object> values) throws Exception {
            ActiveCondition condition = RepoProvider.provider().createInstance(ActiveCondition.class, valueTypeMember.memberOfType(), valueTypeMember.memberType());
            condition.setCondition(operator, values.toArray(new Object[0]));
            return condition;
        }

        static public ActiveCondition create(ActiveOperator operator, ObjectType objectType, ValueType valueType, Object... values) throws Exception {
            ActiveCondition condition = RepoProvider.provider().createInstance(ActiveCondition.class, objectType, valueType);
            condition.setCondition(operator, values);
            return condition;
        }

        static public ActiveCondition create(ActiveOperator operator, String context_str, String valueTypeMemberStr, Object[] values) throws Exception {
            ValueTypeMember valueTypeMember = ValueTypeMember.parse(context_str, valueTypeMemberStr);
            return create(operator, valueTypeMember, Arrays.asList(values));
        }
    }

    /**
     *
     *
     */
    interface ConditionStatementParser extends Function<String, ActiveCondition>, FunctionalException, Mingleton {

        public ActiveCondition apply(String statement);

        /**
         *
         * @param context
         * @param classFor
         * @return
         * @throws Exception
         */
        static public ConditionStatementParser create(AppContext context, ActiveOperator[] activeOperators) throws Exception {
            return RepoProvider.provider().createConditionStatementParser(context, activeOperators);
        }
    }
}
