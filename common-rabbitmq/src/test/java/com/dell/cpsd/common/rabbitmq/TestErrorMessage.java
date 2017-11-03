/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq;

import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.contract.extension.amqp.message.HasErrors;

import java.util.List;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
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
