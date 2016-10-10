/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer;

import com.dell.cpsd.common.logging.ILogger;

import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;

/**
 * A default implementation of the UnhandledMessageHandler that will write
 *  the details out to the application log file.
 *  
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * 
 * @version 1.0
 *
 * @since   SINCE-TBD
 */
public class LoggingUnhandledMessageHandler implements UnhandledMessageHandler
{
    /*
     * The logger for this class
     */
    private static final ILogger LOGGER = 
        RabbitMQLoggingManager.getLogger(LoggingUnhandledMessageHandler.class);
    
    /*
     * The name of the consumer.
     */
    private String consumerName = null;
    
    
    /**
     * LoggingUnhandledMessageHandler constructor.
     * 
     * @param   consumerName    The name of the consumer.
     * 
     * @throws  IllegalArgumentException    Thrown if the consumer name is null.
     * 
     * @since   SINCE-TBD
     */
    public LoggingUnhandledMessageHandler(final String consumerName)
    {
        super();
        
        if (consumerName == null)
        {
            throw new IllegalArgumentException("The consumer name is null.");
        }
        
        this.consumerName = consumerName;
    }
    
    
    /**
     * Log the received message details.  Attempts to log the payload as a 
     * String if possible, as most messages will be text based protocols 
     * such as json or xml.
     *
     * @param   payload     The message payload.
     * 
     * @since   SINCE-TBD
     */
    @Override 
    public void unhandledMessage(byte[] payload)
    {
        try
        {
            final String message = new String(payload);
            
            Object[] lparams = {this.consumerName, message};
            LOGGER.error(RabbitMQMessageCode.MESSAGE_CONSUMER_E.getMessageCode(),
                        lparams);
        }
        catch (Exception exception)
        {
            Object[] lparams = {this.consumerName, payload};
            LOGGER.error(RabbitMQMessageCode.MESSAGE_CONSUMER_E.getMessageCode(),
                        lparams, exception);
        }
    }
}