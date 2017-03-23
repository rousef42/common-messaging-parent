/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

import com.dell.cpsd.common.rabbitmq.annotation.MessageContentType;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageStereotype;
import com.dell.cpsd.common.rabbitmq.context.builder.MessageExchangeType;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class MessageDescription<M>
{
    private Class<M>            messageClass          = null;
    private MessageStereotype   stereotype            = null;
    private String              type                  = null;
    private String              version               = null;
    private String              exchange              = null;
    private MessageExchangeType exchangeType          = null;
    private String              routingKey            = null;
    private MessageContentType  contentType           = null;

    public void setMessageClass(Class<M> messageClass)
    {
        this.messageClass = messageClass;
    }

    public MessageStereotype getStereotype()
    {
        return stereotype;
    }

    public void setStereotype(MessageStereotype stereotype)
    {
        this.stereotype = stereotype;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public void setExchange(String exchange)
    {
        this.exchange = exchange;
    }

    public void setExchangeType(MessageExchangeType exchangeType)
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

    public Class<M> getMessageClass()
    {
        return messageClass;
    }

    public String getType()
    {
        return type;
    }

    public String getVersion()
    {
        return version;
    }

    public String getExchange()
    {
        return exchange;
    }

    public MessageExchangeType getExchangeType()
    {
        return exchangeType;
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
