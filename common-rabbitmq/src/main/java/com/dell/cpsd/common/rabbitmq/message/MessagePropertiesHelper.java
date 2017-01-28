/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.message;

import java.util.Date;

import java.io.UnsupportedEncodingException;

import org.springframework.amqp.core.MessageProperties;

/**
 * This helper class for message properties.
 * 
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 * 
 * @since   1.0
 */
public class MessagePropertiesHelper
{
    /**
     * This makes a <code>MessageProperties</code> with the specified properties.
     * 
     * @param   timestamp       The timestamp.
     * @param   correlationId   The correlation identifier.
     * @param   replyTo         The reply to destination.
     * 
     * @since   1.0
     */
    public static MessageProperties makeMessageProperties(final Date timestamp, 
            final String correlationId, final String replyTo)
    {
        final MessageProperties messageProperties = new MessageProperties();
        
        messageProperties.setTimestamp(timestamp);
        messageProperties.setCorrelationIdString(correlationId);
        
        if (correlationId != null)
        {
            try
            {
                messageProperties.setCorrelationId(correlationId.getBytes("UTF-8"));

            }
            catch (UnsupportedEncodingException exception)
            {
            }
        }
        
        messageProperties.setReplyTo(replyTo);
        
        return messageProperties;
    }
    
    
    /**
     * This returns the correalation identifier in the message properties, or
     * null.
     * 
     * @param   messageProperties   The message properties.
     * 
     * @return  The correlation identifier, or null.
     * 
     * @since   1.0
     */
    public static String getCorrelationId(final MessageProperties messageProperties)
    {
        if (messageProperties == null)
        {
            return null;
        }
        
        String correlationId = messageProperties.getCorrelationIdString();

        if ((correlationId == null) || (correlationId.isEmpty()))
        {
            byte[] array = messageProperties.getCorrelationId();

            if (array != null)
            {
                try
                {
                    correlationId = new String(array, "UTF-8");
                }
                catch (UnsupportedEncodingException exception)
                {
                }
            }
        }
        
        return correlationId;
    }
}
