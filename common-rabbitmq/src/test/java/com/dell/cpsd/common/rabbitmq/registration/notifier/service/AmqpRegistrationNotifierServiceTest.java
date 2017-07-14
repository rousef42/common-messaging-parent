/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.service;

import com.dell.cpsd.common.rabbitmq.TestReplyMessage;
import com.dell.cpsd.common.rabbitmq.processor.PropertiesPostProcessor;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageBinding;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageExchange;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageRegistrationNotified;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageRegistrationWithdrawn;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.BindingDataDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageDirectionType;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageExchangeDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageQueueDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for AmqpRegistrationNotifierService.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since x.y.z
 */
public class AmqpRegistrationNotifierServiceTest
{
    @Mock
    RabbitTemplate template;

    String notificationExchange = "notificationExchange";
    String routingKey = "routingKey";
    ObjectMapper mapper;
    AmqpRegistrationNotifierService service;


    @Before
    public void setUp() throws Exception
    {
        template = Mockito.mock(RabbitTemplate.class);
        mapper = new ObjectMapper();
        service = new AmqpRegistrationNotifierService(mapper, template, notificationExchange, routingKey);
    }

    @Test
    public void transformNotified() throws Exception
    {
        String serviceName = "serviceName";
        Class<?> messageClass = TestReplyMessage.class;
        String messageType = "messageType";
        String messageVersion = "1.0";
        String exchangeName = "exchangeName";
        String queueName = "queeueName";
        MessageDirectionType direction = MessageDirectionType.CONSUME;

        JsonSchema schema = mapper.generateJsonSchema(MessageBinding.class);

        MessageRegistrationDto entry = getMessageRegistrationDto(serviceName, messageClass, messageType, messageVersion,
                exchangeName, queueName, direction, schema);

        MessageRegistrationNotified registration = service.transformNotified(entry);

        assertEquals(serviceName, registration.getServiceName());
        assertEquals(messageType, registration.getMessageType());
        assertEquals(messageVersion, registration.getMessageVersion());
        assertEquals(schema, registration.getSchema());

        List<MessageExchange> actualExchanges =  registration.getMessageExchanges();
        assertEquals(1, actualExchanges.size());
        assertEquals(exchangeName, actualExchanges.get(0).getName());
        assertEquals(direction.name(), actualExchanges.get(0).getDirection());
    }

    @Test
    public void transformWithdrawn() throws Exception
    {
        String serviceName = "serviceName";
        Class<?> messageClass = TestReplyMessage.class;
        String messageType = "messageType";
        String messageVersion = "1.0";
        String exchangeName = "exchangeName";
        String queueName = "queeueName";
        MessageDirectionType direction = MessageDirectionType.CONSUME;

        JsonSchema schema = mapper.generateJsonSchema(MessageBinding.class);

        MessageRegistrationDto entry = getMessageRegistrationDto(serviceName, messageClass, messageType, messageVersion,
                exchangeName, queueName, direction, schema);
        String registrationId = entry.getRegistrationId();

        MessageRegistrationWithdrawn registration = service.transformWithdrawn(entry);

        assertEquals(registrationId, registration.getRegistrationId());

    }

    @Test
    public void transformMessageExchange() throws Exception
    {
        String exchangeName = "exchangeName";
        MessageDirectionType direction = MessageDirectionType.CONSUME;
        MessageExchangeDto exchangeDto = new MessageExchangeDto(exchangeName, direction);

        MessageExchange actualExchange  = service.transformMessageExchange(exchangeDto);

        assertEquals(exchangeName, actualExchange.getName());
        assertEquals(direction.name(), actualExchange.getDirection());
    }

    @Test
    public void transformBindingData() throws Exception
    {
        String queueName = "queueName";
        String routingKey = "routingKey";
        BindingDataDto binding = new BindingDataDto(queueName, routingKey);

        MessageBinding actualBinding = service.transformBindingData(binding);

        assertEquals(queueName, actualBinding.getQueueName());
        assertEquals(routingKey, actualBinding.getRoutingKey());
    }

    @Test
    public void notifyTest() throws Exception
    {
        String serviceName = "serviceName";
        Class<?> messageClass = TestReplyMessage.class;
        String messageType = "messageType";
        String messageVersion = "1.0";
        String exchangeName = "exchangeName";
        String queueName = "queeueName";
        MessageDirectionType direction = MessageDirectionType.CONSUME;

        JsonSchema schema = mapper.generateJsonSchema(MessageBinding.class);

        MessageRegistrationDto entry = getMessageRegistrationDto(serviceName, messageClass, messageType, messageVersion,
                exchangeName, queueName, direction, schema);

        doNothing().when(template).convertAndSend(eq(notificationExchange), eq(routingKey), any(MessageRegistrationNotified.class));

        service .notify(entry);

        verify(template).convertAndSend(eq(notificationExchange), eq(routingKey), any(MessageRegistrationNotified.class));
    }

    @Test
    public void withdrawTest() throws Exception
    {
        String serviceName = "serviceName";
        Class<?> messageClass = TestReplyMessage.class;
        String messageType = "messageType";
        String messageVersion = "1.0";
        String exchangeName = "exchangeName";
        String queueName = "queeueName";
        MessageDirectionType direction = MessageDirectionType.CONSUME;

        JsonSchema schema = mapper.generateJsonSchema(MessageBinding.class);

        MessageRegistrationDto entry = getMessageRegistrationDto(serviceName, messageClass, messageType, messageVersion,
                exchangeName, queueName, direction, schema);

        doNothing().when(template).convertAndSend(eq(notificationExchange), eq(routingKey), any(MessageRegistrationWithdrawn.class));

        service.withdraw(entry);

        verify(template).convertAndSend(eq(notificationExchange), eq(routingKey), any(MessageRegistrationWithdrawn.class));
    }

    private MessageRegistrationDto getMessageRegistrationDto(final String serviceName, final Class<?> messageClass,
            final String messageType, final String messageVersion, final String exchangeName, final String queueName,
            final MessageDirectionType direction, final JsonSchema schema)
    {
        MessageExchangeDto exchangeDto = new MessageExchangeDto(exchangeName, direction);
        List<MessageExchangeDto> messageExchanges = Arrays.asList(exchangeDto);

        MessageQueueDto queueDto = new MessageQueueDto(queueName);
        List<MessageQueueDto> messageQueues = Arrays.asList(queueDto);

        return new MessageRegistrationDto(serviceName, messageClass, messageType, messageVersion, schema, messageExchanges, messageQueues);
    }

}