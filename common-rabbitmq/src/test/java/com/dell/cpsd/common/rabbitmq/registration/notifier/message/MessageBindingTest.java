/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.message;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit testf for MessageBinding.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since x.y.z
 */
public class MessageBindingTest
{
    @Test
    public void testConstructor() throws Exception
    {
        String queueName = "my-queue-name";
        String routingKey = "my-routing-key";

        MessageBinding binding = new MessageBinding(queueName, routingKey);

        assertEquals(queueName, binding.getQueueName());
        assertEquals(routingKey, binding.getRoutingKey());
    }

}