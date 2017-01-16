/*
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Temporal solution. Must be replaced with a proper aggregator.
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @version TBD
 * @since TBD
 */
public class SimpleMessageAggregator<M extends Aggregate> implements MessageAggregator<M>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMessageAggregator.class);

    private Supplier<M> aggregateSupplier;
    private Consumer<M> onCompleteAction;
    private int timeToLiveSeconds = 24 * 3600;

    private Map<String, AggregateWrapper> aggregates = new HashMap<>();

    public SimpleMessageAggregator(final Supplier<M> aggregateSupplier, final Consumer<M> onCompleteAction)
    {
        if (aggregateSupplier == null || onCompleteAction == null)
        {
            throw new IllegalArgumentException("aggregateSupplier and onCompleteAction connot be null");
        }
        this.aggregateSupplier = aggregateSupplier;
        this.onCompleteAction = onCompleteAction;
    }

    @Override
    public synchronized void process(final String correlationId, final Consumer<M> updateAction)
    {
        if (updateAction == null)
        {
            throw new IllegalArgumentException("updateAction connot be null");
        }

        final M aggregate = getAggregate(correlationId);

        updateAction.accept(aggregate);

        if (aggregate.isComplete())
        {
            LOGGER.info("Aggregated message [{}], Starting onComplete action.", correlationId);
            onCompleteAction.accept(aggregate);
            aggregates.remove(correlationId);
        }
        deleteOldAggregates();
    }

    public M getAggregate(final String correlationId)
    {
        AggregateWrapper wrapper = aggregates.get(correlationId);
        if (wrapper == null)
        {
            LOGGER.debug("No wrapper for correlationId=" + correlationId);
            final M agg = aggregateSupplier.get();
            wrapper = new AggregateWrapper(agg);
            aggregates.put(correlationId, wrapper);
        }
        return wrapper.getAggregate();
    }

    public boolean checkIfCorrelationIdPresent(String correlationId)
    {
        if(aggregates.get(correlationId)!=null)
            return true;
        else
            return false;
    }

    protected void deleteOldAggregates()
    {
        final Date oldestAllowedCreationDate = DateUtils.addSeconds(new Date(), -timeToLiveSeconds);
        aggregates.entrySet().removeIf(e -> e.getValue().getCreationDate().before(oldestAllowedCreationDate));
    }

    private class AggregateWrapper
    {
        private M    aggregate    = null;
        private Date creationDate = new Date();

        public AggregateWrapper(final M aggregate)
        {
            this.aggregate = aggregate;
        }

        public M getAggregate()
        {
            return aggregate;
        }

        public Date getCreationDate()
        {
            return creationDate;
        }
    }
}
