/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.consumer.LoggingUnhandledMessageHandler;
import com.dell.cpsd.common.rabbitmq.context.RabbitContext;
import com.dell.cpsd.common.rabbitmq.context.RabbitContextAware;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This is a generic handler base class.
 * <p>
 * <p/>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @since SINCE-TBD
 */
public class DelegatingMessageHandler extends LoggingUnhandledMessageHandler implements RabbitContextAware
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(DelegatingMessageHandler.class);

    private Map<Class<?>, GenericMessageHandler<?, ?>> handlers = new HashMap<>();

    /**
     * GenericMessageHandler constructor.
     *
     * @since 1.0
     */
    public DelegatingMessageHandler()
    {
        this("DelegatingMessageHandler@" + UUID.randomUUID().toString());
    }

    /**
     * GenericMessageHandler constructor.
     *
     * @param consumerName The consumer identifier.
     * @since 1.0
     */
    public DelegatingMessageHandler(String consumerName)
    {
        super(consumerName);
    }

    /**
     * This handles the message.
     *
     * @param requestMessage The message to handle.
     * @throws Exception Thrown if the message cannot be handled.
     * @since 1.0
     */
    public void handleMessage(final Object requestMessage) throws Throwable
    {
        try
        {
            GenericMessageHandler handler = handlers.get(requestMessage.getClass());
            if (handler == null)
            {
                LOGGER.error("Unable to resolve handler for message: " + requestMessage);
                return;
            }

            handler.executeOperation(requestMessage);
        }
        catch (Throwable e)
        {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public void addHandler(Class requestMessage, GenericMessageHandler handler)
    {
        handlers.put(requestMessage, handler);
    }

    @Override
    public void setRabbitContext(RabbitContext rabbitContext)
    {
        handlers.values().forEach(handler -> handler.setRabbitContext(rabbitContext));
    }
}
