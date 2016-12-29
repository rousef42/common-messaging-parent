/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.exceptions.AmqpExceptionUnwrapTrait;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.util.ErrorHandler;

/**
 * Logs errors by default
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultContainerErrorHandler implements ErrorHandler, AmqpExceptionUnwrapTrait
{
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(DefaultContainerErrorHandler.class);

    private String listenerName;

    public DefaultContainerErrorHandler(String listenerName)
    {
        this.listenerName = listenerName;
    }

    @Override
    public void handleError(Throwable cause)
    {
        cause = unwrap(cause);

        if (cause instanceof ImmediateAcknowledgeAmqpException)
        {
            String message = RabbitMQMessageCode.MESSAGE_IMMEDIATE_ACK_E.getMessageText(listenerName, cause.getMessage());
            LOGGER.info(message);
            return;
        }

        String message = RabbitMQMessageCode.AMQP_ERROR_E.getMessageText(listenerName, cause.getMessage());
        LOGGER.error(message, cause);
    }
};
