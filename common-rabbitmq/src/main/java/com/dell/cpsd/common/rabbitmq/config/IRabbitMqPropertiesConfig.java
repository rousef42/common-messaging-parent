/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.config;

/**
 * Configuration for common RabbitMQ properties.
 *
 * <p/>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>

 * @since   SINCE-TBD
 */
public interface IRabbitMqPropertiesConfig
{

    /**
     * The Routing Key Separator for all the services [Pipe].
     */
    String ROUTING_KEY_SEPARATOR = "|";

    /**
     * This returns the rabbit host name. The name of the property is 
     * <code>remote.dell.amqp.rabbitHostname</code>.
     * 
     * @return  The rabbit host name.
     * 
     * @since   SINCE-TBD
     */
    public String rabbitHostname();
    
    
    /**
     * This returns the secondary host names. The name of the property is
     * <code>remote.dell.secondaries.amqp.rabbitHostname</code>.
     * 
     * @return  The secondary rabbit host names.
     * 
     * @since   SINCE-TBD
     */
    public String secondaryHostnames();

    
    /**
     * This returns the rabbit amqp port number. The name of the property is
     * <code>remote.dell.amqp.rabbitPort</code>.
     * 
     * @return  The rabbit port number.
     * 
     * @since   SINCE-TBD
     */
    public Integer rabbitPort();

    
    /**
     * This returns the rabbit broker password. The name of the property is
     * <code>remote.dell.amqp.rabbitPassword</code>.
     * 
     * @return  The rabbit broker password.
     * 
     * @since   SINCE-TBD
     */
    public String rabbitPassword();
    
    
    /**
     * This returns the rabbit user name. The name of the property is
     * <code>remote.dell.amqp.rabbitUsername</code>.
     * 
     * @return  The rabbit user name.
     * 
     * @since   SINCE-TBD
     */
    public String rabbitUsername();

    
    /**
     * This returns the rabbit virtual host name. The name of the property is
     * <code>remote.dell.amqp.rabbitVirtualHost</code>.
     * 
     * @return  The rabbit virtual hostname.
     * 
     * @since   SINCE-TBD
     */
    public String rabbitVirtualHost();

    
    /**
     * This returns the rabbit heartbeat interval. The name of the property is
     * <code>remote.dell.amqp.rabbitRequestedHeartbeat</code>.
     * 
     * @return  The rabbit heartbeat interval.
     * 
     * @since   SINCE-TBD
     */
    public Integer rabbitRequestedHeartbeat();


    /**
     * This returns the name of the data center. The name of the property is 
     * <code>data.center</code>.
     * 
     * @return  The name of the data center.
     * 
     * @since   SINCE-TBD
     */
    public String dataCenter();

    /**
     * This returns the name of the service or application name. The name of
     * the property is <code>application.name</code>
     * @return The Application/Service Name
     *
     * @since   SINCE-TBD
     */
    String applicationName();

    /**
     * This returns the name of the passphrase. The name of
     * the property is <code>passphrase</code>
     * @return The passphrase
     *
     * @since   SINCE-TBD
     */
    String trustStorePassphrase();

    /**
     * This returns the name of the keyPassPhrase. The name of
     * the property is <code>keyPassPhrase</code>
     * @return The passphrase
     *
     * @since   SINCE-TBD
     */
    String keyStorePassPhrase();


    String keyStorePath();


    String trustStorePath();


    String tlsVersion();


    Boolean isSslEnabled();

}
