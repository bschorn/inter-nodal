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
package org.schorn.ella.impl.sql;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.sql.ActiveSQL.SynchronizeObjects;
import org.schorn.ella.sql.RDBMS;
import org.schorn.ella.sql.RDBMS.JDBCExecutable;
import org.schorn.ella.sql.RDBMS.SQLDialect;
import org.schorn.ella.sql.RDBMS.SQLProgramCode;
import org.schorn.ella.sql.RDBMS.SQLProgramData;

/**
 *
 * @author schorn
 *
 */
public abstract class SynchronizeObjectsImpl extends AbstractContextual implements SynchronizeObjects, SQLProgramCode, SQLProgramData {

    private final RDBMS.SQLDialect dialect;
    private final RDBMS.SQLInterpreter interpreter;
    private final List<ObjectType> orderedTypeList;
    private final Deque<ObjectType> reversedTypeList;
    private final Map<ObjectType, List<ObjectData>> orderedDataLists;

    /*
	 * Keep track of the order that the objects come in
     */
    protected SynchronizeObjectsImpl(AppContext context, RDBMS.SQLDialect dialect) {
        super(context);
        this.dialect = dialect;
        this.interpreter = RDBMS.SQLInterpreter.create(this.context());
        this.orderedTypeList = new ArrayList<>();
        this.reversedTypeList = new LinkedList<>();
        this.orderedDataLists = new HashMap<>();
    }

    /*
	 * 
     */
    @Override
    public void accept(ObjectData objectData) {
        if (!this.orderedTypeList.stream().filter(ot -> ot.equals(objectData.objectType())).findAny().isPresent()) {
            this.orderedTypeList.add(objectData.objectType());
            this.reversedTypeList.addFirst(objectData.objectType());
        }
        List<ObjectData> dataList = this.orderedDataLists.get(objectData.objectType());
        if (dataList == null) {
            dataList = new ArrayList<>();
            this.orderedDataLists.put(objectData.objectType(), dataList);
        }
        dataList.add(objectData);
    }

    /*
	 * 
     */
    @Override
    public JDBCExecutable getExecutable() {
        return this.interpreter.compile(this, this);
    }

    @Override
    public List<ObjectType> orderedTypes() {
        return this.orderedTypeList;
    }

    @Override
    public List<ObjectType> reverseOrderTypes() {
        return this.reversedTypeList.stream().collect(Collectors.toList());
    }

    @Override
    public List<ObjectData> getOrderedDataList(ObjectType objectType) {
        return this.orderedDataLists.get(objectType);
    }

    @Override
    public SQLDialect dialect() {
        return this.dialect;
    }
}
