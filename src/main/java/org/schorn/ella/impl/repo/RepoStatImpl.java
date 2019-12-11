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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ActiveData;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
//import org.schorn.ella.node.ActiveNode.ObjectData.SeriesKey;
import org.schorn.ella.node.ActiveNode.ObjectType;
import org.schorn.ella.node.MetaTypes;
import org.schorn.ella.repo.RepoData.EventLogBroker;
import org.schorn.ella.repo.RepoData.RepoStat;
import org.schorn.ella.repo.RepoProvider;
import org.schorn.ella.util.Functions;

/**
 * Passive Listener for the purpose of collecting stats
 *
 *
 * @author schorn
 *
 */
public class RepoStatImpl extends AbstractContextual implements RepoStat {

    static final Logger LGR = LoggerFactory.getLogger(RepoStatImpl.class);

    static class Summary {

        ObjectType objectType;
        LocalDateTime min_datetime = LocalDateTime.now();
        LocalDateTime max_datetime = LocalDateTime.now();
        Map<Integer, ObjectData> uniqueInstances = new HashMap<>();

        Summary(ObjectType objectType) {
            this.objectType = objectType;
        }

        Summary(ObjectData objectData) {
            this.objectType = objectData.objectType();
            this.uniqueInstances.put(objectData.getSeriesKey(), objectData);
        }

        void set(ObjectData objectData) {
            this.uniqueInstances.put(objectData.getSeriesKey(), objectData);
            this.max_datetime = LocalDateTime.now();
        }
    }

    private final Map<ObjectType, Summary> summaryMap;

    RepoStatImpl(AppContext context) {
        super(context);
        EventLogBroker broker = RepoProvider.provider().getMingleton(EventLogBroker.class, context);
        broker.join(this);
        this.summaryMap = new HashMap<>();
    }

    @Override
    public void accept(ObjectData objectData) {
        Summary summary = this.summaryMap.get(objectData.objectType());
        if (summary == null) {
            summary = new Summary(objectData);
            this.summaryMap.put(objectData.objectType(), summary);
        } else {
            summary.set(objectData);
        }
    }

    @Override
    public ArrayData summary() {
        /*
		 * Results Array
         */
        ArrayData repoStats = MetaTypes.Arrays.repo_stats.arrayType().create();
        /*
		 * Iterate the summaries by ObjectType, creating a dummy for those with no activity
         */
        for (ObjectType objectType : this.context().objectTypes()) {
            Summary summary = this.summaryMap.get(objectType);
            if (summary == null) {
                summary = new Summary(objectType);
            }
            /*
			 * How many of this type?
             */
            int count = summary.uniqueInstances.values().size();
            /*
			 * What is the total byte size used of this type?
             */
            int bytes = count * objectType.bytes();
            /*
			 * Determine amount of bytes with null value
             */
            int unusedBytes = 0;
            for (ObjectData objectData : summary.uniqueInstances.values()) {
                OptionalInt optInteger = objectData.nodes().stream().filter(ad -> ad.isNull()).mapToInt(ad -> ad.bytes()).reduce(Integer::sum);
                if (optInteger.isPresent()) {
                    unusedBytes += optInteger.getAsInt();
                }
            }
            /*
			 * Create a row for this type
             */
            ObjectData.Builder builder = MetaTypes.ObjectTypes.repo_stats.objectType().builder();
            builder.add(MetaTypes.ValueTypes.object_type.valueType().create(objectType.name()));
            builder.add(MetaTypes.ValueTypes.count.valueType().create(count));
            builder.add(MetaTypes.ValueTypes.bytes.valueType().create(bytes));
            builder.add(MetaTypes.ValueTypes.null_bytes.valueType().create(unusedBytes));
            builder.add(MetaTypes.ValueTypes.min_datetime.valueType().create(summary.min_datetime));
            builder.add(MetaTypes.ValueTypes.max_datetime.valueType().create(summary.max_datetime));
            /*
			 * Add row to results table
             */
            try {
                repoStats.add(builder.build());
            } catch (Exception e) {
                LGR.error("{}.summary() - ",
                        this.getClass().getSimpleName(),
                        Functions.getStackTraceAsString(e));
            }
        }
        return repoStats;
    }

    @Override
    public Stream<ObjectData> read(ObjectType objectType, Predicate<ActiveData> filter) {
        return null;
    }

}
