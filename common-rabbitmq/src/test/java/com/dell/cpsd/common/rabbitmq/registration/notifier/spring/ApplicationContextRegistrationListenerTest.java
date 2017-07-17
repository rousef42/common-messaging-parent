/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.spring;

import com.dell.cpsd.common.rabbitmq.TestReplyMessage;
import com.dell.cpsd.common.rabbitmq.registration.MessageRegistrationAware;
import com.dell.cpsd.common.rabbitmq.registration.notifier.message.MessageBinding;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageDirectionType;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageExchangeDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageQueueDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.model.MessageRegistrationDto;
import com.dell.cpsd.common.rabbitmq.registration.notifier.service.RegistrationNotifierService;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.StaticApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * Unit tests for ApplicationContextRegistrationListener.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since x.y.z
 */
public class ApplicationContextRegistrationListenerTest
{
    private StaticApplicationContext context;

    @Mock
    private RegistrationNotifierService registrationService;

    @Mock
    private MessageRegistrationAware registration;

    @Before
    public void setUp() throws Exception
    {
        registrationService = mock(RegistrationNotifierService.class);
        registration = mock(MessageRegistrationAware.class);

        context = new StaticApplicationContext();
        context.getBeanFactory().registerSingleton("registrationService", registrationService);
        context.getBeanFactory().registerSingleton("registration", registration);

        context.refresh();
    }

    @Test
    public void onApplicationEventNoAutoRegister() throws Exception
    {
        reset(registration);
        reset(registrationService);

        when(registration.isAutoRegister()).thenReturn(false);

        ContextRefreshedEvent event = new ContextRefreshedEvent(context);
        ApplicationContextRegistrationListener listener = new ApplicationContextRegistrationListener();
        listener.onApplicationEvent(event);

        verify(registration).isAutoRegister();
        verify(registration, never()).getRegistrations();
    }

    @Test
    public void onApplicationEventRefresh() throws Exception
    {
        reset(registration);
        reset(registrationService);

        String serviceName = "serviceName";
        Class<?> messageClass = TestReplyMessage.class;
        String messageType = "messageType";
        String messageVersion = "1.0";
        String exchangeName = "exchangeName";
        String queueName = "queeueName";
        MessageDirectionType direction = MessageDirectionType.CONSUME;
        MessageRegistrationDto entry = getMessageRegistrationDto(serviceName, messageClass, messageType, messageVersion, exchangeName,
                queueName, direction);

        when(registration.isAutoRegister()).thenReturn(true);
        when(registration.getRegistrations()).thenReturn(Arrays.asList(entry));

        ArgumentCaptor<MessageRegistrationDto> empEventArgCaptor = ArgumentCaptor.forClass(MessageRegistrationDto.class);
        doNothing().when(registrationService).notify(empEventArgCaptor.capture());

        ContextRefreshedEvent event = new ContextRefreshedEvent(context);
        ApplicationContextRegistrationListener listener = new ApplicationContextRegistrationListener();
        listener.onApplicationEvent(event);

        verify(registration).isAutoRegister();
        verify(registration).getRegistrations();

        assertEquals(serviceName, empEventArgCaptor.getValue().getServiceName());
        assertEquals(messageClass, empEventArgCaptor.getValue().getMessageClass());
        assertEquals(messageType, empEventArgCaptor.getValue().getMessageType());

        List<MessageExchangeDto> actualExcanges = empEventArgCaptor.getValue().getMessageExchanges();
        assertEquals(1, actualExcanges.size());
        assertEquals(exchangeName, actualExcanges.get(0).getName());

        List<MessageQueueDto> actualQueues = empEventArgCaptor.getValue().getMessageQueues();
        assertEquals(1, actualQueues.size());
        assertEquals(queueName, actualQueues.get(0).getName());
    }

    @Test
    public void onApplicationEventWithdraw() throws Exception
    {
        reset(registration);
        reset(registrationService);

        String serviceName = "serviceName";
        Class<?> messageClass = TestReplyMessage.class;
        String messageType = "messageType";
        String messageVersion = "1.0";
        String exchangeName = "exchangeName";
        String queueName = "queeueName";
        MessageDirectionType direction = MessageDirectionType.CONSUME;
        MessageRegistrationDto entry = getMessageRegistrationDto(serviceName, messageClass, messageType, messageVersion, exchangeName,
                queueName, direction);

        when(registration.isAutoRegister()).thenReturn(true);
        when(registration.getRegistrations()).thenReturn(Arrays.asList(entry));

        doNothing().when(registrationService).notify(any(MessageRegistrationDto.class));

        // Have to send a ContextRefreshedEvent to add a MessageRegistrationDto so the withdraw will have something in registrations to work with
        ApplicationContextRegistrationListener listener = new ApplicationContextRegistrationListener();
        ContextRefreshedEvent setUpEvent = new ContextRefreshedEvent(context);
        listener.onApplicationEvent(setUpEvent);

        // Now send the ContextClosedEvent to test withdraw
        ArgumentCaptor<MessageRegistrationDto> empEventArgCaptor = ArgumentCaptor.forClass(MessageRegistrationDto.class);
        doNothing().when(registrationService).withdraw(empEventArgCaptor.capture());

        ContextClosedEvent event = new ContextClosedEvent(context);
        listener.onApplicationEvent(event);

        verify(registrationService).withdraw(any(MessageRegistrationDto.class));

        assertEquals(serviceName, empEventArgCaptor.getValue().getServiceName());
        assertEquals(messageClass, empEventArgCaptor.getValue().getMessageClass());
        assertEquals(messageType, empEventArgCaptor.getValue().getMessageType());

        List<MessageExchangeDto> actualExcanges = empEventArgCaptor.getValue().getMessageExchanges();
        assertEquals(1, actualExcanges.size());
        assertEquals(exchangeName, actualExcanges.get(0).getName());

        List<MessageQueueDto> actualQueues = empEventArgCaptor.getValue().getMessageQueues();
        assertEquals(1, actualQueues.size());
        assertEquals(queueName, actualQueues.get(0).getName());
    }

    private MessageRegistrationDto getMessageRegistrationDto(final String serviceName, final Class<?> messageClass,
            final String messageType, final String messageVersion, final String exchangeName, final String queueName,
            final MessageDirectionType direction) throws JsonMappingException
    {
        ObjectMapper  mapper = new ObjectMapper();
        JsonSchema schema = mapper.generateJsonSchema(MessageBinding.class);
        MessageExchangeDto exchangeDto = new MessageExchangeDto(exchangeName, direction);
        List<MessageExchangeDto> messageExchanges = Arrays.asList(exchangeDto);

        MessageQueueDto queueDto = new MessageQueueDto(queueName);
        List<MessageQueueDto> messageQueues = Arrays.asList(queueDto);

        return new MessageRegistrationDto(serviceName, messageClass, messageType, messageVersion, schema, messageExchanges, messageQueues);
    }
}