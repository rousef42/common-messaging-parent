/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;

import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessor;
import com.dell.cpsd.common.rabbitmq.MessageAnnotationProcessorCallback;

/**
 * The is a factory for default retry templates.
 * 
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 * 
 * @since   1.1
 */
public class DefaultClassMapperFactory 
{
    /**
     * This creates the <code>DefaultClassMapper</code> with the list of message
     * classes.
     * 
     * @param   messageClasses  The list of message classes.
     * 
     * @return  The default class mapper with the message classes.
     * 
     * @since   1.1
     */
    public static ClassMapper makeClassMapper(
                                        final List<Class<?>>  messageClasses)
    {
        if (messageClasses == null)
        {
            throw new IllegalArgumentException("The message class list is null.");
        }
        
        final DefaultClassMapper classMapper = new DefaultClassMapper();
        final Map<String, Class<?>> classMappings = new HashMap<>();

        final MessageAnnotationProcessor messageAnnotationProcessor = new MessageAnnotationProcessor();

        messageAnnotationProcessor.process(new MessageAnnotationProcessorCallback()
        {
            @Override
            public void found(String messageType, Class messageClass)
            {
                classMappings.put(messageType, messageClass);
            }
        }, messageClasses);

        classMapper.setIdClassMapping(classMappings);

        return classMapper;
    }
}
