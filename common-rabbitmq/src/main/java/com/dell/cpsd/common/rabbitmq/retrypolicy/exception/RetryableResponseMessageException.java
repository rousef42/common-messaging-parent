/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.retrypolicy.exception;

/**
 * TODO: Document usage. Set proper Vision version in since tag.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class RetryableResponseMessageException extends ResponseMessageException
{
    private static final long serialVersionUID = -8141904275698452459L;

    public RetryableResponseMessageException(Throwable cause, String exchange, String routingKey, Object body)
    {
        super(cause, exchange, routingKey, body);
    }

    public RetryableResponseMessageException(Throwable cause, ResponseDetails responseDetails)
    {
        super(cause, responseDetails);
    }
}
