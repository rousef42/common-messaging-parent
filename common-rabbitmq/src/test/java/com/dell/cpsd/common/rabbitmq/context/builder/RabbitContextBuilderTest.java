/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.rabbitmq.connectors.RabbitMQConnectionFactory;
import com.dell.cpsd.common.rabbitmq.context.ApplicationConfiguration;
import com.dell.cpsd.common.rabbitmq.context.ApplicationConfigurationFactory;
import com.dell.cpsd.common.rabbitmq.context.RabbitContext;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;

import java.util.Arrays;
import java.util.Collection;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public class RabbitContextBuilderTest
{
    @Test
    public void testConsumeRequest()
    {
        RabbitContextBuilder builder = new RabbitContextBuilder(new RabbitMQConnectionFactory(), appConfig("appXXX"),
                Arrays.asList(new MessageMetaData("test.message.request", "asdf", null, "routing.base")));
        builder.consumes("queue1", false, new Object(), TestRequestMessage.class);
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
        Assert.assertEquals("routing.base", binding.getRoutingKey());
    }

    @Test
    public void testProduceRequest()
    {
        RabbitContextBuilder builder = new RabbitContextBuilder(new RabbitMQConnectionFactory(), appConfig("appXXX"),
                Arrays.asList(new MessageMetaData("test.message.request", "asdf", null, "routing.base")));
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
        RabbitContextBuilder builder = new RabbitContextBuilder(new RabbitMQConnectionFactory(), appConfig("appXXX"),
                Arrays.asList(new MessageMetaData("test.message.reply", "asdf", null, "routing.base")));
        builder.consumes("queue1", false, new Object(), TestReplyMessage.class);
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
        Assert.assertTrue(binding.getRoutingKey().startsWith("routing.base."));
        Assert.assertTrue(binding.getRoutingKey().contains("appXXX"));
    }

    @Test
    public void testProduceReply()
    {
        RabbitContextBuilder builder = new RabbitContextBuilder(new RabbitMQConnectionFactory(), appConfig("appXXX"),
                Arrays.asList(new MessageMetaData("test.message.reply", "asdf", null, "routing.base")));
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

    @Test
    public void testRequestAndReply()
    {
        RabbitContextBuilder builder = new RabbitContextBuilder(new RabbitMQConnectionFactory(), appConfig("appXXX"),
                Arrays.asList(new MessageMetaData("test.message.request", "asdf", null, "routing.base"),
                        new MessageMetaData("test.message.reply", "asdf", null, "routing.base")));

        builder.requestsAndReplies(TestRequestMessage.class, "requestReply", false, new Object(), TestReplyMessage.class);
        RabbitContext context = builder.build();

        Collection<Exchange> exchanges = context.getExchanges();
        Collection<Queue> queues = context.getQueues();
        Collection<Binding> bindings = context.getBindings();

        Assert.assertEquals(1, exchanges.size());
        Assert.assertEquals(1, queues.size());
        Assert.assertEquals(1, bindings.size());
    }

    private ApplicationConfiguration appConfig(String name)
    {
        return ApplicationConfigurationFactory.getInstance().createApplicationConfiguration(name);
    }
}
