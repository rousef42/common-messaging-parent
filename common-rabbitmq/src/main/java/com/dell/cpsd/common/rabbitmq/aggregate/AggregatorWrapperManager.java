/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

/**
 * This interface should be implemented by an aggregator manager.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.1.2
 */
public interface AggregatorWrapperManager<W extends AggregatorWrapper>
{
    /**
     * This returns the aggregation wrapper with the specified correlation identifier.
     *
     * @param correlationId The correlation identifier.
     * @return The aggreation wrapper, or null.
     * @since 2.1.2
     */
    W lookupAggregatorWrapper(final String correlationId);
}
