/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * Adaptor between Spring AMQP listener and MessageHandler implementations.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultMessageListener implements ChannelAwareMessageListener
{
    protected MessageConverter converter;
    protected List<MessageHandler<?>> handlers = new ArrayList<>();

    public DefaultMessageListener(MessageConverter converter, MessageHandler<?>... handlers)
    {
        this.converter = converter;
        this.handlers.addAll(asList(handlers));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onMessage(Message message, Channel channel) throws Exception
    {
        Object bean = converter.fromMessage(message);
        MessageHandler handler = findHandler(message, bean);
        if (handler == null)
        {
            throw new RuntimeException("Failed to find message handler for " + bean);
        }
        handler.handleMessage(bean);
    }

    protected MessageHandler<?> findHandler(Message message, Object bean)
    {
        for (MessageHandler<?> handler : handlers)
        {
            if (handler.canHandle(message, bean))
            {
                return handler;
            }
        }
        return null;
    }
}
