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

import org.schorn.ella.FunctionalAction;
import org.schorn.ella.Mingleton;
import org.schorn.ella.context.AbstractContextual;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.QueryData;

/**
 * Discreet Actions of the Repo that can have independent implementations within
 * and across contexts.
 *
 *
 *
 * @author schorn
 *
 */
public interface RepoActions {

    /**
     *
     */
    interface QueryExecution extends FunctionalAction.Mutate<ActiveQuery, QueryData>, Mingleton {

        /**
         * Execute Query and return ArrayData (resultset)
         *
         * @param activeQuery
         * @return
         */
        @Override
        QueryData apply(ActiveQuery activeQuery);
    }

    /**
     * Since this is a Mingleton we need a separate interface for State.
     */
    interface QueryExecutionState extends QueryExecution {
    }

    /**
     * Since this is a Mingleton we need a separate interface for Events.
     */
    interface QueryExecutionEvents extends QueryExecution {
    }

    public class ValidateActivity extends AbstractContextual implements FunctionalAction.Verify<ObjectData>, Mingleton {

        /**
         * Check Constraints
         * <p>
         * Are the contents within the static field-level constraints?
         *
         * @param structData
         * @return
         */
        public ValidateActivity(AppContext context) {
            super(context);
        }

        @Override
        public boolean test(ObjectData objectData) {
            return objectData.validate();
        }
    }

    interface ActivityEntitled extends FunctionalAction.Verify<ObjectData>, Mingleton {

        /**
         * Check Update Authorization of Identity
         * <p>
         * Does the Identity have authorization?
         *
         * @param structData
         * @return
         */
        @Override
        boolean test(ObjectData objectData);
    }

    interface ActivityActionable extends FunctionalAction.Verify<ObjectData>, Mingleton {

        /**
         * Check Rules
         * <p>
         * Is this an acceptable change of state?
         *
         *
         * @param structData
         * @return
         */
        @Override
        boolean test(ObjectData objectData);
    }

    interface LogActivity extends FunctionalAction.Consume<ObjectData>, FunctionalAction.Supply<ObjectData>, Mingleton {

        /**
         * Persist Object
         *
         * @param objectData
         * @return
         */
        @Override
        void accept(ObjectData objectData);

        /**
         * Supplier to persistence
         *
         */
        @Override
        ObjectData get();
    }

    interface RegisterActivity extends FunctionalAction.Consume<ObjectData>, Mingleton {

        /**
         * Store (repository)
         * <p>
         * When persist was confirmed in-line we store it cleanly.
         *
         * @param objectData
         * @return
         */
        @Override
        void accept(ObjectData objectData);
    }

    interface MaintainReferences extends FunctionalAction.Consume<ObjectData>, Mingleton {

        /**
         * Arranger (references)
         * <p>
         * Arranges objects by relationships defined in ActiveRef ActiveRef
         * activeRef = this.context().getActiveRef();
         * activeRef.get(LDS.Objects.MLST_FACILITY.objectType()).add(ReferenceType.PARENT_AS_ATTRIBUTE,
         * LDS.Objects.MLST_DEAL.objectType());
         *
         * @param objectData
         * @return
         */
        @Override
        void accept(ObjectData objectData);
    }

    interface ActivityTrigger extends FunctionalAction.Consume<ObjectData>, Mingleton {

        /**
         * Trigger (events)
         * <p>
         * When persist was confirmed in-line we notify it cleanly.
         *
         * @param objectData
         * @return
         */
        @Override
        void accept(ObjectData objectData);
    }

    interface NotifyOfActivity extends FunctionalAction.Consume<ObjectData>, Mingleton {

        /**
         * Notify (publish)
         * <p>
         * When persist was confirmed in-line we notify it cleanly.
         *
         * @param objectData
         * @return
         */
        @Override
        void accept(ObjectData objectData);
    }
}
