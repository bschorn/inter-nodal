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

import org.schorn.ella.Mingleton;
import org.schorn.ella.context.ActiveContext.Contextual;
import org.schorn.ella.node.ActiveNode.ArrayData;
import org.schorn.ella.node.ActiveNode.ObjectData;
import org.schorn.ella.repo.RepoActions.ActivityActionable;
import org.schorn.ella.repo.RepoActions.ActivityEntitled;
import org.schorn.ella.repo.RepoActions.ActivityTrigger;
import org.schorn.ella.repo.RepoActions.LogActivity;
import org.schorn.ella.repo.RepoActions.NotifyOfActivity;
import org.schorn.ella.repo.RepoActions.MaintainReferences;
import org.schorn.ella.repo.RepoActions.RegisterActivity;
import org.schorn.ella.repo.RepoActions.ValidateActivity;
import org.schorn.ella.repo.RepoSupport.ActiveQuery;
import org.schorn.ella.repo.RepoSupport.QueryData;

/**
 *
 * The interfaces and default implementations under the RepoCoordinators
 * umbrella are mostly for provided clarity in the process flow.
 * RepoCoordinators contain one or more RepoActions and control the flow of an
 * ObjectData through various stages.
 *
 *
 * @author schorn
 *
 */
public interface RepoCoordinators {

    /**
     * Common Interface
     *
     *
     */
    interface Coordinator extends Contextual, Mingleton {

    }

    /**
     * Approvals: Constraints, Entitlements, Workflow/StateMgt
     *
     */
    interface Inspector extends Coordinator {

        ValidateActivity isValid();

        ActivityEntitled isEntitled();

        ActivityActionable isActionable();

        /**
         *
         * @param objectData
         * @return
         */
        default boolean approve(ObjectData objectData) {
            // constraints
            if (this.isValid().test(objectData)) {
                // is identity entitled to perform this input request 
                if (this.isEntitled().test(objectData)) {
                    // does the request make sense (flow/state management)
                    if (this.isActionable().test(objectData)) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

    /**
     * Receiver: persist and in-memory
     *
     */
    interface Receiver extends Coordinator {

        LogActivity persistActivity();

        RegisterActivity registerActivity();

        MaintainReferences maintainReferences();

        /**
         * Receive entry for Repo
         * <p>
         *
         *
         * @param objectData
         * @return
         */
        default void receive(ObjectData objectData) {
            // append to file based repo log
            this.persistActivity().accept(objectData);
            // append to in-memory repo log
            this.registerActivity().accept(objectData);
            // build relationships
            this.maintainReferences().accept(objectData);
        }
    }

    /**
     * Recovery Receiver: in-memory only.
     *
     */
    interface RecoveryReceiver extends Receiver {

        /**
         * Recovery should not re-persist (the recovery file already these)
         */
        default void receive(ObjectData objectData) {
            // append to in-memory repo log
            this.registerActivity().accept(objectData);
            // build relationships
            this.maintainReferences().accept(objectData);
        }
    }

    /**
     * Distribute: Internal Events, External Publishing (tell me when it
     * changes)
     *
     */
    interface Dispatcher extends Coordinator {

        ActivityTrigger activityTrigger();

        NotifyOfActivity notifyOfActivity();

        default void distribute(ObjectData objectData) {
            /*
			 * Internal Events
             */
            this.activityTrigger().accept(objectData);
            /*
			 * External Subscriptions
             */
            this.notifyOfActivity().accept(objectData);
        }
    }

    /**
     * Responses: Queries (tell me what it is now)
     *
     */
    interface Responder extends Coordinator {

        QueryData query(ActiveQuery activeQuery);

        QueryData queryEvents(ActiveQuery activeQuery);
    }

    /**
     *
     *
     */
    interface Summary extends Coordinator {

        /**
         * Creates a Array or Object (depends on implementation) of where
         * implementer believes is a summarized status of the entire Repo.
         *
         * @return
         */
        ArrayData summary();

    }
}
