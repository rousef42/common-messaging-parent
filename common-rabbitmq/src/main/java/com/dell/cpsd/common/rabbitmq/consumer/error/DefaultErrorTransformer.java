/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.consumer.error;

import com.dell.cpsd.common.rabbitmq.exceptions.RabbitMQException;
import com.dell.cpsd.common.rabbitmq.message.HasErrorMessage;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ErrorResponseException;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.function.Supplier;

import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.ERROR_RESPONSE_FAILED_E;
import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.ERROR_RESPONSE_NO_PROPERTY_E;
import static org.apache.commons.lang3.StringUtils.isEmpty;

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
    private static final Logger log = LoggerFactory.getLogger(DefaultErrorTransformer.class);

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
            validate(requestMessage);

            String routingKey = createRoutingKey(context);

            ErrorMessage message = errorMessageSupplier.get();
            populateErrorMessage(errorText, requestMessage, message);

            return new ErrorResponseException(e, responseExchange, routingKey, message);
        }
        catch (Exception internalError)
        {
            String msg = ERROR_RESPONSE_FAILED_E.getMessageText(e.getMessage());
            log.error(msg, internalError);
            return e;
        }
    }

    /**
     * Ensures incoming message has enough data to create response error message.
     *
     * @param requestMessage request message
     * @throws RabbitMQException in case of absent data
     */
    protected void validate(HasMessageProperties<?> requestMessage) throws RabbitMQException
    {
        MessagePropertiesContainer messageProperties = requestMessage.getMessageProperties();
        if (messageProperties == null)
        {
            throw new RabbitMQException(ERROR_RESPONSE_NO_PROPERTY_E.getMessageText("messageProperties"));
        }
        if (isEmpty(messageProperties.getReplyTo()))
        {
            throw new RabbitMQException(ERROR_RESPONSE_NO_PROPERTY_E.getMessageText("replyTo"));
        }
        if (isEmpty(messageProperties.getCorrelationId()))
        {
            throw new RabbitMQException(ERROR_RESPONSE_NO_PROPERTY_E.getMessageText("correlationId"));
        }
    }

    protected String createRoutingKey(ErrorContext<HasMessageProperties<?>> context) throws RabbitMQException
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
