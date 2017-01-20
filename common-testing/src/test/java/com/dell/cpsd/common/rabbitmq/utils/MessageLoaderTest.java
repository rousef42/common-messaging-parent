/*
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.amqp.core.Message;

import static org.junit.Assert.*;
import static com.dell.cpsd.common.rabbitmq.utils.MessageLoader.*;

/**
 * Message loader test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class MessageLoaderTest
{
    private String pingBody = "{\n  \"messageProperties\": {\n    \"timestamp\": \"2010-01-01T12:00:00Z\",\n"
            + "    \"correlationId\": \"abcdabcd-abcd-abcd-abcd-abcdabcdabcd\",\n    \"replyTo\": \"pingsender\"\n  },\n"
            + "  \"message\": \"hey\"\n}\n";

    @Test
    public void testLoadFromResources() throws Exception
    {
        Message message = loadFromResources("/messages/ping.json", "com.dell.cpsd.ping");
        assertNotNull(message);
        assertEquals("com.dell.cpsd.ping", message.getMessageProperties().getHeaders().get(MessageLoader.TYPE_ID));
        assertEquals(pingBody, new String(message.getBody()));
    }

    @Test
    public void testLoadFromResourcesNullType() throws Exception
    {
        Message message = loadFromResources("/messages/ping.json", null);
        assertNotNull(message);
        assertNull(message.getMessageProperties().getHeaders().get(MessageLoader.TYPE_ID));
        assertEquals(pingBody, new String(message.getBody()));
    }

    @Test
    public void testRemoveAmqpToolWrapper() throws Exception
    {
        Message message = loadFromResources("/messages/ping.usage.json", "com.dell.cpsd.ping");
        assertNotNull(message);
        assertEquals("com.dell.cpsd.ping", message.getMessageProperties().getHeaders().get(MessageLoader.TYPE_ID));

        //This removes unnecessary spaces and NL
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readValue(pingBody, JsonNode.class);

        assertEquals(rootNode.toString(), new String(message.getBody()));
    }

}
