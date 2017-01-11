/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq;

import com.dell.cpsd.common.rabbitmq.message.HasErrors;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;

import java.util.List;

/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class TestErrorMessage implements HasMessageProperties<TestMessageProperties>, HasErrors<Error>
{
    private TestMessageProperties messageProperties = new TestMessageProperties();
    private List<Error> errors;

    public TestMessageProperties getMessageProperties()
    {
        return messageProperties;
    }

    public void setMessageProperties(TestMessageProperties messageProperties)
    {
        this.messageProperties = messageProperties;
    }

    public List<Error> getErrors()
    {
        return errors;
    }

    public void setErrors(List<Error> errors)
    {
        this.errors = errors;
    }

}
