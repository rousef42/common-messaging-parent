/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.validators;

import com.dell.cpsd.contract.extension.amqp.EventMessageProperties;
import com.dell.cpsd.contract.extension.amqp.RequestMessageProperties;
import com.dell.cpsd.contract.extension.amqp.ResponseMessageProperties;
import com.dell.cpsd.contract.extension.amqp.message.EventMessage;
import com.dell.cpsd.contract.extension.amqp.message.RequestMessage;
import com.dell.cpsd.contract.extension.amqp.message.ResponseMessage;
import com.dell.cpsd.contract.extension.amqp.visitor.MessageVisitor;

/**
 * Default implementation of message validator.
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultMessageValidator<M extends com.dell.cpsd.contract.extension.amqp.message.Message> extends GenericMessageValidator<M>
        implements MessageVisitor
{

    @Override
    protected void validateMessage(M message, ValidationResult validationResult) throws Exception
    {
        validationResult.addValidationResultErrors((ValidationResult) message.accept(this));
    }

    @Override
    public Object visit(RequestMessage message) throws RuntimeException
    {
        ValidationResult validationResult = new ValidationResult();
        RequestMessageProperties properties = message.getMessageProperties();
        validateNotNull(properties, "messageProperties", validationResult);
        if (properties != null)
        {
            validateNotEmpty(properties.getCorrelationId(), "correlationId", validationResult);
            validateNotEmpty(properties.getReplyTo(), "replyTo", validationResult);
        }
        return validationResult;
    }

    @Override
    public Object visit(ResponseMessage message) throws RuntimeException
    {
        ValidationResult validationResult = new ValidationResult();
        ResponseMessageProperties properties = message.getMessageProperties();
        validateNotNull(properties, "messageProperties", validationResult);
        if (properties != null)
        {
            validateNotEmpty(properties.getCorrelationId(), "correlationId", validationResult);
        }
        return validationResult;
    }

    @Override
    public Object visit(EventMessage message) throws RuntimeException
    {
        ValidationResult validationResult = new ValidationResult();
        EventMessageProperties properties = message.getMessageProperties();
        validateNotNull(properties, "messageProperties", validationResult);
        if (properties != null)
        {
            validateNotEmpty(properties.getCorrelationId(), "correlationId", validationResult);
        }
        return validationResult;
    }
}
