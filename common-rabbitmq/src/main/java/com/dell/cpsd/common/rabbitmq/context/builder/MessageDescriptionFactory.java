/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.rabbitmq.annotation.Message;
import com.dell.cpsd.common.rabbitmq.annotation.MessageContentType;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageConsumer;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageExchange;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageExchangeType;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageProducer;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.OpinionConstants;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageEvent;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageReply;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageRequest;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageStereotype;
import com.dell.cpsd.common.rabbitmq.context.MessageDescription;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class MessageDescriptionFactory
{
    public <M> MessageDescription<M> createDescription(Class<M> messageClass)
    {
        String type = null;
        String version = null;
        String exchange = null;
        MessageExchangeType exchangeType = null;
        String correlationIdProperty = null;
        String replyToProperty = null;
        String timestampProperty = null;
        String routingKey = null;
        MessageStereotype stereotype = null;
        MessageContentType contentType = null;
        String containerAlias = null;

        // Basic Message Information
        Message messageAnnotation = messageClass.getAnnotation(Message.class);
        type = messageAnnotation.value();
        version = messageAnnotation.version();
        correlationIdProperty = messageAnnotation.correlationIdProperty();
        timestampProperty = messageAnnotation.timestampProperty();
        contentType = messageAnnotation.content();

        // Opinion
        MessageExchange exchangeAnnotation = messageClass.getAnnotation(MessageExchange.class);
        if (exchangeAnnotation != null)
        {
            exchange = exchangeAnnotation.exchange();
            exchangeType = exchangeAnnotation.exchangeType();
        }

        // Opinion
        MessageConsumer consumerAnnotation = messageClass.getAnnotation(MessageConsumer.class);
        if (consumerAnnotation != null)
        {
            if (OpinionConstants.isDefined(consumerAnnotation.routingKey()))
            {
                routingKey = consumerAnnotation.routingKey();
            }
            if (OpinionConstants.isDefined(consumerAnnotation.containerAlias()))
            {
                containerAlias = consumerAnnotation.containerAlias();
            }
        }

        // Opinion
        MessageProducer producerAnnotation = messageClass.getAnnotation(MessageProducer.class);
        if (producerAnnotation != null)
        {
            if (OpinionConstants.isDefined(producerAnnotation.routingKey()))
            {
                routingKey = producerAnnotation.routingKey();
            }
        }

        // Stereotype
        MessageRequest requestAnnotation = messageClass.getAnnotation(MessageRequest.class);
        if (requestAnnotation != null)
        {
            replyToProperty = requestAnnotation.replyToProperty();
            stereotype = requestAnnotation.stereotype();
        }

        // Stereotype
        MessageReply replyAnnotation = messageClass.getAnnotation(MessageReply.class);
        if (replyAnnotation != null)
        {
            stereotype = replyAnnotation.stereotype();
        }

        // Stereotype
        MessageEvent eventAnnotation = messageClass.getAnnotation(MessageEvent.class);
        if (eventAnnotation != null)
        {
            stereotype = eventAnnotation.stereotype();
        }

        MessageDescription<M> description = new MessageDescription<M>();
        description.setCorrelationIdProperty(correlationIdProperty);
        description.setExchange(exchange);
        description.setExchangeType(exchangeType);
        description.setMessageClass(messageClass);
        description.setRoutingKey(routingKey);
        description.setReplyToProperty(replyToProperty);
        description.setCorrelationIdProperty(correlationIdProperty);
        description.setStereotype(stereotype);
        description.setTimestampProperty(timestampProperty);
        description.setType(type);
        description.setVersion(version);
        description.setContentType(contentType);
        description.setContainerAlias(containerAlias);
        return description;
    }
}
