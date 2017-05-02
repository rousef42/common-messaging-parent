/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.retrypolicy;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.retry.MessageKeyGenerator;

/**
 * Default message key generator.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultMessageKeyGenerator implements MessageKeyGenerator
{
    private static final String UNDEFINED_MESSAGE_KEY = "UNDEFINED";

    @Override
    public Object getKey(Message message)
    {
        if (message.getMessageProperties().getMessageId() != null)
        {
            return message.getMessageProperties().getMessageId();
        }
        else
        {
            return UNDEFINED_MESSAGE_KEY;
        }
    }
}
