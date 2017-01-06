/**
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * <p>
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class ApplicationConfigurationFactory
{
    private final String instanceUuid;
    private final String hostName;

    private static ApplicationConfigurationFactory INSTANCE = null;

    private ApplicationConfigurationFactory()
    {
        this.hostName = resolveHostName();
        this.instanceUuid = UUID.randomUUID().toString();
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

    public static ApplicationConfigurationFactory getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new ApplicationConfigurationFactory();
        }
        return INSTANCE;
    }
}
