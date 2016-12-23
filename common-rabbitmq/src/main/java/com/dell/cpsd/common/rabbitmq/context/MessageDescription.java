/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

import com.dell.cpsd.common.rabbitmq.annotation.MessageContentType;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageExchangeType;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageStereotype;

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
    private String              correlationIdProperty = null;
    private String              replyToProperty       = null;
    private String              timestampProperty     = null;
    private String              consumerBindingBase   = null;
    private String              producerRoutingKey    = null;
    private MessageContentType  contentType           = null;
    private String              containerAlias        = null;

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

    public void setCorrelationIdProperty(String correlationIdProperty)
    {
        this.correlationIdProperty = correlationIdProperty;
    }

    public void setReplyToProperty(String replyToProperty)
    {
        this.replyToProperty = replyToProperty;
    }

    public String getTimestampProperty()
    {
        return timestampProperty;
    }

    public void setTimestampProperty(String timestampProperty)
    {
        this.timestampProperty = timestampProperty;
    }

    public String getConsumerBindingBase()
    {
        return consumerBindingBase;
    }

    public void setConsumerBindingBase(String consumerBindingBase)
    {
        this.consumerBindingBase = consumerBindingBase;
    }

    public String getProducerRoutingKey()
    {
        return producerRoutingKey;
    }

    public void setProducerRoutingKey(String producerRoutingKey)
    {
        this.producerRoutingKey = producerRoutingKey;
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

    public String getCorrelationIdProperty()
    {
        return correlationIdProperty;
    }

    public String getReplyToProperty()
    {
        return replyToProperty;
    }

    public MessageContentType getContentType()
    {
        return contentType;
    }

    public void setContentType(MessageContentType contentType)
    {
        this.contentType = contentType;
    }

    public String getContainerAlias()
    {
        return containerAlias;
    }

    public void setContainerAlias(String containerAlias)
    {
        this.containerAlias = containerAlias;
    }
}
