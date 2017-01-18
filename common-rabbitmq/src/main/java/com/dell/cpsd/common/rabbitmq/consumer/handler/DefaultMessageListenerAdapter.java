/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import org.springframework.amqp.support.converter.MessageConverter;

import com.dell.cpsd.common.rabbitmq.message.DefaultMessageProperties;

/**
 * This is a default message listener adapter that invokes a message handler 
 * with the <code>MessagePropertiesContainer</code> as a second argument. The
 * first argument is the message.
 * 
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @since   SINCE-TBD
 */

public class DefaultMessageListenerAdapter extends MessageListenerAdapter
{
    /**
     * DefaultMessageListenerAdapter constructor.
     * 
     * @param   delegate            The message listener delegate.
     * @param   messageConverter    The message converter.
     * 
     * @since   1.0
     */
    public DefaultMessageListenerAdapter(Object delegate, MessageConverter messageConverter) 
    {
        super(delegate, messageConverter);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Object invokeListenerMethod(String methodName, Object[] arguments, Message originalMessage)
            throws Exception 
    {
        final MessageProperties originalMessageProperties = originalMessage.getMessageProperties();
        
        DefaultMessageProperties extractedMessageProperties = new DefaultMessageProperties();
        
        if (originalMessageProperties != null)
        {   
            String correlationId = originalMessageProperties.getCorrelationIdString();
            
            if ((correlationId == null) || (correlationId.isEmpty()))
            {
                byte[] array = originalMessageProperties.getCorrelationId();
                
                if (array != null)
                {
                    try
                    {
                        correlationId = new String(array, "UTF-8");
                        
                    } catch (UnsupportedEncodingException exception)
                    { 
                    }
                }
            }
            
            extractedMessageProperties.setCorrelationId(correlationId);
            extractedMessageProperties.setTimestamp(originalMessageProperties.getTimestamp());
            extractedMessageProperties.setReplyTo(originalMessageProperties.getReplyTo());
        }

        // one argument is expected i.e. the message to invoke on the delegate 
        // handler.
        int size = arguments.length;
        
        Object[] enrichedArguments = new Object[size + 1];
        
        System.arraycopy(arguments, 0, enrichedArguments, 0, size);
        
        enrichedArguments[size] = extractedMessageProperties;
        
        return super.invokeListenerMethod(methodName, enrichedArguments, originalMessage);
    }
}
