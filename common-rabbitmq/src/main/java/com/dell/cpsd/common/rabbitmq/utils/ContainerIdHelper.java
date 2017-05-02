/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This is a helper class that returns the container id or the host name.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @since 1.0
 */
public class ContainerIdHelper
{
    /*
     * The container identifier
     */
    private static final String CONTAINER_ID = "container.id";

    /**
     * This returns the container id or host name if the container id is not
     * set.
     *
     * @return The container id or host name.
     * @since 1.0
     */
    public static String getContainerId()
    {
        String containerId = System.getProperty(CONTAINER_ID);

        if (containerId == null)
        {
            try
            {
                containerId = InetAddress.getLocalHost().getHostName();
            }
            catch (UnknownHostException e)
            {
                throw new RuntimeException("Failed to resolve the container id or host name.", e);
            }
        }

        return containerId;
    }
}
