/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.env.Environment;

/**
 * Configuration for common RabbitMQ properties.
 *
 * <p/>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>

 * @since   SINCE-TBD
 */
@Configuration
public class RabbitMQPropertiesConfig implements IRabbitMqPropertiesConfig
{

    /**
     * The Environment.
     */
    @Autowired
    protected Environment environment;
    
    /**
     * RabbitMQPropertiesConfig constructor.
     * 
     * @since   SINCE-TBD
     */
    public RabbitMQPropertiesConfig()
    {
        super();
    }

    
    /**
     * This returns the rabbit host name. The name of the property is 
     * <code>remote.dell.amqp.rabbitHostname</code>.
     * 
     * @return  The rabbit host name.
     * 
     * @since   SINCE-TBD
     */
    @Bean
    public String rabbitHostname()
    {
        return environment.getRequiredProperty("remote.dell.amqp.rabbitHostname");
    }
    
    
    /**
     * This returns the secondary host names. The name of the property is
     * <code>remote.dell.secondaries.amqp.rabbitHostname</code>.
     * 
     * @return  The secondary rabbit host names.
     * 
     * @since   SINCE-TBD
     */
    @Bean 
    public String secondaryHostnames()
    {
        return environment.getProperty("remote.dell.secondaries.amqp.rabbitHostname", "");
    }

    
    /**
     * This returns the rabbit amqp port number. The name of the property is
     * <code>remote.dell.amqp.rabbitPort</code>.
     * 
     * @return  The rabbit port number.
     * 
     * @since   SINCE-TBD
     */
    @Bean
    public Integer rabbitPort()
    {
        return environment.getProperty("remote.dell.amqp.rabbitPort", Integer.class, 5672);
    }

    
    /**
     * This returns the rabbit broker password. The name of the property is
     * <code>remote.dell.amqp.rabbitPassword</code>.
     * 
     * @return  The rabbit broker password.
     * 
     * @since   SINCE-TBD
     */
    @Bean
    public String rabbitPassword()
    {
        return environment.getProperty("remote.dell.amqp.rabbitPassword", "");
    }
    
    
    /**
     * This returns the rabbit user name. The name of the property is
     * <code>remote.dell.amqp.rabbitUsername</code>.
     * 
     * @return  The rabbit user name.
     * 
     * @since   SINCE-TBD
     */
    @Bean
    public String rabbitUsername()
    {
        return environment.getProperty("remote.dell.amqp.rabbitUsername", "");
    }

    
    /**
     * This returns the rabbit virtual host name. The name of the property is
     * <code>remote.dell.amqp.rabbitVirtualHost</code>.
     * 
     * @return  The rabbit virtual hostname.
     * 
     * @since   SINCE-TBD
     */
    @Bean
    public String rabbitVirtualHost()
    {
        return environment.getRequiredProperty("remote.dell.amqp.rabbitVirtualHost");
    }

    
    /**
     * This returns the rabbit heartbeat interval. The name of the property is
     * <code>remote.dell.amqp.rabbitRequestedHeartbeat</code>.
     * 
     * @return  The rabbit heartbeat interval.
     * 
     * @since   SINCE-TBD
     */
    @Bean
    public Integer rabbitRequestedHeartbeat()
    {
        return environment.getProperty("remote.dell.amqp.rabbitRequestedHeartbeat", Integer.class, 0);
    }


    /**
     * This returns the name of the data center. The name of the property is 
     * <code>data.center</code>.
     * 
     * @return  The name of the data center.
     * 
     * @since   SINCE-TBD
     */
    @Bean
    public String dataCenter()
    {
        return environment.getRequiredProperty("data.center");
    }

    @Bean
    public String applicationName()
    {
        return environment.getProperty("application.name", "");
    }

    @Bean
    public String trustStorePassphrase() {
        return null;
    }

    @Bean
    public String keyStorePassPhrase() {
        return null;
    }

    @Bean
    public String keyStorePath() {
        return null;
    }
    @Bean
    public String trustStorePath() {
        return null;
    }


    @Bean
    public String tlsVersion() {
        return null;
    }

    @Bean
    public Boolean isSslEnabled() {
        return null;
    }
}
