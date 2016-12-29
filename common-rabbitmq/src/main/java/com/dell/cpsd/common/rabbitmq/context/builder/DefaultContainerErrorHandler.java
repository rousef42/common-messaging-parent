/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;
import org.springframework.util.ErrorHandler;

/**
 * Logs errors by default
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultContainerErrorHandler implements ErrorHandler
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
        Object[] params = {listenerName, cause.getMessage()};
        LOGGER.error(RabbitMQMessageCode.MESSAGE_CONSUMER_E.getMessageCode(), params, cause);
    }
};
