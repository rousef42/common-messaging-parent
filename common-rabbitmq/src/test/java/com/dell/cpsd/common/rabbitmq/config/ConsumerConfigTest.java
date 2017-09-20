/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.config;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

import com.dell.cpsd.common.logging.LoggingManager;

/**
 * The class contains the Junit test cases for ConsumerConfig.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 */
@RunWith(MockitoJUnitRunner.class)
public class ConsumerConfigTest
{
    @InjectMocks
    ConsumerConfig classUnderTest;

    @Mock
    private Queue  responseQueue;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception
    {
        classUnderTest = null;
    }

    /**
     * Test SimpleMessageListenerContainer bean creation config
     */
    @Test
    public void testSimpleMessageListenerContainer()
    {
        SimpleMessageListenerContainer simpleMessageListenerContainer = classUnderTest.simpleMessageListenerContainer();
        assertNotNull(simpleMessageListenerContainer);
    }

    /**
     * Test LoggingManager bean creation config
     */
    @Test
    public void testLoggingManager()
    {
        LoggingManager loggingManager = classUnderTest.loggingManager();
        assertNotNull(loggingManager);
    }

    /**
     * Test RabbitListenerContainerFactory bean creation config
     */
    @Test
    public void testRabbitListenerContainerFactory()
    {
        RabbitListenerContainerFactory<SimpleMessageListenerContainer> rabbitListenerContainerFactory = classUnderTest
                .simpleRabbitListenerContainerFactory();
        assertNotNull(rabbitListenerContainerFactory);
    }

    /**
     * Test HandlerRegistrar bean creation config
     */
    @Test
    public void testHandlerRegistrar()
    {
        HandlerRegistrar handlerRegistrar = classUnderTest.handlerRegistrar();
        assertNotNull(handlerRegistrar);
    }

}
