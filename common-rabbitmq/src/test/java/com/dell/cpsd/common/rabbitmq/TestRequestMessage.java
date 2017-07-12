/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq;

import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public class TestRequestMessage implements HasMessageProperties<TestMessageProperties>
{
    private TestMessageProperties messageProperties = new TestMessageProperties();

    public TestMessageProperties getMessageProperties()
    {
        return messageProperties;
    }

    public void setMessageProperties(TestMessageProperties messageProperties)
    {
        this.messageProperties = messageProperties;
    }
}
