/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

import com.dell.cpsd.common.rabbitmq.annotation.MessageContentType;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageStereotype;
import com.dell.cpsd.common.rabbitmq.context.builder.MessageExchangeType;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class MessageDescription<M>
{
    private Class<M>            messageClass = null;
    private MessageStereotype   stereotype   = null;
    private String              type         = null;
    private String              version      = null;
    private String              exchange     = null;
    private MessageExchangeType exchangeType = null;
    private String              routingKey   = null;
    private MessageContentType  contentType  = null;

    public MessageStereotype getStereotype()
    {
        return stereotype;
    }

    public void setStereotype(MessageStereotype stereotype)
    {
        this.stereotype = stereotype;
    }

    public String getRoutingKey()
    {
        return routingKey;
    }

    public void setRoutingKey(String routingKey)
    {
        this.routingKey = routingKey;
    }

    public Class<M> getMessageClass()
    {
        return messageClass;
    }

    public void setMessageClass(Class<M> messageClass)
    {
        this.messageClass = messageClass;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getExchange()
    {
        return exchange;
    }

    public void setExchange(String exchange)
    {
        this.exchange = exchange;
    }

    public MessageExchangeType getExchangeType()
    {
        return exchangeType;
    }

    public void setExchangeType(MessageExchangeType exchangeType)
    {
        this.exchangeType = exchangeType;
    }

    public MessageContentType getContentType()
    {
        return contentType;
    }

    public void setContentType(MessageContentType contentType)
    {
        this.contentType = contentType;
    }
}
