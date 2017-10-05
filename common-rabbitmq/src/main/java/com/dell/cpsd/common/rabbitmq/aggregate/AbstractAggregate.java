/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract aggregate implementation.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.1.2
 */
public abstract class AbstractAggregate
        implements Aggregate
{
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAggregate.class);

    /**
     * The flag to force completion.
     */
    private volatile boolean forcedToComplete = false;

    /**
     * The correlation identifiers with their current state.
     */
    private final Map<String, CorrelationStatus> correlationStates = new HashMap<>();

    @Override
    public boolean isComplete()
    {
        boolean isComplete = false;

        // if the aggegator cannot complete, then it is complete.
        if (this.isForcedToComplete())
        {
            LOGGER.debug("Is forced to complete");
            isComplete = true;
        }
        else
        {
            // if the aggregator doesn't have any active requests, then it is complete
            synchronized (this.correlationStates)
            {
                isComplete = !containsRequested();
            }
        }

        LOGGER.info("isComplete: [{}]", isComplete);

        return isComplete;
    }

    /**
     * This forces the completion of this aggregate.
     *
     * @since 2.1.2
     */
    public void forceCompletion()
    {
        this.forcedToComplete = true;
    }

    /**
     * This returns the value of the forced completion flag.
     *
     * @return The value of the forced completion flag.
     * @since 2.1.2
     */
    public boolean isForcedToComplete()
    {
        return this.forcedToComplete;
    }

    /**
     * Sets a correlation status in the map of &lt;correlation-id,correlation-status&gt;.
     *
     * @param correlationId The correlation identifier that status will be updated.
     * @param status        The status to be set.
     * @since 2.1.2
     */
    public void updateCorrelationStatus(final String correlationId,
            final CorrelationStatus status)
    {
        if (correlationId == null)
        {
            LOGGER.warn("Correlation id is null, no status updated");
            return;
        }

        LOGGER.info("Updating correlation id [{}] with status [{}]", correlationId, status);

        synchronized (this.correlationStates)
        {
            if (this.correlationStates.containsKey(correlationId))
            {
                final boolean transitionCorrect = statusTransitionCorrect(this.correlationStates.get(correlationId));
                if (!transitionCorrect)
                {
                    LOGGER.warn("Incorrect state transition from [{}] to [{}] for correlation id [{}]");
                }
            }
            this.correlationStates.put(correlationId, status);
        }
    }

    /**
     * Transition from RESPONDED or FAILED should not happen.
     *
     * @param previousStatus The status <b>before</b> the update.
     * @return true if transition is correct.
     * @since 2.1.2
     */
    protected boolean statusTransitionCorrect(final CorrelationStatus previousStatus)
    {
        if (previousStatus == CorrelationStatus.RESPONDED || previousStatus == CorrelationStatus.FAILED)
        {
            return false;
        }

        return true;
    }

    /**
     * @return true if there is a correlation with status REQUESTED in the correlations map.
     * @since 2.1.2
     */
    protected boolean containsRequested()
    {
        return containsStatus(CorrelationStatus.REQUESTED);
    }

    /**
     * @return true if a given status type is in the correlations map.
     * @since 2.1.2
     */
    protected boolean containsStatus(final CorrelationStatus status)
    {
        synchronized (this.correlationStates)
        {
            return this.correlationStates.containsValue(status);
        }
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("forcedToComplete", forcedToComplete)
                .append("correlationStates", correlationStates)
                .toString();
    }
}
