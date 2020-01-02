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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ArrayType;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.ActiveNode.ValueType;
import org.schorn.ella.node.BondType;
import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.node.ValueFlag;
import org.schorn.ella.repo.RepoCoordinators.Summary;
import org.schorn.ella.repo.RepoData;
import org.schorn.ella.repo.RepoData.CurrentState;
import org.schorn.ella.util.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author schorn
 *
 */
public class SummaryImpl extends AbstractContextual implements Summary {

    private static final Logger LGR = LoggerFactory.getLogger(SummaryImpl.class);

    private final CurrentState current;
    private ArrayType typeSummaryTable;
    private ObjectType typeSummaryRow;

    SummaryImpl(AppContext context) {
        super(context);
        this.current = RepoData.CurrentState.get(this.context());
        List<ValueType> valueTypes = new ArrayList<>();
        try {
            valueTypes.add(ValueType.create(context, "ObjectType", MetaTypes.FieldTypes.TEXT.fieldType(), ValueFlag.getEnumSetFromLong(0)));
            valueTypes.add(ValueType.create(context, "Total", MetaTypes.FieldTypes.NUMBER.fieldType(), ValueFlag.getEnumSetFromLong(0)));
            valueTypes.add(ValueType.create(context, "Utilization", MetaTypes.FieldTypes.DECIMAL.fieldType(), ValueFlag.getEnumSetFromLong(0)));
            valueTypes.add(ValueType.create(context, "Bytes", MetaTypes.FieldTypes.NUMBER.fieldType(), ValueFlag.getEnumSetFromLong(0)));
            ObjectType.Builder builder = ObjectType.builder(context, "-RepoSummary-", new ArrayList<>());
            valueTypes.stream().forEach(vt -> builder.add(vt, BondType.OPTIONAL));
            this.typeSummaryRow = builder.build();
            this.typeSummaryTable = ArrayType.create(context, this.typeSummaryRow.name(), this.typeSummaryRow, BondType.OPTIONAL);
        } catch (Exception ex) {
            LGR.error("{}.ctor ", this.getClass().getSimpleName(), Functions.getStackTraceAsString(ex));
        }
    }

    @Override
    public ArrayData summary() {
        try {
            ArrayData summaryTable = this.typeSummaryTable.create();
            Map<ObjectType, Stats> summary = new HashMap<>();
            for (ObjectType objectType : this.context().objectTypes()) {
                Stats stats = new Stats(objectType);
                summary.put(objectType, stats);
                this.current.read(objectType, null)
                        .forEach(od -> {
                            ++stats.objectCount;
                            stats.fieldCount += od.nodes().size();
                            int nullFields = objectType.schema().memberDefs().size() - (int) od.nodes().stream().filter(ad -> !ad.isNull()).count();
                            stats.nullFieldCount += nullFields;
                            stats.bytes += od.bytes();
                        });
            }
            for (ObjectType objectType : this.context().objectTypes()) {
                Stats stats = summary.get(objectType);
                double utilPct = 0.0;
                if (stats.fieldCount > 0) {
                    utilPct = 1.0 - ((double) stats.nullFieldCount / (double) stats.fieldCount);
                    utilPct *= 100;
                }
                ObjectData summaryRow = this.typeSummaryRow.create(
                        objectType.name(),
                        stats.objectCount,
                        utilPct,
                        stats.bytes / 1.0);
                summaryTable.add(summaryRow);
            }
            return summaryTable;
        } catch (Exception ex) {
            LGR.error(Functions.stackTraceToString(ex));
        }
        return ArrayData.empty("BadRepoSummary");
    }

    static class Stats {

        final ObjectType objectType;
        int objectCount = 0;
        int fieldCount = 0;
        int nullFieldCount = 0;
        int bytes = 0;

        Stats(ObjectType objectType) {
            this.objectType = objectType;
        }
    }
}
