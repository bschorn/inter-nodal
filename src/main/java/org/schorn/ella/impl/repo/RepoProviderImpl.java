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

import java.util.function.Supplier;
import org.schorn.ella.AbstractProvider;
import org.schorn.ella.context.AppContext;
import org.schorn.ella.event.ActiveEvent;
import org.schorn.ella.event.ActiveEvent.EventFlag;
import org.schorn.ella.node.ActiveNode;
import org.schorn.ella.repo.ActiveRepo;
import org.schorn.ella.repo.RepoActions;
import org.schorn.ella.repo.RepoCoordinators;
import org.schorn.ella.repo.RepoData;
import org.schorn.ella.repo.RepoData.EventLogBroker;
import org.schorn.ella.repo.RepoProvider;
import org.schorn.ella.repo.RepoSupport;
import org.schorn.ella.repo.RepoSupport.ConditionStatementParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author schorn
 *
 */
public class RepoProviderImpl extends AbstractProvider implements RepoProvider {

    private static final Logger LGR = LoggerFactory.getLogger(RepoProviderImpl.class);

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *                                
	 *                                METHODS
	 *                                
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    @Override
    public void init() {
        this.mapInterfaceToImpl(RepoData.EventLog.class, EventLogImpl.class);
        this.mapInterfaceToImpl(RepoData.EventLogBroker.class, EventLogBrokerImpl.class);
        this.mapInterfaceToImpl(RepoData.TopicsLog.class, TopicsLogImpl.class);
        this.mapInterfaceToImpl(RepoData.CurrentState.class, CurrentStateImpl.class);
        this.mapInterfaceToImpl(RepoActions.ValidateActivity.class, RepoActions.ValidateActivity.class);
        this.mapInterfaceToImpl(RepoActions.ActivityActionable.class, ActionableImpl.class);
        this.mapInterfaceToImpl(RepoActions.ActivityEntitled.class, EntitledImpl.class);
        this.mapInterfaceToImpl(RepoActions.ActivityTrigger.class, TriggerImpl.class);
        this.mapInterfaceToImpl(RepoActions.NotifyOfActivity.class, NotifyImpl.class);
        this.mapInterfaceToImpl(RepoActions.LogActivity.class, LogActivityImpl.class);
        this.mapInterfaceToImpl(RepoActions.RegisterActivity.class, StoreImpl.class);
        this.mapInterfaceToImpl(RepoActions.QueryExecution.class, QueryExecutionStateImpl.class);
        this.mapInterfaceToImpl(RepoActions.QueryExecutionEvents.class, QueryExecutionEventsImpl.class);
        this.mapInterfaceToImpl(RepoActions.MaintainReferences.class, MaintainReferencesImpl.class);
        this.mapInterfaceToImpl(RepoSupport.QueryData.class, QueryDataImpl.class);
        this.mapInterfaceToImpl(RepoSupport.ActiveFilter.class, ActiveFilterImpl.class);
        this.mapInterfaceToImpl(RepoSupport.FilteredObjectData.class, FilteredObjectDataImpl.class);
        this.mapInterfaceToImpl(RepoSupport.ActiveQuery.Builder.class, ActiveQueryImpl.BuilderImpl.class);
        this.mapInterfaceToImpl(RepoSupport.ActiveUpdate.class, ActiveUpdateImpl.class);
        this.mapInterfaceToImpl(RepoSupport.UpdateData.class, UpdateDataImpl.class);
        this.mapInterfaceToImpl(RepoSupport.ActiveCondition.class, ActiveConditionImpl.class);
        this.mapInterfaceToImpl(RepoSupport.QueryNodeParser.class, QueryNodeParserImpl.class);
        this.mapInterfaceToImpl(RepoSupport.ConditionStatementParser.class, ConditionStatementParserImpl.class);
        this.mapInterfaceToImpl(RepoCoordinators.Inspector.class, InpsectorImpl.class);
        this.mapInterfaceToImpl(RepoCoordinators.Receiver.class, ReceiverImpl.class);
        this.mapInterfaceToImpl(RepoCoordinators.RecoveryReceiver.class, RecoveryReceiverImpl.class);
        this.mapInterfaceToImpl(RepoCoordinators.Dispatcher.class, DispatcherImpl.class);
        this.mapInterfaceToImpl(RepoCoordinators.Responder.class, ResponderImpl.class);
        this.mapInterfaceToImpl(RepoCoordinators.Summary.class, RepoStatImpl.class);
        this.mapInterfaceToImpl(ActiveRepo.ServicesRepo.class, ServicesRepoImpl.class);
        this.mapInterfaceToImpl(ActiveRepo.LoaderRepo.class, SystemsRepoImpl.class);

    }

    @Override
    public void registerContext(AppContext context) throws Exception {
        super.registerContext(context);
        /*
         * We need to make sure this gets created here before some dependencies come looking for it
         */
        @SuppressWarnings("unused")
        EventLogBroker broker = this.getMingleton(EventLogBroker.class, context);

    }

    @Override
    public Supplier<?> getActivitySupplier(AppContext context) {
        RepoActions.LogActivity persist = this.getMingleton(RepoActions.LogActivity.class, context);
        if (persist instanceof Supplier) {
            return (Supplier<?>) persist;
        }
        LGR.error(String.format("%s.getActivitySupplier() - Context: '%s' Persist implementation"
                + " '%s' is not a Supplier which is required by ActiveIO.ActiveRecord.",
                this.getClass().getSimpleName(),
                context.name(),
                persist.getClass().getSimpleName()));
        return null;
    }

    @Override
    public EventFlag getRepoUpdateEventFlag() {
        return ActiveEvent.EventFlag.REPO_TRIGGER_CHANGE_EVENT;
    }

    @Override
    public EventLogBroker getContextEventLogBroker(AppContext context) {
        return EventLogBrokerImpl.getContextEventLogBroker(context);
    }

    @Override
    public ActiveRepo.EndOfQueue getEndOfQueueMarker() {
        return EndOfQueueImpl.MARKER;
    }

    @Override
    public ConditionStatementParser createConditionStatementParser(AppContext context, ActiveNode.ActiveOperator[] operators) throws Exception {
        return new ConditionStatementParserImpl(context, operators);
    }

}
