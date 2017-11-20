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
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.retry.support.RetryTemplate;

/**
 * The class contains the Junit test cases for RabbitConfig.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 */
@RunWith(MockitoJUnitRunner.class)
public class RabbitConfigTest
{
    @InjectMocks
    private RabbitConfig              classUnderTest;
    
    @Mock
    private PropertiesConfig propertiesConfig;
    
    @Mock
    private ConnectionFactory connectionFactory;

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
     * Test rabbitTemplate bean creation config
     */
    @Test
    public void testRabbitTemplate()
    {
        RabbitTemplate rabbitTemplate = classUnderTest.rabbitTemplate();
        assertNotNull(rabbitTemplate);
    }

    /**
     * Test retryTemplate bean creation config
     */
    @Test
    public void testRetryTemplate()
    {
        RetryTemplate retryTemplate = classUnderTest.retryTemplate();
        assertNotNull(retryTemplate);
    }

    /**
     * Test messageConverter bean creation config
     */
    @Test
    public void testMessageConverter()
    {
        MessageConverter messageConverter = classUnderTest.messageConverter();
        assertNotNull(messageConverter);
    }

    /**
     * Test amqpAdmin bean creation config
     */
    @Test
    public void testAmqpAdmin()
    {
        AmqpAdmin amqpAdmin = classUnderTest.amqpAdmin();
        assertNotNull(amqpAdmin);
    }
    
    /**
     * Test classMapper bean creation config
     */
    @Test
    public void testClassMapperOfMessageAnnotation()
    {
        DefaultClassMapper classMapper = classUnderTest.classMapperOfMessageAnnotation();
        assertNotNull(classMapper);
    }
}
