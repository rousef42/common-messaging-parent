/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.connectors;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.exceptions.RabbitMQConnectionException;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionListener;

import javax.annotation.PostConstruct;

/**
 * This class is a connection factory extending <code>CachingConnectionFactory
 * </code> to facilitate the connection to RabbitMQ available in the environment.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @version 1.0
 * @since SINCE-TDB
 */
public final class RabbitMQConnectionFactory extends CachingConnectionFactory
{
    // TODO : consolidate with prepositioning common library 

    /*
     * Logger for this class.
     */
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(RabbitMQConnectionFactory.class);

    /*
     * RabbitMQ host used for the connection. 
     * */
    private String host;

    /*
     * RabbitMQ password. 
     */
    private String password;

    /*
     * RabbitMQ username. 
     */
    private String username;

    /*
     * RabbitMQ port. Default value 5672. 
     */
    private int port;

    /*
     * RabbitMQ virtual host.
     */
    private String virtualHost;

    /*
     * RabbitMQ heartbeat interval.
     */
    private Integer heartbeat;

    /**
     * RabbitMQConnectionFactory constructor.
     *
     * @since SINCE-TBD
     */
    public RabbitMQConnectionFactory()
    {
        super(new ConnectionFactory());
    }

    /**
     * RabbitMQConnectionFactory constructor.
     *
     * @param host        RabbitMQ host used for the connection.
     * @param password    RabbitMQ password.
     * @param port        RabbitMQ port.
     * @param virtualHost RabbitMQ virtual host.
     * @param username    RabbitMQ username.
     * @since SINCE-TBD
     */
    public RabbitMQConnectionFactory(String host, String password, int port, String virtualHost, String username)
    {
        super(new ConnectionFactory());

        this.host = host;
        this.password = password;
        this.port = port;
        this.virtualHost = virtualHost;
        this.username = username;

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
     * Initializes the connection factory object's host, port and credentials
     *
     * @throws RabbitMQConnectionException
     * @since SINCE-TBD
     */
    @PostConstruct
    protected void init() throws RabbitMQConnectionException
    {
        Object[] lparams = {this.host, "" + this.port};
        LOGGER.info(RabbitMQMessageCode.CONNECTION_FACTORY_INIT_I.getMessageCode(), lparams);

        setHost(this.host);
        setPort(this.port);
        setUsername(this.username);
        setPassword(this.password);
        setVirtualHost(this.virtualHost);
        setRequestedHeartBeat(this.heartbeat);

        addConnectionListener(new ConnectionListener()
        {
            @Override
            public void onCreate(Connection connection)
            {
                if (LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("Created AMQP connection");
                }
            }

            @Override
            public void onClose(Connection connection)
            {
                if (LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("Closing AMQP connection");
                }
            }
        });
    }
}
