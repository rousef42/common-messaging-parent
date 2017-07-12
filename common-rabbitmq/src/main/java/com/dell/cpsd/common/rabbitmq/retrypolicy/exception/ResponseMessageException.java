/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.retrypolicy.exception;

/**
 * Exception signalling that we need to send provided response message.
 * By default, no retries would be attempt.
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class ResponseMessageException extends RuntimeException
{
    private static final long serialVersionUID    = -5837101839767033032L;
    private static final int  DEFAULT_RETRY_COUNT = 0;

    private final int             maxRetryCount;
    private final ResponseDetails responseDetails;

    public ResponseMessageException(Throwable cause, String exchange, String routingKey, Object body)
    {
        this(cause, DEFAULT_RETRY_COUNT, exchange, routingKey, body);
    }

    public ResponseMessageException(Throwable cause, int maxRetryCount, String exchange, String routingKey, Object body)
    {
        this(cause, maxRetryCount, new ResponseDetails(exchange, routingKey, body));
    }

    public ResponseMessageException(Throwable cause, ResponseDetails responseDetails)
    {
        this(cause, DEFAULT_RETRY_COUNT, responseDetails);
    }

    public ResponseMessageException(Throwable cause, int maxRetryCount, ResponseDetails responseDetails)
    {
        super(cause);
        this.maxRetryCount = maxRetryCount;
        this.responseDetails = responseDetails;
    }

    @Override
    public String toString()
    {
        return "ResponseMessageException{" + "responseDetails=" + responseDetails + ", cause=" + getCause() + '}';
    }

    public int getMaxRetryCount()
    {
        return maxRetryCount;
    }

    public ResponseDetails getResponseDetails()
    {
        return responseDetails;
    }
}
