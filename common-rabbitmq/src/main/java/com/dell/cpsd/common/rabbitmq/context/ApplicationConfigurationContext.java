/**
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

/**
 * <p>
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class ApplicationConfigurationContext
{
    private static final ThreadLocal<ApplicationConfiguration> CURRENT = new ThreadLocal<>();

    public static void setCurrent(String applicationName)
    {
        ApplicationConfiguration applicationConfiguration =
                ApplicationConfigurationFactory.getInstance().createApplicationConfiguration(applicationName);
        setCurrent(applicationConfiguration);
    }

    public static void setCurrent(ApplicationConfiguration configuration)
    {
        CURRENT.set(configuration);
    }

    public static ApplicationConfiguration getCurrent()
    {
        return CURRENT.get();
    }
}
