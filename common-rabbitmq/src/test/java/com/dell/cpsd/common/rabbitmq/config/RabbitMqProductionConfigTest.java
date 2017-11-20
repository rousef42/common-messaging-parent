/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

/**
 * The class contains the Junit test cases for RabbitMqProductionConfig.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 */
@RunWith(MockitoJUnitRunner.class)
public class RabbitMqProductionConfigTest
{
    @InjectMocks
    private RabbitMqProductionConfig              classUnderTest;
    
    @Mock
    private PropertiesConfig propertiesConfig;
    
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

    @Test
    public void testProductionCachingConnectionFactory(){
        Mockito.when(propertiesConfig.isSslEnabled()).thenReturn(false);
        ConnectionFactory connectionFactory =  classUnderTest.productionCachingConnectionFactory();
        assertNotNull(connectionFactory);
    }
    @Test
    public void testProductionCachingConnectionFactoryIsNullWhenError(){
        Mockito.when(propertiesConfig.isSslEnabled()).thenThrow(RuntimeException.class);
        ConnectionFactory connectionFactory =  classUnderTest.productionCachingConnectionFactory();
        assertNull(connectionFactory);
    }
}
