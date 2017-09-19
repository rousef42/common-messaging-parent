/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.config;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import com.dell.cpsd.common.rabbitmq.consumer.handler.DefaultMessageListener;
import com.dell.cpsd.common.rabbitmq.consumer.handler.MessageHandler;

/**
 * This class registers the message handlers with the Message Listener Container.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class HandlerRegistrar
{

    @Autowired
    private SimpleMessageListenerContainer simpleMessageListenerContainer;

    @Autowired
    private MessageConverter       messageConverter;

    /**
     * Bean of type {@link DefaultMessageListener}.
     *
     * @param responseHandlers
     *            the response handlers
     * @return the default message listener
     */
    @Bean
    public DefaultMessageListener messageListener(MessageHandler<?>... responseHandlers)
    {
        return new DefaultMessageListener(messageConverter, responseHandlers);
    }

    /**
     * Method to register Message Handler of type {@link AbstractDefaultMessageHandler} with the listener
     * 
     * @param responseHandlers
     *            - Message Handler to be registered wit the listener
     */
    public void register(MessageHandler<?>... responseHandlers)
    {
        simpleMessageListenerContainer.setMessageListener(messageListener(responseHandlers));
    }

}
