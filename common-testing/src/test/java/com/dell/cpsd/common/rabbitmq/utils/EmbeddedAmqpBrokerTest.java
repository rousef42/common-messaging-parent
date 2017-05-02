/*
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.utils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test the embedded broker.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class EmbeddedAmqpBrokerTest
{
    public static final String PING_EXCHANGE   = "pingExchange";
    public static final String PING_QUEUE      = "pingQueue";
    public static final String ROUTING_KEY_ALL = "#";

    private static EmbeddedAmqpBroker embedded;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        embedded = EmbeddedAmqpBroker.getInstance();
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
        if (embedded != null)
        {
            embedded.close();
        }
    }

    @Test
    public void testEmbeddedBrokerStarts() throws Exception
    {
        assertNotNull(embedded.getQpidBroker());
        assertNotNull(embedded.getQpidBroker().getBroker());
    }

    @Test
    public void testCreateExchange() throws Exception
    {
        createPingExchangeAndQueue();
        final CachingConnectionFactory connFactory = new CachingConnectionFactory(embedded.getPort());
        final RabbitAdmin admin = new RabbitAdmin(connFactory);
        final Properties queueProperties = admin.getQueueProperties(PING_QUEUE);
        assertEquals(PING_QUEUE, queueProperties.get("QUEUE_NAME"));
    }

    @Test
    public void testSendPing() throws Exception
    {
        createPingExchangeAndQueue();
        final CachingConnectionFactory connFactory = new CachingConnectionFactory(embedded.getPort());
        final RabbitAdmin admin = new RabbitAdmin(connFactory);

        Properties queueProperties = admin.getQueueProperties(PING_QUEUE);
        assertEquals(0, queueProperties.get("QUEUE_MESSAGE_COUNT"));
        sendPing();
        queueProperties = admin.getQueueProperties(PING_QUEUE);
        assertEquals(1, queueProperties.get("QUEUE_MESSAGE_COUNT"));
    }

    private void createPingExchangeAndQueue()
    {
        final CachingConnectionFactory connFactory = new CachingConnectionFactory(embedded.getPort());
        final RabbitAdmin admin = new RabbitAdmin(connFactory);
        final Queue queue = new Queue(PING_QUEUE, false, false, true);
        admin.declareQueue(queue);
        final TopicExchange exchange = new TopicExchange(PING_EXCHANGE);
        admin.declareExchange(exchange);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_ALL));
        connFactory.destroy();
    }

    public void sendPing() throws Exception
    {
        final CachingConnectionFactory cf = new CachingConnectionFactory(embedded.getPort());
        final RabbitTemplate template = new RabbitTemplate(cf);
        final String message = "Ping";
        template.convertAndSend(PING_EXCHANGE, ROUTING_KEY_ALL, message);
        cf.destroy();
    }
}
