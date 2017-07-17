/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import org.springframework.amqp.core.Message;

/**
 * Handles specific AMQP message.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public interface MessageHandler<T>
{
    boolean canHandle(Message message, Object body);

    void handleMessage(T message) throws Exception;
}
