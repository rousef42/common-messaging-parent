/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.error;

/**
 * Transforms message processing exception into more specific exception. Ideally, ResponseMessageException.
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public interface ErrorTransformer<M>
{
    Exception transform(Exception e, ErrorContext<M> context);
}
