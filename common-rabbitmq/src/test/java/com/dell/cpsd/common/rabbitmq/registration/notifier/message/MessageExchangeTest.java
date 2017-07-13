/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.message;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for MessageExchange.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since x.y.z
 */
public class MessageExchangeTest
{
    @Test
    public void testConstructor() throws Exception
    {
        String name = "my-name";
        String direction = "my-direction";
        String queueName = "my-queue-name";
        String routingKey = "my-routing-key";

        List<MessageBinding> bindings = Arrays.asList(new MessageBinding(queueName, routingKey));
        MessageExchange exchange = new MessageExchange (name, direction, bindings);

        assertEquals(name, exchange.getName());
        assertEquals(direction, exchange.getDirection());
        assertArrayEquals(bindings.toArray(), exchange.getBindings().toArray());
    }

}