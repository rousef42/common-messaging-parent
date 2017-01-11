/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.validators;

import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;

/**
 * Default implementation of message validator.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultMessageValidator<M extends HasMessageProperties<? extends MessagePropertiesContainer>> extends GenericMessageValidator<M>
{
    @Override
    protected void validateMessage(M message, ValidationResult validationResult) throws Exception
    {
        MessagePropertiesContainer properties = message.getMessageProperties();
        validateNotNull(properties, "messageProperties", validationResult);
        if (properties != null)
        {
            validateNotEmpty(properties.getCorrelationId(), "correlationId", validationResult);
            validateNotEmpty(properties.getReplyTo(), "replyTo", validationResult);
        }
    }
}
