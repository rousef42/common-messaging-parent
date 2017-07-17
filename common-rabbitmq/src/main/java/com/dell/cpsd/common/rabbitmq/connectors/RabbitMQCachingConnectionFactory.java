/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.connectors;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.config.IRabbitMqPropertiesConfig;
import com.dell.cpsd.common.rabbitmq.config.RabbitMQPropertiesConfig;
import com.dell.cpsd.common.rabbitmq.exceptions.RabbitMQConnectionException;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;

import javax.annotation.PostConstruct;

/**
 * This class is a helper class to support the establishment of a failover
 * caching connection factory.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since SINCE-TDB
 */
public final class RabbitMQCachingConnectionFactory extends CachingConnectionFactory
{
    // TODO : consolidate with prepositioning common library

    /*
     * Logger for this class.
     */
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(RabbitMQCachingConnectionFactory.class);

    /*
     * The primary RabbitMQ host used for the connection.
     */
    private String primaryHost;

    /*
     * The secondary RabbitMQ hosts used for the connection.
     */
    private String secondaryAddresses;

    /*
     * The RabbitMQ password.
     */
    private String password;

    /*
     * The RabbitMQ username.
     */
    private String username;

    /*
     * The RabbitMQ port. Default value 5672.
     */
    private Integer port;

    /*
     * The RabbitMQ virtual host.
     */
    private String virtualHost;

    /*
     * The RabbitMQ heartbeat interval
     */
    private Integer heartbeat;

    /**
     * RabbitMQCachingConnectionFactory constructor.
     *
     * @param connectionFactory The RabbitMQ connection factory.
     * @since SINCE-TDB
     */
    public RabbitMQCachingConnectionFactory(ConnectionFactory connectionFactory)
    {
        super(connectionFactory);
    }

    /**
     * RabbitMQCachingConnectionFactory constructor.
     *
     * @param connectionFactory The RabbitMQ connection factory.
     * @param configuration     The configuration to use.
     * @since SINCE-TDB
     */
    public RabbitMQCachingConnectionFactory(ConnectionFactory connectionFactory, RabbitMQPropertiesConfig configuration)
    {
        this(connectionFactory, ((IRabbitMqPropertiesConfig) configuration));
    }

    /**
     * RabbitMQCachingConnectionFactory constructor.
     *
     * @param connectionFactory The RabbitMQ connection factory.
     * @param configuration     The configuration to use.
     * @since SINCE-TDB
     */
    public RabbitMQCachingConnectionFactory(ConnectionFactory connectionFactory, IRabbitMqPropertiesConfig configuration)
    {
        super(connectionFactory);

        this.primaryHost = configuration.rabbitHostname();
        this.password = configuration.rabbitPassword();
        this.username = configuration.rabbitUsername();
        this.port = configuration.rabbitPort();
        this.virtualHost = configuration.rabbitVirtualHost();
        this.secondaryAddresses = configuration.secondaryHostnames();
        this.heartbeat = configuration.rabbitRequestedHeartbeat();

        try
        {
            this.init();
        }
        catch (RabbitMQConnectionException exception)
        {
            Object[] lparams = {exception.getMessage()};
            LOGGER.error(RabbitMQMessageCode.CONNECTION_FACTORY_INIT_E.getMessageCode(), lparams, exception);
        }
    }

    /**
     * Initializes the connection factory object's host, port and credentials.
     *
     * @since SINCE-TBD
     */
    @PostConstruct
    protected void init() throws RabbitMQConnectionException
    {
        Object[] lparams = {this.primaryHost, "" + this.port};
        LOGGER.info(RabbitMQMessageCode.CONNECTION_FACTORY_INIT_I.getMessageCode(), lparams);

        String primaryAddress = this.primaryHost + ":" + this.port;
        String addresses = (secondaryAddresses != null) ? primaryAddress + "," + secondaryAddresses : primaryAddress;

        setAddresses(addresses);
        setPassword(this.password);
        setUsername(this.username);
        setVirtualHost(this.virtualHost);
        setRequestedHeartBeat(this.heartbeat);

        addConnectionListener(new ConnectionListener()
        {
            @Override
            public void onCreate(Connection connection)
            {
                if (LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("Creating AMQP connection factory");
                }
            }

            @Override
            public void onClose(Connection connection)
            {
                if (LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("Closing AMQP connection factory");
                }
            }
        });
    }
}
