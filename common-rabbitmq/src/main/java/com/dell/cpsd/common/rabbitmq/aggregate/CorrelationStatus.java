/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

/**
 * Status of a correlation.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.1.2
 */
public enum CorrelationStatus
{
    /**
     * The request has been sent.
     *
     * @since 2.1.2
     */
    REQUESTED,
    /**
     * The answer received.
     *
     * @since 2.1.2
     */
    RESPONDED,
    /**
     * The request has failed.
     *
     * @since 2.1.2
     */
    FAILED
}
