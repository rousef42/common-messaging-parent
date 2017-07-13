/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for MessageRegistrationNotified.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since x.y.z
 */
public class MessageRegistrationNotifiedTest
{
    @Test
    public void constructorTest() throws Exception
    {
        String correlationId = "correlationId";
        Date timestamp = new Date(0);
        String registrationId = "registrationId";
        String serviceName = "serviceName";
        String messageType = "messageType";
        String messageVersion = "messageVersion";
        List<MessageExchange> messageExchanges = Arrays.asList(new MessageExchange("name", "direction", null));

        ObjectMapper mapper = new ObjectMapper();
        JsonSchema schema = mapper.generateJsonSchema(MessageBinding.class);

        MessageRegistrationNotified registration = new MessageRegistrationNotified(correlationId, timestamp, registrationId, serviceName, messageType, messageVersion, schema, messageExchanges);

        assertEquals(correlationId, registration.getCorrelationId());
        assertEquals(timestamp, registration.getTimestamp());
        assertEquals(registrationId, registration.getRegistrationId());
        assertEquals(serviceName, registration.getServiceName());
        assertEquals(messageType, registration.getMessageType());
        assertEquals(messageVersion, registration.getMessageVersion());
        assertEquals(schema, registration.getSchema());
        assertArrayEquals(messageExchanges.toArray(), registration.getMessageExchanges().toArray());
    }

}