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
public class ResponseMessageException extends RuntimeException
{
    private static final long serialVersionUID = -5837101839767033032L;

    private ResponseDetails responseDetails;

    public ResponseMessageException(Throwable cause, String exchange, String routingKey, Object body)
    {
        this(cause, new ResponseDetails(exchange, routingKey, body));
    }

    public ResponseMessageException(Throwable cause, ResponseDetails responseDetails)
    {
        super(cause);
        this.responseDetails = responseDetails;
    }

    @Override
    public String toString()
    {
        return "ResponseMessageException{" +
                "responseDetails=" + responseDetails +
                ", cause=" + getCause() +
                '}';
    }

    public ResponseDetails getResponseDetails()
    {
        return responseDetails;
    }
}
