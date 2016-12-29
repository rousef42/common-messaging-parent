package com.dell.cpsd.common.rabbitmq.exceptions;

import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;

/**
 * Scala-like trait that allows classes to unwrap AMQP exceptions.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface AmqpExceptionUnwrapTrait
{
    default Throwable unwrap(Throwable cause)
    {
        if (cause instanceof ListenerExecutionFailedException && cause.getCause() != null)
        {
            return cause.getCause();
        }
        return cause;
    }
}
