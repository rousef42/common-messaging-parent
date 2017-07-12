/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class ApplicationConfiguration
{
    private final String applicationName;
    private final String instanceUuid;
    private final String hostName;

    public ApplicationConfiguration(String applicationName, String instanceUuid, String hostName)
    {
        this.applicationName = applicationName;
        this.instanceUuid = instanceUuid;
        this.hostName = hostName;
    }

    public String getApplicationName()
    {
        return applicationName;
    }

    public String getInstanceUuid()
    {
        return instanceUuid;
    }

    public String getHostName()
    {
        return hostName;
    }
}
