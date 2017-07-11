/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.utils;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

/**
 * ContainerIdHelper Test.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information.
 * <p/>
 *
 * @since 1.0
 */
public class ContainerIdHelperTest
{
    @Test
    public void testGetContainerId() throws UnknownHostException
    {
        String s1 = System.getProperty("container.id");
        s1 = s1 == null ? InetAddress.getLocalHost().getHostName() : s1;
        
        assertTrue(s1.equals(ContainerIdHelper.getContainerId()));
    }
}
