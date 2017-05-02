/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.error;

/**
 * Context for error transformation.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class ErrorContext<M>
{
    protected M      requestMessage;
    protected String errorRoutingKeyPrefix;

    public ErrorContext(M requestMessage, String errorRoutingKeyPrefix)
    {
        this.requestMessage = requestMessage;
        this.errorRoutingKeyPrefix = errorRoutingKeyPrefix;
    }

    public M getRequestMessage()
    {
        return requestMessage;
    }

    public String getErrorRoutingKeyPrefix()
    {
        return errorRoutingKeyPrefix;
    }
}
