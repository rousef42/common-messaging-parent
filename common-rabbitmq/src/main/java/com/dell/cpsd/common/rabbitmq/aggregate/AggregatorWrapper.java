/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

/**
 * Generic message aggregator interface.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.1.2
 */
public interface AggregatorWrapper
{
    /**
     * This forces the completion of the message aggregator.
     *
     * @param initialRequestCorrelationId The initial correlation identifier.
     * @since 2.1.2
     */
    void forceCompletion(final String initialRequestCorrelationId);

    /**
     * This returns true of the correlation identifier is being tracked by an aggregator.
     *
     * @param initialRequestCorrelationId The initial correlation identifier.
     * @throws Exception Thrown if the message cannot be processed.
     * @since 2.1.2
     */
    boolean checkIfCorrelationIdPresent(final String initialRequestCorrelationId)
            throws Exception;
}
