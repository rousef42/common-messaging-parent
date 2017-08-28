/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @author Connor Goulding
 */
public class MessageMetaDataReaderTest
{
    @Test
    public void test()
    {
        
        File  file = new File(ClassLoader.getSystemClassLoader().getResource("./api-schema/messaging.json").getFile());
        System.out.println("Testing Test Case ==================================> "+file.getPath());
        
        MessageMetaDataReader reader = new MessageMetaDataReader();
        Collection<MessageMetaData> metaDatas = reader
                .read(file);
        Assert.assertEquals(2, metaDatas.size());
    }
}
