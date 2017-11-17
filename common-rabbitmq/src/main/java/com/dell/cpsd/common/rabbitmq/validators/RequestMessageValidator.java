/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.validators;

import com.dell.cpsd.contract.extension.amqp.message.HasMessageProperties;
import com.dell.cpsd.contract.extension.amqp.message.MessagePropertiesContainer;

/**
 * Validator implementation of Request message.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 * 
 * @param <M>
 *            - Message class to be validated
 */
public class RequestMessageValidator<M extends HasMessageProperties<? extends MessagePropertiesContainer>>
        extends GenericMessageValidator<M>
{
    /**
     * Validates MessageProperties (Non Null), correlationId(Non Blank) and replyTo(Non Blank)
     */
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
