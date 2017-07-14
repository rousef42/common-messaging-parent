/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.amqp.core.Message;

import com.dell.cpsd.common.rabbitmq.context.RabbitContext;

/**
 * DelegatingMessageHandler Test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DelegatingMessageHandlerTest
{

    @Mock
    RabbitContext            rabbitContext;
    @SuppressWarnings("rawtypes")
    @Spy
    GenericMessageHandler    genericMessagehandler;
    DelegatingMessageHandler delegateHanlder;
    @Mock
    Message                  message;

    @Before
    public void setUp() throws Exception
    {
        message = Mockito.mock(Message.class);

        genericMessagehandler = Mockito.mock(GenericMessageHandler.class);

        delegateHanlder = new DelegatingMessageHandler();

        genericMessagehandler = new GenericMessageHandler<Object, Throwable>()
        {

            @Override
            protected void executeOperation(Object requestMessage) throws Throwable
            {
                // TODO Auto-generated method stub

            }

            @Override
            protected Throwable convertException(Throwable t)
            {
                // TODO Auto-generated method stub
                return null;
            }

        };

    }

    @Test
    public void testHandleMessage()
    {

        try
        {
            delegateHanlder.addHandler(message.getClass(), genericMessagehandler);

            delegateHanlder.handleMessage(message);

        }
        catch (Throwable t)
        {
            fail("Should have been no exception in DelegatingMessageHandler handleMessage Test");
        }
    }

}
