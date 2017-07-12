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
public class ApplicationConfigurationContext
{
    private static final ThreadLocal<ApplicationConfiguration> CURRENT = new ThreadLocal<>();

    public static void setCurrent(String applicationName)
    {
        ApplicationConfiguration applicationConfiguration = ApplicationConfigurationFactory.getInstance()
                .createApplicationConfiguration(applicationName);
        setCurrent(applicationConfiguration);
    }

    public static ApplicationConfiguration getCurrent()
    {
        return CURRENT.get();
    }

    public static void setCurrent(ApplicationConfiguration configuration)
    {
        CURRENT.set(configuration);
    }
}
