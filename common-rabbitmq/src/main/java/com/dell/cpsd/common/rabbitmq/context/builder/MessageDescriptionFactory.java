/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.rabbitmq.annotation.Message;
import com.dell.cpsd.common.rabbitmq.annotation.MessageContentType;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageExchange;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageExchangeType;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.OpinionConstants;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageEvent;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageReply;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageRequest;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageStereotype;
import com.dell.cpsd.common.rabbitmq.context.ApplicationConfiguration;
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
    public static final String PROVIDER_ID_PLACEHOLDER = "{providerId}";
    private ApplicationConfiguration applicationConfiguration;

    public MessageDescriptionFactory(ApplicationConfiguration applicationConfiguration)
    {
        this.applicationConfiguration = applicationConfiguration;
    }

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
            exchange = applicationFlavouredExchange(exchangeAnnotation.exchange());
            exchangeType = exchangeAnnotation.exchangeType();
            if (OpinionConstants.isDefined(exchangeAnnotation.routingKey()))
            {
                routingKey = exchangeAnnotation.routingKey();
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
        return description;
    }

    private String applicationFlavouredExchange(String exchange)
    {
        if (exchange != null)
        {
            if (exchange.contains(PROVIDER_ID_PLACEHOLDER))
            {
                // This can be tuned later if necessary
                return exchange.replace("{providerId}", applicationConfiguration.getApplicationName());
            }
        }
        return exchange;
    }
}
