/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.message;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * The is a factory for a default message converter configuration.
 * 
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 * 
 * @since   1.1
 */
public class DefaultMessageConverterFactory
{
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
    
    /**
     * This returns the <code>MessageConverter</code> for the
     * <code>RabbitTemplate</code>.
     * 
     * @param   classMapper The class mapper for the converter.
     *
     * @return  The <code>MessageConverter</code> for the template.
     * 
     * @since   1.1
     */
    public static MessageConverter makeMessageConverter(final ClassMapper classMapper)
    {
        if (classMapper == null)
        {
            throw new IllegalArgumentException("The class mapper is null.");
        }
        
        final Jackson2JsonMessageConverter messageConverter = 
                                            new Jackson2JsonMessageConverter();
        messageConverter.setClassMapper(classMapper);
        messageConverter.setCreateMessageIds(true);

        final ObjectMapper objectMapper = new ObjectMapper();

        // use ISO8601 format for dates
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new SimpleDateFormat(ISO8601_DATE_FORMAT));

        // ignore properties we don't need or aren't expecting
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        messageConverter.setJsonObjectMapper(objectMapper);

        return messageConverter;
    }
}
