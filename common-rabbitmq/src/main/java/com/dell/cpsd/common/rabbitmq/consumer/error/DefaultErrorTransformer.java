/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.consumer.error;

import com.dell.cpsd.common.rabbitmq.message.HasErrorMessage;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;

import java.util.Date;

/**
 * Creates ResponseMessageException using standard approach from
 * <a href="https://wiki.ent.vce.com/display/VSE/AMQP+considerations">https://wiki.ent.vce.com/display/VSE/AMQP+considerations</a>
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultErrorTransformer<
        MessageProperties extends MessagePropertiesContainer,
        ErrorMessage extends HasMessageProperties<MessageProperties> & HasErrorMessage> implements ErrorTransformer<HasMessageProperties<?>>
{

    protected Class<MessageProperties> messagePropertiesClass;
    protected Class<ErrorMessage> errorMessageClass;
    protected String responseExchange;
    protected String responseRoutingKeyPrefix;
    protected String replyTo;

    public DefaultErrorTransformer(Class<MessageProperties> messagePropertiesClass,
                                   Class<ErrorMessage> errorMessageClass, String responseExchange,
                                   String responseRoutingKeyPrefix, String replyTo)
    {
        this.messagePropertiesClass = messagePropertiesClass;
        this.errorMessageClass = errorMessageClass;
        this.responseExchange = responseExchange;
        this.responseRoutingKeyPrefix = responseRoutingKeyPrefix;
        this.replyTo = replyTo;
    }

    @Override
    public Exception transform(Exception e, HasMessageProperties<?> requestMessage)
    {
        if (e instanceof MessageValidationException)
        {
            String errorText = ((MessageValidationException) e).getFirstError();
            if (errorText != null)
            {
                return createResponseMessageException(errorText, e, requestMessage);
            }
            // Fallback to default error handling if for some reason there are no validation messages
        }

        return createResponseMessageException(e.getMessage(), e, requestMessage);
    }

    protected Exception createResponseMessageException(String errorText, Exception e, HasMessageProperties<?> requestMessage)
    {
        try
        {
            String routingKey = responseRoutingKeyPrefix + "." + requestMessage.getMessageProperties().getReplyTo();

            ErrorMessage message = errorMessageClass.newInstance();
            MessageProperties properties = messagePropertiesClass.newInstance();
            message.setMessageProperties(properties);
            populateErrorMessage(errorText, requestMessage, message);

            return new ResponseMessageException(e, responseExchange, routingKey, message);
        }
        catch (Exception internalError)
        {
            return new TransformationException("Failed to transform error message: " + internalError.getMessage(), e, internalError);
        }
    }

    protected void populateErrorMessage(String errorText, HasMessageProperties<?> requestMessage, ErrorMessage message)
    {
        MessageProperties properties = message.getMessageProperties();
        properties.setCorrelationId(requestMessage.getMessageProperties().getCorrelationId());
        properties.setReplyTo(replyTo);
        properties.setTimestamp(new Date());

        message.setErrorMessage(errorText);
    }
}
