/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class is the default aggregator wrapper manager.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.1.2
 */
public class DefaultAggregatorWrapperManager<W extends AggregatorWrapper>
        implements AggregatorWrapperManager
{
    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAggregatorWrapperManager.class);

    /**
     * The list of aggregator wrappers.
     */
    private List<W> aggregatorWrappers;

    /**
     * DefaultAggregatorWrapperManager constructor.
     *
     * @param aggregatorWrappers The list of aggregator wrappers.
     * @throws IllegalArgumentException Thrown if the list is null.
     * @since 2.1.2
     */
    public DefaultAggregatorWrapperManager(final List<W> aggregatorWrappers)
    {
        super();

        if (aggregatorWrappers == null)
        {
            throw new IllegalArgumentException("The aggregator wrapper list is null.");
        }

        this.aggregatorWrappers = aggregatorWrappers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public W lookupAggregatorWrapper(final String correlationId)
    {
        for (final W aggregatorWrapper : this.aggregatorWrappers)
        {
            if (aggregatorWrapper != null)
            {
                try
                {
                    if (aggregatorWrapper.checkIfCorrelationIdPresent(correlationId))
                    {
                        return aggregatorWrapper;
                    }
                }
                catch (final Exception exception)
                {
                    LOGGER.error(exception.getMessage(), exception);
                }
            }
        }

        return null;
    }
}
