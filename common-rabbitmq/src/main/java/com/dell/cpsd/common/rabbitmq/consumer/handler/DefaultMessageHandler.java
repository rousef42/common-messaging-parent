/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.consumer.handler;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidator;
import com.dell.cpsd.common.rabbitmq.validators.ValidationResult;
import org.springframework.amqp.core.Message;

/**
 * TODO: Document usage. Set proper Vision version in since tag.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public abstract class DefaultMessageHandler<M extends HasMessageProperties<? extends MessagePropertiesContainer>> implements MessageHandler<M>
{
    protected Class<M> messageClass;
    protected ErrorTransformer<HasMessageProperties<?>> errorTransformer;
    protected MessageValidator<M> validator;

    protected abstract void executeOperation(M message) throws Exception;

    public DefaultMessageHandler(Class<M> messageClass, MessageValidator<M> validator, ErrorTransformer<HasMessageProperties<?>> errorTransformer)
    {
        this.messageClass = messageClass;
        this.validator = validator;
        this.errorTransformer = errorTransformer;
    }

    @Override
    public boolean canHandle(Message message, Object body)
    {
        return messageClass.isInstance(body);
    }

    @Override
    public void handleMessage(M message) throws Exception
    {
        try
        {
            validate(message);
            executeOperation(message);
        }
        catch (Exception e)
        {
            handleError(e, message);
        }
    }

    protected void validate(M message) throws Exception
    {
        if (validator == null)
        {
            return;
        }
        ValidationResult result = validator.validate(message);
        if (!result.isValid())
        {
            throw new MessageValidationException(result);
        }
    }

    protected void handleError(Exception e, M message) throws Exception
    {
        throw errorTransformer.transform(e, message);
    }
}
