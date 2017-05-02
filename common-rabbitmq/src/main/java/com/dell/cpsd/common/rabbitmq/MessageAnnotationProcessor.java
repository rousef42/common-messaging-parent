/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq;

import com.dell.cpsd.common.rabbitmq.annotation.Message;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * An annotation process for evaluating @Message annotations.
 * <p>
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 *
 * @version 1.0
 * @since SINCE-TDB
 */
public class MessageAnnotationProcessor
{
    /**
     * Called to process the list of classes.
     *
     * @param callback
     * @param classes
     */
    public void process(final MessageAnnotationProcessorCallback callback, final List<Class<?>> classes)
    {
        for (final Class aClass : classes)
        {
            process(callback, aClass);
        }
    }

    /**
     * Calls the callback if the class contains the Message annotation.
     *
     * @param callback
     * @param aClass
     */
    public void process(final MessageAnnotationProcessorCallback callback, final Class<?> aClass)
    {
        Annotation annotation = aClass.getAnnotation(Message.class);

        if (annotation != null)
        {
            Message messageAnnotation = (Message) annotation;
            callback.found(messageAnnotation.value(),
                    aClass); // @TODO, update callback to include version ... messageAnnotation.version());
        }
    }
}
