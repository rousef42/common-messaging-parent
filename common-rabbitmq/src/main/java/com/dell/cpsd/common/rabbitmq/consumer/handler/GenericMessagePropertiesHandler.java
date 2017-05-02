/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.consumer.LoggingUnhandledMessageHandler;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import org.springframework.amqp.core.MessageProperties;

import java.util.UUID;

/**
 * This is a generic handler base class for messages with message properties.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @since SINCE-TBD
 */
public abstract class GenericMessagePropertiesHandler<R extends Object, E extends Throwable> extends LoggingUnhandledMessageHandler
{
    /*
     * The logger for this class.
     */
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(GenericMessagePropertiesHandler.class);

    /**
     * GenericMessagePropertiesHandler constructor.
     *
     * @since 1.0
     */
    public GenericMessagePropertiesHandler()
    {
        this("GenericMessageHandler@" + UUID.randomUUID().toString());
    }

    /**
     * GenericMessagePropertiesHandler constructor.
     *
     * @param consumerName The consumer identifier.
     * @since 1.0
     */
    public GenericMessagePropertiesHandler(String consumerName)
    {
        super(consumerName);
    }

    /**
     * This executes the operation This might include updating a database,
     * sending further messages to continue the conversation.
     *
     * @param requestMessage    The message to process.
     * @param messageProperites The message properties.
     * @throws Throwable Thrown if there operation fails.
     * @since 1.0
     */
    protected abstract void executeOperation(final R requestMessage, final MessageProperties messageProperties) throws Throwable;

    /**
     * This handles the message.
     *
     * @param requestMessage The message to handle.
     * @throws Exception Thrown if the message cannot be handled.
     * @since 1.0
     */
    public void handleMessage(final R requestMessage, final MessageProperties messageProperties) throws E
    {
        try
        {
            executeOperation(requestMessage, messageProperties);
        }
        catch (Throwable e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        finally
        {
            cleanup(requestMessage);
        }
    }

    /**
     * This performs an required cleanup after the message is handled.
     *
     * @param requestMessage The request message.
     * @since 1.0
     */
    protected void cleanup(final R requestMessage)
    {
        // do nothing by default
    }

    /**
     * This converts an exception.
     *
     * @param t The <code>Throwable</code> to convert.
     * @return The converted exception.
     * @sincve 1.0
     */
    protected abstract E convertException(Throwable t);
}
