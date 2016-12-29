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
public class ResponseDetails
{
    private String exchange;
    private String routingKey;
    private Object body;

    public ResponseDetails(String exchange, String routingKey, Object body)
    {
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.body = body;
    }

    @Override
    public String toString()
    {
        return "ResponseDetails{" +
                "exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", body=" + (body == null ? null : body.getClass().getSimpleName()) +
                '}';
    }

    public String getExchange()
    {
        return exchange;
    }

    public String getRoutingKey()
    {
        return routingKey;
    }

    public Object getBody()
    {
        return body;
    }
}
