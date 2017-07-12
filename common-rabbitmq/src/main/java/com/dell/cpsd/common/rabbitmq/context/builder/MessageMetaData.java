/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
  * @author Connor Goulding
 */
public class MessageMetaData
{
    private String message;
    private String exchange;
    private String exchangeType;
    private String routingKey;

    public MessageMetaData(String message, String exchange, String exchangeType, String routingKey)
    {
        this.message = message;
        this.exchange = exchange;
        this.exchangeType = exchangeType;
        this.routingKey = routingKey;
    }

    public MessageMetaData()
    {
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getExchange()
    {
        return exchange;
    }

    public void setExchange(String exchange)
    {
        this.exchange = exchange;
    }

    public String getExchangeType()
    {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType)
    {
        this.exchangeType = exchangeType;
    }

    public String getRoutingKey()
    {
        return routingKey;
    }

    public void setRoutingKey(String routingKey)
    {
        this.routingKey = routingKey;
    }
}
