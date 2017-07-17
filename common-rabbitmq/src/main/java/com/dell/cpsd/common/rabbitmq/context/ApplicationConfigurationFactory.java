/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public final class ApplicationConfigurationFactory
{
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(ApplicationConfigurationFactory.class);
    private static       ApplicationConfigurationFactory INSTANCE = null;
    private final String instanceUuid;
    private final String hostName;

    private ApplicationConfigurationFactory()
    {
        this.hostName = resolveHostName();
        this.instanceUuid = UUID.randomUUID().toString();
    }

    public static ApplicationConfigurationFactory getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ApplicationConfigurationFactory();
        }
        return INSTANCE;
    }

    public ApplicationConfiguration createApplicationConfiguration(String applicationName)
    {
        return new ApplicationConfiguration(applicationName, instanceUuid, hostName);
    }

    private String resolveHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
