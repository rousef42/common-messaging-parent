/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer;

/**
 * A base class for any message consumer.  This will handle any unhandled
 * messages by passing them onto a UnhandledMessageHandler.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 * @version 1.0
 * @since SINCE-TBD
 * @deprecated Use DefaultMessageListener with DefaultMessageHandler instead.
 */
@Deprecated
public abstract class UnhandledMessageConsumer
{
    /*
     * The handler to use to process unhandled messages, defaults to a logging
     * handler.
     */
    private UnhandledMessageHandler unhandledMessageHandler = null;

    /**
     * UnhandledMessageConsumer constructor.
     *
     * @since SINCE-TBD
     */
    public UnhandledMessageConsumer()
    {
        super();

        this.unhandledMessageHandler = new LoggingUnhandledMessageHandler(this.getClass().getName());
    }

    /**
     * Sets the UnhandledMessageHandler
     *
     * @param unhandledMessageHandler The UnhandledMessageHandler to be used
     * @since SINCE-TBD
     */
    public void setUnhandledMessageHandler(UnhandledMessageHandler unhandledMessageHandler)
    {
        this.unhandledMessageHandler = unhandledMessageHandler;
    }

    /**
     * Handles the byte array, this is called from SpringAMQP when an unhandled
     * message is received.
     *
     * @param messagePayload The message payload
     * @since SINCE-TBD
     */
    public void handleMessage(byte[] messagePayload)
    {
        if (unhandledMessageHandler != null)
        {
            unhandledMessageHandler.unhandledMessage(messagePayload);
        }
    }
}
