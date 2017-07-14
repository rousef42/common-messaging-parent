/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.MessageConverter;

import com.dell.cpsd.common.rabbitmq.TestRequestMessage;
import com.rabbitmq.client.Channel;

/**
 * DefaultMessageListener Test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultMessageListenerTest
{
    @Rule
    public ExpectedException                  thrown = ExpectedException.none();

    DefaultMessageHandler<TestRequestMessage> handler;
    TestRequestMessage                        handlerMessage;

    MessageConverter                          converter;
    DefaultMessageListener                    messageListener;
    Channel                                   channel;
    Message                                   message;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception
    {
        message = Mockito.mock(Message.class);
        handlerMessage = new TestRequestMessage();

        converter = Mockito.mock(MessageConverter.class);

        handler = (DefaultMessageHandler<TestRequestMessage>) Mockito.mock(DefaultMessageHandler.class);

        messageListener = new DefaultMessageListener(converter, handler);
        channel = Mockito.mock(Channel.class);
    }

    @Test
    public void testOnMessageNoHandler() throws Exception
    {
        thrown.expect(RuntimeException.class);
        messageListener.onMessage(message, channel);
    }

    @Test
    public void testOnMessage() throws Exception
    {

        doReturn(handlerMessage).when(converter).fromMessage(message);

        doReturn(true).when(handler).canHandle(message, handlerMessage);
        messageListener.onMessage(message, channel);

        verify(handler).canHandle(message, handlerMessage);
        verify(handler).handleMessage(handlerMessage);
    }

}
