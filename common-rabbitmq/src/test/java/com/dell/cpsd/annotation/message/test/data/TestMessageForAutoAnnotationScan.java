/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.annotation.message.test.data;

import com.dell.cpsd.common.rabbitmq.TestMessageProperties;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.contract.extension.amqp.annotation.Message;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
@Message(value = "test.message.request", version = "1.0")
public class TestMessageForAutoAnnotationScan implements HasMessageProperties<TestMessageProperties>
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
