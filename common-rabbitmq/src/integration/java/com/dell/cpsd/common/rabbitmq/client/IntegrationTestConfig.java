/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.ComponentScan.Filter;

import com.dell.cpsd.common.rabbitmq.config.ConsumerConfig;
import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.dell.cpsd.common.rabbitmq.config.PropertiesConfig;
import com.dell.cpsd.common.rabbitmq.config.RabbitConfig;
import com.dell.cpsd.common.rabbitmq.config.RabbitMQPropertiesConfig;
import com.dell.cpsd.common.rabbitmq.config.RabbitMqProductionConfig;
import com.dell.cpsd.common.rabbitmq.connectors.RabbitMQCachingConnectionFactory;
import com.dell.cpsd.common.rabbitmq.connectors.RabbitMQTLSFactoryBean;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;
import com.dell.cpsd.common.rabbitmq.registration.notifier.config.RegistrationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.DefaultSaslConfig;

/**
 * The class contains all the related configurations required to run integration tests
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 */
@Configuration
@ComponentScan(basePackages = {"com.dell.cpsd.common.rabbitmq.client"}, excludeFilters = {
        @Filter(type = FilterType.REGEX, pattern = "com.dell.cpsd.common.rabbitmq.*.*Test.*"),
        @Filter(type = FilterType.REGEX, pattern = "com.dell.cpsd.common.rabbitmq.*.*IT.*")})
@PropertySources({@PropertySource("classpath:rabbitmq.properties")})
@Import({RabbitConfig.class, PropertiesConfig.class,ConsumerConfig.class, RabbitMqProductionConfig.class,RegistrationConfig.class})
public class IntegrationTestConfig
{
    private static final Logger       LOGGER = LoggerFactory.getLogger(RabbitMqProductionConfig.class);

    /*
     * The configuration properties for the client.
     */
    @Autowired
    private IRabbitMqPropertiesConfig propertiesConfig;

    /**
     * @return The <code>ConnectionFactory</code> to use.
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
        } catch (Exception exception)
        {
            Object[] lparams = {exception.getMessage()};
            LOGGER.error(RabbitMQMessageCode.ERROR_RESPONSE_UNEXPECTED_ERROR_E.getMessageCode(), lparams, exception);
        }
        return cachingCF;
    }

    /**
     * create bean for ObjectMapper
     * 
     * @return {@link ObjectMapper}
     */
    @Bean
    public ObjectMapper objectMapper()
    {
        return new ObjectMapper();
    }
}
