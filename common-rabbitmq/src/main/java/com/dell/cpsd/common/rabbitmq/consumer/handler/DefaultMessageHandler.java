/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;

import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorContext;
import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.message.visitor.CorrelationIdVisitor;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidator;
import com.dell.cpsd.common.rabbitmq.validators.ValidationResult;

/**
 * Validates message. Converts exception in case of error.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public abstract class DefaultMessageHandler<M extends com.dell.cpsd.contract.extension.amqp.message.Message>
        implements MessageHandler<M>
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    protected Class<M>                                  messageClass;
    protected MessageValidator<M>                       validator;
    protected String                                    errorRoutingKeyPrefix;
    protected ErrorTransformer<com.dell.cpsd.contract.extension.amqp.message.Message> errorTransformer;
    protected CorrelationIdVisitor correlationIdVisitor;

    public DefaultMessageHandler(Class<M> messageClass, MessageValidator<M> validator, String errorRoutingKeyPrefix,
            ErrorTransformer<com.dell.cpsd.contract.extension.amqp.message.Message> errorTransformer)
    {
        this.messageClass = messageClass;
        this.validator = validator;
        this.errorRoutingKeyPrefix = errorRoutingKeyPrefix;
        this.errorTransformer = errorTransformer;
    }

    protected abstract void executeOperation(M message) throws Exception;

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
            log.info("Received message {} with correlationId [{}]", message.getClass().getSimpleName(), getCorrelationId(message));

            validate(message);
            executeOperation(message);

            log.info("Finished processing message {} with correlationId [{}]", message.getClass().getSimpleName(),
                    getCorrelationId(message));
        }
        catch (Exception exception)
        {
            log.error("Failed to process message {} with correlationId [{}]: {}", message.getClass().getSimpleName(),
                    getCorrelationId(message), exception.getMessage());
            handleError(exception, message);
        }
    }

    private Object getCorrelationId(M message)
    {
        return message.accept(correlationIdVisitor);
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

    protected void handleError(Exception exception, M message) throws Exception
    {
        throw convertError(exception, message);
    }

    protected Exception convertError(Exception exception, M message)
    {
        ErrorContext<com.dell.cpsd.contract.extension.amqp.message.Message> context = new ErrorContext<>(message, errorRoutingKeyPrefix);
        return errorTransformer.transform(exception, context);
    }
}
