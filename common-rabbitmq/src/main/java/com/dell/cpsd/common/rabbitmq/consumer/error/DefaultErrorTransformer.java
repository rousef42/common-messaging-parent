/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.consumer.error;

import com.dell.cpsd.common.rabbitmq.message.HasErrorMessage;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ErrorResponseException;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;

import java.util.Date;
import java.util.function.Supplier;

/**
 * Creates ResponseMessageException using standard approach from
 * <a href="https://wiki.ent.vce.com/display/VSE/AMQP+considerations">https://wiki.ent.vce.com/display/VSE/AMQP+considerations</a>
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultErrorTransformer<ErrorMessage extends HasMessageProperties<? extends MessagePropertiesContainer> & HasErrorMessage>
        implements ErrorTransformer<HasMessageProperties<?>>
{
    public static final String ERROR_BINDING_TEMPLATE = "${handler.routingKey}.error.${request.replyTo}";

    protected Supplier<ErrorMessage> errorMessageSupplier;
    protected String responseExchange;
    protected String replyTo;

    public DefaultErrorTransformer(String responseExchange, String replyTo, Supplier<ErrorMessage> errorMessageSupplier)
    {
        this.errorMessageSupplier = errorMessageSupplier;
        this.responseExchange = responseExchange;
        this.replyTo = replyTo;
    }

    @Override
    public Exception transform(Exception e, ErrorContext<HasMessageProperties<?>> context)
    {
        if (e instanceof ResponseMessageException)
        {
            return e;
        }
        if (e instanceof MessageValidationException)
        {
            String errorText = ((MessageValidationException) e).getFirstError();
            if (errorText != null)
            {
                return createResponseMessageException(errorText, e, context);
            }
            // Fallback to default error handling if for some reason there are no validation messages
        }

        return createResponseMessageException(e.getMessage(), e, context);
    }

    protected Exception createResponseMessageException(String errorText, Exception e, ErrorContext<HasMessageProperties<?>> context)
    {
        try
        {
            HasMessageProperties<?> requestMessage = context.getRequestMessage();

            String routingKey = createRoutingKey(context);

            ErrorMessage message = errorMessageSupplier.get();
            populateErrorMessage(errorText, requestMessage, message);

            return new ErrorResponseException(e, responseExchange, routingKey, message);
        }
        catch (Exception internalError)
        {
            return new TransformationException("Failed to transform error message: " + internalError.getMessage(), e, internalError);
        }
    }

    protected String createRoutingKey(ErrorContext<HasMessageProperties<?>> context)
    {
        HasMessageProperties<?> requestMessage = context.getRequestMessage();
        return ERROR_BINDING_TEMPLATE
                .replace("${handler.routingKey}", context.getErrorRoutingKeyPrefix())
                .replace("${request.replyTo}", requestMessage.getMessageProperties().getReplyTo());
    }

    protected void populateErrorMessage(String errorText, HasMessageProperties<?> requestMessage, ErrorMessage message)
    {
        MessagePropertiesContainer properties = message.getMessageProperties();
        properties.setCorrelationId(requestMessage.getMessageProperties().getCorrelationId());
        properties.setReplyTo(replyTo);
        properties.setTimestamp(new Date());

        message.setErrorMessage(errorText);
    }
}
