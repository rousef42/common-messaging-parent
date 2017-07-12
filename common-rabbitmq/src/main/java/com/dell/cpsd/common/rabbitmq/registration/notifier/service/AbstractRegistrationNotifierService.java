/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.service;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageBinding;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageExchange;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageRegistrationNotified;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageRegistrationWithdrawn;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.BindingDataDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageExchangeDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class AbstractRegistrationNotifierService
{
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(AbstractRegistrationNotifierService.class);

    protected MessageRegistrationNotified transformNotified(MessageRegistrationDto entry)
    {
        return new MessageRegistrationNotified(UUID.randomUUID().toString(), Calendar.getInstance().getTime(), entry.getRegistrationId(),
                entry.getServiceName(), entry.getMessageType(), entry.getMessageVersion(), entry.getMessageSchema(),
                entry.getMessageExchanges() == null ?
                        null :
                        entry.getMessageExchanges().stream().map(this::transformMessageExchange).collect(Collectors.toList()));
    }

    protected MessageRegistrationWithdrawn transformWithdrawn(MessageRegistrationDto entry)
    {
        return new MessageRegistrationWithdrawn(UUID.randomUUID().toString(), Calendar.getInstance().getTime(), entry.getRegistrationId());
    }

    protected MessageExchange transformMessageExchange(MessageExchangeDto exchange)
    {
        return new MessageExchange(exchange.getName(), String.valueOf(exchange.getDirection()), exchange.getBindings() == null ?
                null :
                exchange.getBindings().stream().map(this::transformBindingData).collect(Collectors.toList()));
    }

    protected MessageBinding transformBindingData(BindingDataDto binding)
    {
        return new MessageBinding(binding.getQueueName(), binding.getRoutingKey());
    }

    protected void log(ObjectMapper objectMapper, Object object)
    {
        try
        {
            LOGGER.info(objectMapper.writeValueAsString(object));
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error("Unable to log message", e);
        }
    }
}
