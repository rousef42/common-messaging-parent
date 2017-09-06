/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.config;

/**
 * Configuration for common RabbitMQ properties.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
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
     * @return The rabbit host name.
     * @since SINCE-TBD
     */
    String rabbitHostname();

    /**
     * This returns the secondary host names. The name of the property is
     * <code>remote.dell.secondaries.amqp.rabbitHostname</code>.
     *
     * @return The secondary rabbit host names.
     * @since SINCE-TBD
     */
    String secondaryHostnames();

    /**
     * This returns the rabbit amqp port number. The name of the property is
     * <code>remote.dell.amqp.rabbitPort</code>.
     *
     * @return The rabbit port number.
     * @since SINCE-TBD
     */
    Integer rabbitPort();

    /**
     * This returns the rabbit broker password. The name of the property is
     * <code>remote.dell.amqp.rabbitPassword</code>.
     *
     * @return The rabbit broker password.
     * @since SINCE-TBD
     */
    String rabbitPassword();

    /**
     * This returns the rabbit user name. The name of the property is
     * <code>remote.dell.amqp.rabbitUsername</code>.
     *
     * @return The rabbit user name.
     * @since SINCE-TBD
     */
    String rabbitUsername();

    /**
     * This returns the rabbit virtual host name. The name of the property is
     * <code>remote.dell.amqp.rabbitVirtualHost</code>.
     *
     * @return The rabbit virtual hostname.
     * @since SINCE-TBD
     */
    String rabbitVirtualHost();

    /**
     * This returns the rabbit heartbeat interval. The name of the property is
     * <code>remote.dell.amqp.rabbitRequestedHeartbeat</code>.
     *
     * @return The rabbit heartbeat interval.
     * @since SINCE-TBD
     */
    Integer rabbitRequestedHeartbeat();

    /**
     * This returns the name of the data center. The name of the property is
     * <code>data.center</code>.
     *
     * @return The name of the data center.
     * @since SINCE-TBD
     */
    String dataCenter();

    /**
     * This returns the name of the service or application name. The name of
     * the property is <code>application.name</code>
     *
     * @return The Application/Service Name
     * @since SINCE-TBD
     */
    String applicationName();

    /**
     * This returns the pass phrase of the trustStore. The name of
     * the property is <code>trustStorePassphrase</code>
     *
     * @return The trustStorePassphrase
     * @Deprecated
     */
    @Deprecated
    String trustStorePassphrase();

    /**
     * This returns the passPhrase of the keyStore. The name of
     * the property is <code>keyStorePassPhrase</code>
     *
     * @return The keyStorePassPhrase
     * @Deprecated
     */
    @Deprecated
    String keyStorePassPhrase();

    /**
     * This returns the path of the keyStore. The name of
     * the property is <code>keyStorePath</code>
     *
     * @return The keyStorePath
     * @Deprecated
     */
    @Deprecated
    String keyStorePath();

    /**
     * This returns the path of the trustStore. The name of
     * the property is <code>trustStorePath</code>
     *
     * @return The trustStorePath
     * @Deprecated
     */
    @Deprecated
    String trustStorePath();

    /**
     * This returns the version of the tls. The name of
     * the property is <code>tlsVersion</code>
     *
     * @return The tlsVersion
     */
    String tlsVersion();

    /**
     * This returns the status of SSL. The name of
     * the property is <code>isSslEnabled</code>
     *
     * @return The isSslEnabled
     */
    Boolean isSslEnabled();
}
