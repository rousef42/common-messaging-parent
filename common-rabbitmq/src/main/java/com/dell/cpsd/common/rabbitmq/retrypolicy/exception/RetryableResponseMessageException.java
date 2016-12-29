/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.retrypolicy.exception;

/**
 * Exception signalling that we need to retry operation, and only then send error message as a response to failure.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class RetryableResponseMessageException extends ResponseMessageException
{
    private static final long serialVersionUID = -8141904275698452459L;

    public RetryableResponseMessageException(ResponseMessageException responseException)
    {
        this(responseException.getCause(), responseException.getResponseDetails());
    }

    public RetryableResponseMessageException(Throwable cause, String exchange, String routingKey, Object body)
    {
        super(cause, exchange, routingKey, body);
    }

    public RetryableResponseMessageException(Throwable cause, ResponseDetails responseDetails)
    {
        super(cause, responseDetails);
    }
}
