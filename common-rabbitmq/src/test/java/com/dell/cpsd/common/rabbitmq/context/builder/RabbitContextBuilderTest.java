/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.rabbitmq.connectors.RabbitMQConnectionFactory;
import com.dell.cpsd.common.rabbitmq.context.RabbitContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;

import java.util.Collection;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class RabbitContextBuilderTest
{
    @Test
    public void testConsumeRequest()
    {
        RabbitContextBuilder builder = new RabbitContextBuilder(new RabbitMQConnectionFactory(), "consumerPostfix");
        builder.consumes(TestRequestMessage.class, "queue1", false, new Object());
        RabbitContext context = builder.build();

        Assert.assertNotNull(context.getAdmin());
        Assert.assertNotNull(context.getRabbitTemplate());
        Assert.assertNotNull(context.getRabbitTemplate().getMessageConverter());
        Assert.assertNotNull(context.getContextUuid());

        Collection<Exchange> exchanges = context.getExchanges();
        Collection<Queue> queues = context.getQueues();
        Collection<Binding> bindings = context.getBindings();

        Assert.assertEquals(1, exchanges.size());
        Assert.assertEquals(1, queues.size());
        Assert.assertEquals(1, bindings.size());

        Binding binding = bindings.stream().findAny().get();
        Assert.assertEquals("binding.base", binding.getRoutingKey());
    }

    @Test
    public void testProduceRequest()
    {
        RabbitContextBuilder builder = new RabbitContextBuilder(new RabbitMQConnectionFactory(), "consumerPostfix");
        builder.produces(TestRequestMessage.class);
        RabbitContext context = builder.build();

        Assert.assertNotNull(context.getAdmin());
        Assert.assertNotNull(context.getRabbitTemplate());
        Assert.assertNotNull(context.getRabbitTemplate().getMessageConverter());
        Assert.assertNotNull(context.getContextUuid());

        Collection<Exchange> exchanges = context.getExchanges();
        Collection<Queue> queues = context.getQueues();
        Collection<Binding> bindings = context.getBindings();

        Assert.assertEquals(1, exchanges.size());
        Assert.assertEquals(0, queues.size());
        Assert.assertEquals(0, bindings.size());
    }

    @Test
    public void testConsumeReply()
    {
        RabbitContextBuilder builder = new RabbitContextBuilder(new RabbitMQConnectionFactory(), "consumerPostfix");
        builder.consumes(TestReplyMessage.class, "queue1", false, new Object());
        RabbitContext context = builder.build();

        Assert.assertNotNull(context.getAdmin());
        Assert.assertNotNull(context.getRabbitTemplate());
        Assert.assertNotNull(context.getRabbitTemplate().getMessageConverter());
        Assert.assertNotNull(context.getContextUuid());

        Collection<Exchange> exchanges = context.getExchanges();
        Collection<Queue> queues = context.getQueues();
        Collection<Binding> bindings = context.getBindings();

        Assert.assertEquals(1, exchanges.size());
        Assert.assertEquals(1, queues.size());
        Assert.assertEquals(1, bindings.size());

        Binding binding = bindings.stream().findAny().get();
        Assert.assertTrue(binding.getRoutingKey().startsWith("binding.base."));
        Assert.assertTrue(binding.getRoutingKey().endsWith("consumerPostfix"));
    }

    @Test
    public void testProduceReply()
    {
        RabbitContextBuilder builder = new RabbitContextBuilder(new RabbitMQConnectionFactory(), "consumerPostfix");
        builder.produces(TestReplyMessage.class);
        RabbitContext context = builder.build();

        Assert.assertNotNull(context.getAdmin());
        Assert.assertNotNull(context.getRabbitTemplate());
        Assert.assertNotNull(context.getRabbitTemplate().getMessageConverter());
        Assert.assertNotNull(context.getContextUuid());

        Collection<Exchange> exchanges = context.getExchanges();
        Collection<Queue> queues = context.getQueues();
        Collection<Binding> bindings = context.getBindings();

        Assert.assertEquals(1, exchanges.size());
        Assert.assertEquals(0, queues.size());
        Assert.assertEquals(0, bindings.size());
    }
}
