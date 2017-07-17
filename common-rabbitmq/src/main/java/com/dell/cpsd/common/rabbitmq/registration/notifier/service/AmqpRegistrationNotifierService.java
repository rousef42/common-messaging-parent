/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.service;

import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageRegistrationNotified;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageRegistrationWithdrawn;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class AmqpRegistrationNotifierService extends AbstractRegistrationNotifierService implements RegistrationNotifierService
{
    private RabbitTemplate template;
    private String         notificationExchange;
    private String         routingKey;
    private ObjectMapper   objectMapper;

    public AmqpRegistrationNotifierService(ObjectMapper objectMapper, RabbitTemplate template, String notificationExchange,
            String routingKey)
    {
        this.objectMapper = objectMapper;
        this.template = template;
        this.notificationExchange = notificationExchange;
        this.routingKey = routingKey;
    }

    @Override
    public void notify(MessageRegistrationDto entry)
    {
        MessageRegistrationNotified notified = transformNotified(entry);
        template.convertAndSend(notificationExchange, routingKey, notified);
        log(objectMapper, notified);
    }

    @Override
    public void withdraw(MessageRegistrationDto entry)
    {
        MessageRegistrationWithdrawn withdrawn = transformWithdrawn(entry);
        template.convertAndSend(notificationExchange, routingKey, withdrawn);
        log(objectMapper, withdrawn);
    }

}