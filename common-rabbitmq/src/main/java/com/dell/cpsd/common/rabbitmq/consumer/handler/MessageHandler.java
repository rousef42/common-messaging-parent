package com.dell.cpsd.common.rabbitmq.consumer.handler;

import org.springframework.amqp.core.Message;

/**
 * Handles specific AMQP message.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public interface MessageHandler<T>
{
    boolean canHandle(Message message, Object body);

    void handleMessage(T message) throws Exception;
}
