/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.config;

import com.dell.cpsd.common.rabbitmq.connectors.RabbitMQCachingConnectionFactory;
import com.dell.cpsd.common.rabbitmq.connectors.RabbitMQTLSFactoryBean;
import com.rabbitmq.client.DefaultSaslConfig;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * This is the production environment-specific RabbitMQ configuration.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since SINCE-TBD
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
     * @return The <code>ConnectionFactory</code> to use.
     * @since SINCE-TBD
     */
    @Bean
    @Qualifier("rabbitConnectionFactory")
    public ConnectionFactory productionCachingConnectionFactory()
    {
    	RabbitMQCachingConnectionFactory cachingCF = null;
        com.rabbitmq.client.ConnectionFactory connectionFactory;
        try {
            if (propertiesConfig.isSslEnabled()) {
                RabbitMQTLSFactoryBean rabbitMQTLSFactoryBean = new RabbitMQTLSFactoryBean(propertiesConfig);
                connectionFactory = rabbitMQTLSFactoryBean.getObject();
                cachingCF = new RabbitMQCachingConnectionFactory(connectionFactory, propertiesConfig);
                cachingCF.getRabbitConnectionFactory().setSaslConfig(DefaultSaslConfig.EXTERNAL);
            } else {
                connectionFactory = new com.rabbitmq.client.ConnectionFactory();
                cachingCF = new RabbitMQCachingConnectionFactory(connectionFactory, propertiesConfig);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return cachingCF;
    }
}