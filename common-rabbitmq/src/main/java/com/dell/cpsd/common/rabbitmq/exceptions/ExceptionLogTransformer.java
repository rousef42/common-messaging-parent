package com.dell.cpsd.common.rabbitmq.exceptions;

import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ErrorResponseException;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;

/**
 * Prepares exception for logs: strips wrapper exceptions that bring no useful context.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class ExceptionLogTransformer
{
    public Throwable transform(Throwable cause)
    {
        if (cause instanceof ListenerExecutionFailedException && cause.getCause() != null)
        {
            cause = cause.getCause();
        }
        if (cause instanceof ErrorResponseException && cause.getCause() != null)
        {
            cause = cause.getCause();
        }
        return cause;
    }
}
