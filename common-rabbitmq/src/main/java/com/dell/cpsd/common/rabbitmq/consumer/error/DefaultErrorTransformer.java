/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.error;

import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.ERROR_RESPONSE_FAILED_E;
import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.ERROR_RESPONSE_NO_PROPERTY_E;
import static com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode.ERROR_RESPONSE_UNEXPECTED_ERROR_E;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dell.cpsd.common.rabbitmq.exceptions.RabbitMQException;
import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedError;
import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedErrorsProvider;
import com.dell.cpsd.common.rabbitmq.message.ErrorContainer;
import com.dell.cpsd.common.rabbitmq.message.HasErrors;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ErrorResponseException;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import com.dell.cpsd.contract.extension.amqp.EventMessageProperties;
import com.dell.cpsd.contract.extension.amqp.MessagePropertiesFactory;
import com.dell.cpsd.contract.extension.amqp.RequestMessageProperties;
import com.dell.cpsd.contract.extension.amqp.ResponseMessageProperties;
import com.dell.cpsd.contract.extension.amqp.message.EventMessage;
import com.dell.cpsd.contract.extension.amqp.message.Message;
import com.dell.cpsd.contract.extension.amqp.message.RequestMessage;
import com.dell.cpsd.contract.extension.amqp.message.ResponseMessage;

/**
 * Creates ResponseMessageException using standard approach from
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultErrorTransformer<ErrorMessage extends ErrorContainer, ErrorResponseMessage extends Message & HasErrors<ErrorMessage>>
        implements ErrorTransformer<Message>
{
    public static final String               ERROR_BINDING_TEMPLATE = "${handler.routingKey}.error.${request.replyTo}";
    private static final Logger              log                    = LoggerFactory.getLogger(DefaultErrorTransformer.class);
    protected Supplier<ErrorResponseMessage> errorMessageSupplier;
    protected Supplier<ErrorMessage>         errorSupplier;
    protected String                         responseExchange;
    protected String                         replyTo;

    public DefaultErrorTransformer(String responseExchange, String replyTo, Supplier<ErrorResponseMessage> errorMessageSupplier,
            Supplier<ErrorMessage> errorSupplier)
    {
        this.errorMessageSupplier = errorMessageSupplier;
        this.errorSupplier = errorSupplier;
        this.responseExchange = responseExchange;
        this.replyTo = replyTo;
    }

    @Override
    public Exception transform(Exception e, ErrorContext<Message> context)
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

    protected Exception createResponseMessageException(String errorText, Exception e, ErrorContext<Message> context)
    {
        LocalizedError error = ERROR_RESPONSE_UNEXPECTED_ERROR_E.getLocalizedError(errorText);
        return createResponseMessageException(asList(error), e, context);
    }

    protected Exception createResponseMessageException(List<LocalizedError> errors, Exception e, ErrorContext<Message> context)
    {
        try
        {
            Message requestMessage = context.getRequestMessage();
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
     * @param requestMessage
     *            request message
     * @throws RabbitMQException
     *             in case of absent data
     */
    protected void validate(Message requestMessage) throws RabbitMQException
    {
        if (requestMessage instanceof RequestMessage)
        {
            RequestMessageProperties messageProperties = ((RequestMessage) requestMessage).getMessageProperties();
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
        else if (requestMessage instanceof ResponseMessage)
        {
            ResponseMessageProperties messageProperties = ((ResponseMessage) requestMessage).getMessageProperties();
            if (messageProperties == null)
            {
                throw new RabbitMQException(ERROR_RESPONSE_NO_PROPERTY_E.getMessageText("messageProperties"));
            }
            if (isEmpty(messageProperties.getCorrelationId()))
            {
                throw new RabbitMQException(ERROR_RESPONSE_NO_PROPERTY_E.getMessageText("correlationId"));
            }
        }
        else if (requestMessage instanceof EventMessage)
        {
            EventMessageProperties messageProperties = ((EventMessage) requestMessage).getMessageProperties();
            if (messageProperties == null)
            {
                throw new RabbitMQException(ERROR_RESPONSE_NO_PROPERTY_E.getMessageText("messageProperties"));
            }
            if (isEmpty(messageProperties.getCorrelationId()))
            {
                throw new RabbitMQException(ERROR_RESPONSE_NO_PROPERTY_E.getMessageText("correlationId"));
            }
        }
    }

    protected String createRoutingKey(ErrorContext<Message> context) throws RabbitMQException
    {
        Message requestMessage = context.getRequestMessage();
        if (requestMessage instanceof RequestMessage)
        {
            return ERROR_BINDING_TEMPLATE.replace("${handler.routingKey}", context.getErrorRoutingKeyPrefix()).replace("${request.replyTo}",
                    ((RequestMessage) requestMessage).getMessageProperties().getReplyTo());
        }
        // TODO: Re-check the approach when message is response. Passing 'N/A' as reply as of now.
        return ERROR_BINDING_TEMPLATE.replace("${handler.routingKey}", context.getErrorRoutingKeyPrefix()).replace("${request.replyTo}",
                "N/A");
    }

    protected void populateErrorMessage(List<LocalizedError> errors, Message requestMessage, ErrorResponseMessage message)
    {
        if (message instanceof RequestMessage)
        {
            RequestMessageProperties properties = ((RequestMessage) message).getMessageProperties();

            // RequestMessageProperties requestMessageProperties =
            // properties.createChildRequestMessageProperties().replyTo("").timestamp(new Date().toInstant()).
            //done properties.setCorrelationId(requestMessage.getMessageProperties().getCorrelationId());
            //done properties.setReplyTo(replyTo);
            //done properties.setTimestamp(new Date());
            if (requestMessage instanceof RequestMessage && message instanceof RequestMessage)
            {
                RequestMessageProperties requestMessageProperties = MessagePropertiesFactory
                        .createRequest(((RequestMessage) requestMessage).getMessageProperties().getCorrelationId()).replyTo(replyTo)
                        .build();
                //((RequestMessage)message).set
            }
        }

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
