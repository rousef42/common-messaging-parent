/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

/**
 * @author Connor Goulding
 */
public class MessageMetaDataReaderTest
{
    @Test
    public void test()
    {
        MessageMetaDataReader reader = new MessageMetaDataReader();
        Collection<MessageMetaData> metaDatas = reader
                .read(new File(ClassLoader.getSystemClassLoader().getResource("api-schema/messaging.json").getFile()));
        Assert.assertEquals(2, metaDatas.size());
    }
}
