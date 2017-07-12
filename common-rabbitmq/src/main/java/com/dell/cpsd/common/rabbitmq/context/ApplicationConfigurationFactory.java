/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

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
public class ApplicationConfigurationFactory
{
    private static ApplicationConfigurationFactory INSTANCE = null;
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
            e.printStackTrace();
        }
        return null;
    }
}
