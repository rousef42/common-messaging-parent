/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.log;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.logging.LoggingManager;
import com.dell.cpsd.common.rabbitmq.i18n.RabbitMQMessageBundle;

import java.util.ResourceBundle;

/**
 * This class is a factory for <code>ILogger</code> instances.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @version 1.0
 * @since SINCE-TDB
 */
public class RabbitMQLoggingManager
{
    /*
     * The message resource bundle used for logging messages.
     */
    private static final ResourceBundle MESSAGE_BUNDLE = ResourceBundle.getBundle(RabbitMQMessageBundle.class.getName());

    /*
     * The logging manager for this class
     */
    private static final LoggingManager LOGGING_MANAGER = new LoggingManager();

    /**
     * RabbitMQLoggingManager constructor.
     *
     * @since SINCE-TDB
     */
    public RabbitMQLoggingManager()
    {
        super();
    }

    /**
     * This returns an <code>ILogger</code> using the name of the specified
     * class as the logger name.
     *
     * @param clazz The <code>Class</code> used for the logger.
     * @return The <code>ILogger</code> with the name of the class.
     * @throws IllegalArgumentException Thrown if the class is null.
     * @since SINCE-TDB
     */
    public static ILogger getLogger(Class clazz)
    {
        return LOGGING_MANAGER.getLogger(clazz, MESSAGE_BUNDLE);
    }

    /**
     * This returns an <code>ILogger</code> using the specified name as the
     * logger name.
     *
     * @param name The name of the logger.
     * @return The <code>ILogger</code> with the specified name.
     * @since SINCE-TDB
     */
    public static ILogger getLogger(String name)
    {
        return LOGGING_MANAGER.getLogger(name, MESSAGE_BUNDLE);
    }
}
