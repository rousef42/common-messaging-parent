/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.template;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.net.UnknownHostException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.dell.cpsd.common.rabbitmq.context.MessageDescription;
import com.dell.cpsd.common.rabbitmq.context.RabbitContext;

/**
 * ContainerIdHelper Test.
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information.
 * <p/>
 *
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class OpinionatedRabbitTemplateTest
{
    OpinionatedRabbitTemplate opinionatedRabbitTemplate = new OpinionatedRabbitTemplate();
    @Mock
    RabbitContext             rabbitContext             = Mockito.mock(RabbitContext.class);
    @Mock
    RabbitTemplate            rabbitTemplate            = Mockito.mock(RabbitTemplate.class);

    private static String     DUMMY_EXCHANGE            = "dummyExchange";
    private static String     DUMMY_ROUTING             = "dummyRoutingKey";

    @Test
    public void testSendMessage() throws UnknownHostException
    {
        MessageDescription description = new MessageDescription();
        description.setExchange(DUMMY_EXCHANGE);
        description.setRoutingKey(DUMMY_ROUTING);
        Object obj = new Object();
        doReturn(description).when(rabbitContext).getDescription(obj.getClass());
        doReturn(rabbitTemplate).when(rabbitContext).getRabbitTemplate();
        opinionatedRabbitTemplate.setRabbitContext(rabbitContext);

        opinionatedRabbitTemplate.send(obj);
        verify(rabbitContext).getDescription(obj.getClass());
        verify(rabbitTemplate).convertAndSend(DUMMY_EXCHANGE, DUMMY_ROUTING, obj);

    }

    @Test
    public void testSendMessageRoutingKey() throws UnknownHostException
    {
        MessageDescription description = new MessageDescription();
        description.setExchange(DUMMY_EXCHANGE);

        Object obj = new Object();
        doReturn(description).when(rabbitContext).getDescription(obj.getClass());
        doReturn(rabbitTemplate).when(rabbitContext).getRabbitTemplate();

        opinionatedRabbitTemplate.setRabbitContext(rabbitContext);
        opinionatedRabbitTemplate.send(obj, DUMMY_ROUTING);

        verify(rabbitContext).getDescription(obj.getClass());
        verify(rabbitTemplate).convertAndSend(DUMMY_EXCHANGE, DUMMY_ROUTING, obj);

    }
}
