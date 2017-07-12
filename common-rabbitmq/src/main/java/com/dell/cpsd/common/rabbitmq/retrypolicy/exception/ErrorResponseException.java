/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.retrypolicy.exception;

/**
 * Indicates that AMQP message should be sent as a response to error.
 * Retry policy is calculated using root cause.
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class ErrorResponseException extends ResponseMessageException
{
    private static final long serialVersionUID = 4050522508860642811L;

    public ErrorResponseException(Throwable cause, String exchange, String routingKey, Object body)
    {
        super(cause, exchange, routingKey, body);
    }
}
