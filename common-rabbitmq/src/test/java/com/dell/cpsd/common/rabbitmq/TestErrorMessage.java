/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq;

import com.dell.cpsd.common.rabbitmq.message.HasErrorMessage;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;

/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class TestErrorMessage implements HasErrorMessage, HasMessageProperties<TestMessageProperties>
{
    private TestMessageProperties messageProperties = new TestMessageProperties();
    private String errorMessage;

    public TestMessageProperties getMessageProperties()
    {
        return messageProperties;
    }

    public void setMessageProperties(TestMessageProperties messageProperties)
    {
        this.messageProperties = messageProperties;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
}
