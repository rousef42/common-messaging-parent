/*
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

/**
 * Generic interface for message aggregates.
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @version TBD
 * @since TBD
 */
public interface Aggregate
{
    /**
     * True if aggregate got all expected messages.
     */
    boolean isComplete();
}
