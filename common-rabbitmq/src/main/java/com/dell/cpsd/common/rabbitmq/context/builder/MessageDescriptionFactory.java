/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dell.cpsd.common.rabbitmq.context.ApplicationConfiguration;
import com.dell.cpsd.common.rabbitmq.context.MessageDescription;
import com.dell.cpsd.contract.extension.amqp.annotation.Message;
import com.dell.cpsd.contract.extension.amqp.annotation.MessageContentType;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.EventMessage;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.ReplyMessage;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.RequestMessage;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.StereotypeMessage;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class MessageDescriptionFactory
{
    public static final String PROVIDER_ID_PLACEHOLDER = "{providerId}";

    private final ApplicationConfiguration applicationConfiguration;
    private final Map<String, MessageMetaData> metaDatas = new HashMap<>();

    public MessageDescriptionFactory(ApplicationConfiguration applicationConfiguration, Collection<MessageMetaData> metaDatas)
    {
        this.applicationConfiguration = applicationConfiguration;
        if (metaDatas != null)
        {
            metaDatas.forEach(metaData -> this.metaDatas.put(metaData.getMessage(), metaData));
        }
    }

    public <M> MessageDescription<M> createDescription(Class<M> messageClass)
    {
        String type = null;
        String version = null;
        StereotypeMessage stereotype = null;
        MessageContentType contentType = null;

        String exchange = null;
        MessageExchangeType exchangeType = null;
        String routingKey = null;

        // Basic Message Information
        Message messageAnnotation = messageClass.getAnnotation(Message.class);
        type = messageAnnotation.value();
        version = messageAnnotation.version();
        contentType = messageAnnotation.content();

        // Opinion
        MessageMetaData metaData = metaDatas.get(type);
        if (metaData != null)
        {
            exchange = applicationFlavouredExchange(metaData.getExchange());
            if (!StringUtils.isBlank(metaData.getExchangeType()))
            {
                exchangeType = MessageExchangeType.valueOf(metaData.getExchangeType());
            }
            else
            {
                exchangeType = MessageExchangeType.TOPIC;
            }

            if (!StringUtils.isBlank(metaData.getRoutingKey()))
            {
                routingKey = metaData.getRoutingKey();
            }
        }

        // Stereotype
        RequestMessage requestAnnotation = messageClass.getAnnotation(RequestMessage.class);
        if (requestAnnotation != null)
        {
            stereotype = requestAnnotation.stereotype();
        }

        // Stereotype
        ReplyMessage replyAnnotation = messageClass.getAnnotation(ReplyMessage.class);
        if (replyAnnotation != null)
        {
            stereotype = replyAnnotation.stereotype();
        }

        // Stereotype
        EventMessage eventAnnotation = messageClass.getAnnotation(EventMessage.class);
        if (eventAnnotation != null)
        {
            stereotype = eventAnnotation.stereotype();
        }

        MessageDescription<M> description = new MessageDescription<M>();
        description.setExchange(exchange);
        description.setExchangeType(exchangeType);
        description.setMessageClass(messageClass);
        description.setRoutingKey(routingKey);
        description.setStereotype(stereotype);
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
