/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.consumer.error;

import com.dell.cpsd.common.rabbitmq.exceptions.RabbitMQException;
import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedError;
import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedErrorsProvider;
import com.dell.cpsd.common.rabbitmq.message.ErrorContainer;
import com.dell.cpsd.common.rabbitmq.message.HasErrors;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ErrorResponseException;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.ERROR_RESPONSE_FAILED_E;
import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.ERROR_RESPONSE_NO_PROPERTY_E;
import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.ERROR_RESPONSE_UNEXPECTED_ERROR_E;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Creates ResponseMessageException using standard approach from
 * <a href="https://wiki.ent.vce.com/display/VSE/AMQP+considerations">https://wiki.ent.vce.com/display/VSE/AMQP+considerations</a>
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultErrorTransformer<
        ErrorMessage extends ErrorContainer,
        ErrorResponseMessage extends HasMessageProperties<? extends MessagePropertiesContainer> & HasErrors<ErrorMessage>
        >
        implements ErrorTransformer<HasMessageProperties<?>>
{
    private static final Logger log = LoggerFactory.getLogger(DefaultErrorTransformer.class);

    public static final String ERROR_BINDING_TEMPLATE = "${handler.routingKey}.error.${request.replyTo}";

    protected Supplier<ErrorResponseMessage> errorMessageSupplier;
    protected Supplier<ErrorMessage> errorSupplier;
    protected String responseExchange;
    protected String replyTo;

    public DefaultErrorTransformer(String responseExchange,
                                   String replyTo,
                                   Supplier<ErrorResponseMessage> errorMessageSupplier,
                                   Supplier<ErrorMessage> errorSupplier)
    {
        this.errorMessageSupplier = errorMessageSupplier;
        this.errorSupplier = errorSupplier;
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
        if (e instanceof LocalizedErrorsProvider)
        {
            List<LocalizedError> errors = ((LocalizedErrorsProvider) e).getLocalizedErrors();
            return createResponseMessageException(errors, e, context);
        }

        return createResponseMessageException(e.getMessage(), e, context);
    }

    protected Exception createResponseMessageException(String errorText, Exception e, ErrorContext<HasMessageProperties<?>> context)
    {
        LocalizedError error = ERROR_RESPONSE_UNEXPECTED_ERROR_E.getLocalizedError(errorText);
        return createResponseMessageException(asList(error), e, context);
    }

    protected Exception createResponseMessageException(List<LocalizedError> errors, Exception e, ErrorContext<HasMessageProperties<?>> context)
    {
        try
        {
            HasMessageProperties<?> requestMessage = context.getRequestMessage();
            validate(requestMessage);

            String routingKey = createRoutingKey(context);

            ErrorResponseMessage message = errorMessageSupplier.get();
            populateErrorMessage(errors, requestMessage, message);

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

    protected void populateErrorMessage(List<LocalizedError> errors, HasMessageProperties<?> requestMessage, ErrorResponseMessage message)
    {
        MessagePropertiesContainer properties = message.getMessageProperties();
        properties.setCorrelationId(requestMessage.getMessageProperties().getCorrelationId());
        properties.setReplyTo(replyTo);
        properties.setTimestamp(new Date());

        List<ErrorMessage> errorMessages = new ArrayList<>();
        for (LocalizedError localizedError : errors)
        {
            ErrorMessage errorMessage = errorSupplier.get();
            populateErrorDetails(errorMessage, localizedError);
            errorMessages.add(errorMessage);
        }

        message.setErrors(errorMessages);
    }

    protected void populateErrorDetails(ErrorMessage errorMessage, LocalizedError localizedError)
    {
        errorMessage.setCode(localizedError.getMessageCode());
        errorMessage.setMessage(localizedError.getMessage());
    }
}
