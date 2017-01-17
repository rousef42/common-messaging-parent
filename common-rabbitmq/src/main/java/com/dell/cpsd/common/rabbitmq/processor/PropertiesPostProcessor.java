/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.processor;

import java.util.List;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.Message;

import org.springframework.amqp.core.MessagePostProcessor;

import org.springframework.amqp.AmqpException;

import com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer;

/**
 * The default message post processor to handle message properties.
 *
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * 
 * @version  1.0
 *
 * @since   SINCE-TDB
 */
public class PropertiesPostProcessor<M extends MessagePropertiesContainer> implements MessagePostProcessor
{
    /**
     * The properties to use in post processing of the message.
     */
    private M properties = null;
    
    /**
     * PropertiesPostProcessor constructor
     * 
     * @param   properties  The properties to use in post processing.
     * 
     * @since   1.0
     */
    public PropertiesPostProcessor(M properties)
    {
        super();
        
        if (properties == null)
        {
            throw new IllegalArgumentException("The message properties container is null.");
        }
        
        this.properties = properties;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override 
    public Message postProcessMessage(final Message message) throws AmqpException
    {
        if (message == null)
        {
            return message;
        }
        
        final MessageProperties messageProperties = message.getMessageProperties();
          
        messageProperties.setTimestamp(this.properties.getTimestamp());
        
        final String correlationId = this.properties.getCorrelationId();
        
        if (correlationId != null)
        {
            try
            {
                messageProperties.setCorrelationId(correlationId.getBytes("UTF-8"));
                
            } catch (UnsupportedEncodingException exception)
            {
            }
        }
        
        messageProperties.setCorrelationIdString(this.properties.getCorrelationId());
        messageProperties.setReplyTo(this.properties.getReplyTo());
        
        return message;
    }
}
