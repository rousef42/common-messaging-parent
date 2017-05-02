/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * This is a default message listener adapter that invokes a message handler
 * with the <code>MessagePropertiesContainer</code> as a second argument. The
 * first argument is the message.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @since SINCE-TBD
 */

public class DefaultMessageListenerAdapter extends MessageListenerAdapter
{
    /**
     * DefaultMessageListenerAdapter constructor.
     *
     * @param delegate         The message listener delegate.
     * @param messageConverter The message converter.
     * @since 1.0
     */
    public DefaultMessageListenerAdapter(Object delegate, MessageConverter messageConverter)
    {
        super(delegate, messageConverter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object invokeListenerMethod(String methodName, Object[] arguments, Message originalMessage) throws Exception
    {
        final MessageProperties originalMessageProperties = originalMessage.getMessageProperties();

        // one argument is expected i.e. the message to invoke on the delegate 
        // handler.
        int size = arguments.length;

        Object[] enrichedArguments = new Object[size + 1];

        System.arraycopy(arguments, 0, enrichedArguments, 0, size);

        enrichedArguments[size] = originalMessageProperties;

        return super.invokeListenerMethod(methodName, enrichedArguments, originalMessage);
    }
}
