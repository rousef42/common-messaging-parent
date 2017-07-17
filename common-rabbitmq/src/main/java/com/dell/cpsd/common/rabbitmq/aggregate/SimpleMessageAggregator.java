/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Temporal solution. Must be replaced with a proper aggregator.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public class SimpleMessageAggregator<M extends Aggregate> implements MessageAggregator<M>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMessageAggregator.class);

    private Supplier<M> aggregateSupplier;
    private Consumer<M> onCompleteAction;
    private Consumer<M> onDeprecationAction;
    private int timeToLiveSeconds = 24 * 3600;

    private Map<String, AggregateWrapper> aggregates = new HashMap<>();

    /**
     * Constructor with onDeprecationAction.
     *
     * @param aggregateSupplier   aggregate creator
     * @param onCompleteAction    action to be taken when aggregate is complete
     * @param onDeprecationAction action to be taken when an aggregate is deprecated
     */
    public SimpleMessageAggregator(final Supplier<M> aggregateSupplier, final Consumer<M> onCompleteAction,
            final Consumer<M> onDeprecationAction)
    {
        if (aggregateSupplier == null || onCompleteAction == null)
        {
            throw new IllegalArgumentException("aggregateSupplier and onCompleteAction connot be null");
        }
        this.aggregateSupplier = aggregateSupplier;
        this.onCompleteAction = onCompleteAction;
        this.onDeprecationAction = onDeprecationAction;
        if (this.onDeprecationAction == null)
        {
            this.onDeprecationAction = this::logDeprecation;
        }
    }

    /**
     * Constructor using logging as onDeprecationAction.
     *
     * @param aggregateSupplier aggregate creator
     * @param onCompleteAction  action to be taken when aggregate is complete
     */
    public SimpleMessageAggregator(final Supplier<M> aggregateSupplier, final Consumer<M> onCompleteAction)
    {
        this(aggregateSupplier, onCompleteAction, null);
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

    /**
     * Get aggregate for given correlationId.<br>
     * If there is no aggregate for given correlation, then new one is created.
     * Do not use this method to check if correlationId is present, use {@link #checkIfCorrelationIdPresent(String)} instead.
     *
     * @param correlationId correlation id for an aggregate
     * @return aggregate corellated to given id
     */
    public M getAggregate(final String correlationId)
    {
        AggregateWrapper wrapper = aggregates.get(correlationId);
        if (wrapper == null)
        {
            LOGGER.info("No AggregateWrapper for correlationId={}", correlationId);
            final M agg = aggregateSupplier.get();
            wrapper = new AggregateWrapper(agg);
            aggregates.put(correlationId, wrapper);
        }
        return wrapper.getAggregate();
    }

    /**
     * Check if given correlationId is present.
     * Do not use {@link #getAggregate(String)} to check if correlationId is present.
     */
    public boolean checkIfCorrelationIdPresent(final String correlationId)
    {
        return aggregates.get(correlationId) != null;
    }

    protected void deleteOldAggregates()
    {
        final Date oldestAllowedCreationDate = DateUtils.addSeconds(new Date(), -timeToLiveSeconds);
        final Set<Map.Entry<String, AggregateWrapper>> entries = aggregates.entrySet();
        for (final Map.Entry<String, AggregateWrapper> entry : entries)
        {
            if (entry.getValue().getCreationDate().before(oldestAllowedCreationDate))
            {
                onDeprecationAction.accept(entry.getValue().getAggregate());
                entries.remove(entry);
            }
        }
    }

    /**
     * Default onDeprecationAction - just logging the fact of a deprecation.
     */
    protected void logDeprecation(final M aggregate)
    {
        LOGGER.info("Removing old (>{}s) aggregate: {}", timeToLiveSeconds, aggregate);
    }

    protected int getTimeToLiveSeconds()
    {
        return timeToLiveSeconds;
    }

    protected void setTimeToLiveSeconds(final int timeToLiveSeconds)
    {
        this.timeToLiveSeconds = timeToLiveSeconds;
    }

    private class AggregateWrapper
    {
        private M    aggregate    = null;
        private Date creationDate = new Date();

        AggregateWrapper(final M aggregate)
        {
            this.aggregate = aggregate;
        }

        M getAggregate()
        {
            return aggregate;
        }

        Date getCreationDate()
        {
            return creationDate;
        }
    }
}
