/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Bean;

import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;

import com.dell.cpsd.common.rabbitmq.connectors.RabbitMQCachingConnectionFactory;

/**
 * This is the production environment-specific RabbitMQ configuration.
 *
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 * 
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
@Configuration
@Profile("production")
public class RabbitMqProductionConfig
{
    /*
     * The configuration properties for the client.
     */
    @Autowired
    @Qualifier("rabbitPropertiesConfig")
    private IRabbitMqPropertiesConfig propertiesConfig;
    
    
    /**
     *
     * @return  The <code>ConnectionFactory</code> to use.
     *  
     * @since   SINCE-TBD
     */
    @Bean
    @Qualifier("rabbitConnectionFactory")
    public ConnectionFactory productionCachingConnectionFactory()
    {
        final com.rabbitmq.client.ConnectionFactory connectionFactory = 
                                    new com.rabbitmq.client.ConnectionFactory();
        
        final RabbitMQCachingConnectionFactory cachingCF = 
                new RabbitMQCachingConnectionFactory(connectionFactory, propertiesConfig);
        
        return cachingCF;
    }
}
