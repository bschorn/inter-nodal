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
package org.schorn.ella.impl.load;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.io.EndPoint;
import org.schorn.ella.load.ActiveTabularLoad.ActiveObjectAssembler;
import org.schorn.ella.load.ActiveTabularLoad.ActiveTypeValue;
import org.schorn.ella.load.ActiveTabularLoad.DbRowProcessor;
import org.schorn.ella.load.ActiveTabularLoad.LoadFromQuery;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ActiveType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueData;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.ActiveNode.Identity;
import org.schorn.ella.sql.DBConnect;
import org.schorn.ella.util.Functions;

/**
 *
 * @author schorn
 *
 */
class LoadFromQueryImpl extends AbstractContextual implements LoadFromQuery {

    static private final Logger LGR = LoggerFactory.getLogger(LoadFromQueryImpl.class);

    private AppContext context;
    private Identity identity;
    private ObjectType objectType;
    private String sql;
    private List<ActiveType> types;
    private Function<ActiveTypeValue, ActiveData> fieldCreator;
    private Predicate<ActiveData> dataChecker;
    private Function<List<ActiveData>, ObjectData> objectCreator;
    private Predicate<ObjectData> objectChecker;
    private Consumer<ObjectData> submitter;
    private Map<String, Exception> badFields;
    private ActiveObjectAssembler assembler;
    private Function<ResultSet, List<ActiveTypeValue>> rowsToFields;
    private Predicate<ActiveTypeValue> validateField;
    private DbRowProcessor rowProcessor;
    private Exception exception;
    private EndPoint.DatabasePoint dbEndPoint;
    private boolean killSwitch = false;

    protected LoadFromQueryImpl(AppContext context) {
        super(context);
    }

    @Override
    public LoadFromQuery renew(Object... params) {
        AppContext context = this.context;
        for (Object param : params) {
            if (param instanceof AppContext) {
                context = (AppContext) param;
            }
        }
        return new LoadFromQueryImpl(context);
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", this.objectType.name(), this.sql);
    }

    @Override
    public void stop() {
        this.killSwitch = true;
    }

    @Override
    public void use(EndPoint.DatabasePoint dbEndPoint, String sql, Identity identity) {
        this.dbEndPoint = dbEndPoint;
        this.sql = sql;
        this.identity = identity;
    }

    /*
	 * 
     */
    @Override
    public void to(ObjectType objectType) throws Exception {

        this.badFields = new HashMap<>();
        this.objectType = objectType;
        this.context = objectType.context();
        this.types = objectType.valueTypes().stream().map(vt -> ActiveType.class.cast(vt)).collect(Collectors.toList());

        /*
		 * 
		 * Raw Data + ValueType -> ValueData
         */
        this.fieldCreator = (atv) -> {
            switch (atv.type().role()) {
                case Value:
                    ValueType valueType = (ValueType) atv.type();
                    ValueData valueData = valueType.create(atv.value());
                    return valueData;
                default:
                    break;
            }
            return null;
        };

        /*
		 * 
         */
        this.dataChecker = (ad) -> {
            return true;
        };

        /*
		 * Create
         */
        this.objectCreator = (members) -> {
            ObjectData objectData = null;
            try {
                objectData = objectType.create(members.toArray(new ActiveData[0]));
            } catch (Exception e) {
                LGR.error(Functions.getStackTraceAsString(e));
            }

            return objectData;
        };

        /*
		 * Check the integrity of this object
         */
        this.objectChecker = (oc) -> {
            return true;
        };

        /*
		 * Submit object to the context (it will determine how to get the object into the repository)
		 * 
         */
        this.submitter = (od) -> {
            this.context.load(od, this.identity);
        };

        /*
		 * Step 2 - Assembly
         */
        this.assembler = ActiveObjectAssembler.create(fieldCreator, dataChecker, objectCreator, objectChecker, submitter);

        /*
		 * Rows to Fields
         */
        this.rowsToFields = (resultSet) -> {
            List<ActiveTypeValue> typeValues = new ArrayList<>();
            for (ActiveType activeType : types) {
                if (badFields.containsKey(activeType.name())) {
                    continue;
                }
                switch (activeType.role()) {
                    case Value:
                        ValueType valueType = (ValueType) activeType;
                        switch (valueType.fieldType().dataType().primitiveType().dataGroup()) {
                            case DECIMAL:
                                try {
                                    typeValues.add(ActiveTypeValue.create(activeType, resultSet.getBigDecimal(valueType.name())));
                                } catch (Exception ex) {
                                    badFields.put(valueType.name(), ex);
                                }
                                break;
                            case NUMBER:
                                try {
                                    typeValues.add(ActiveTypeValue.create(activeType, resultSet.getLong(valueType.name())));
                                } catch (Exception ex) {
                                    badFields.put(valueType.name(), ex);
                                }
                                break;
                            default:
                                try {
                                    typeValues.add(ActiveTypeValue.create(activeType, resultSet.getString(valueType.name())));
                                } catch (Exception ex) {
                                    badFields.put(valueType.name(), ex);
                                }
                                break;
                        }
                        break;
                    default:
                        break;
                }

            }
            return typeValues;
        };

        /*
		 * 
         */
        this.validateField = (atv) -> {
            return true;
        };

        /*
		 * 
         */
        this.rowProcessor = DbRowProcessor.create(this.types,
                this.rowsToFields,
                this.validateField,
                this.assembler);
    }

    /**
     *
     *
     */
    public void throwException() throws Exception {
        if (this.exception != null) {
            throw this.exception;
        }
    }

    /**
     *
     * Run
     *
     */
    @Override
    public Result call() throws Exception {
        String origThreadName = Thread.currentThread().getName();
        Thread.currentThread().setName(String.format("%s.%s", this.context.name(), this.objectType.name()));
        Result result = new LoadFromQueryResultImpl(this.objectType, this.sql);
        int rows = 0;
        int rowsInLastNMillis = 0;
        int logEveryNMillis = 5000;
        try {
            try (
                    DBConnect db = DBConnect.get(this.dbEndPoint);
                    Statement stmt = db.createStatement();
                    ResultSet resultSet = stmt.executeQuery(this.sql);) {
                LocalDateTime startTS = LocalDateTime.now();
                LocalDateTime lastTS = startTS;
                while (resultSet.next()) {
                    this.rowProcessor.process(resultSet);
                    rows++;
                    rowsInLastNMillis++;
                    if (Duration.between(lastTS, LocalDateTime.now()).toMillis() >= logEveryNMillis) {
                        long totalMS = Duration.between(startTS, LocalDateTime.now()).toMillis();
                        LGR.info("{} - {} rows in last {} seconds. {} total rows in {} seconds. Avg: {}ms per row",
                                this.objectType.name(),
                                rowsInLastNMillis,
                                logEveryNMillis * 0.001,
                                rows,
                                totalMS,
                                totalMS / rows
                        );
                        lastTS = LocalDateTime.now();
                        rowsInLastNMillis = 0;
                    }
                    if (this.killSwitch == true) {
                        LGR.info("{} {} rows", rows, this.objectType.name());
                        LGR.info("{}.run() - forced stop of loader for '{}' using query '{}'",
                                this.getClass().getSimpleName(), this.objectType.name(),
                                this.sql);
                        return result.killed(rows);
                    }
                }
                LGR.info("{} - total {} @ {}ms", this.objectType.name(), rows,
                        Duration.between(startTS, LocalDateTime.now()).toMillis());
            }
        } catch (Exception ex) {
            this.exception = ex;
            LGR.error("{}.call() - attempting to load: {}.{} using {}\nhas resulted in Exception: {}",
                    this.getClass().getSimpleName(),
                    this.context.name(),
                    this.objectType.name(),
                    this.sql,
                    Functions.stackTraceToString(ex));
            return result.exception(rows, ex);
        } finally {
            Thread.currentThread().setName(origThreadName);
        }
        return result.result(rows);
    }
}
