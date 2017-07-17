/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TUnit test for MessageRegistrationWithdrawn.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since x.y.z
 */
public class MessageRegistrationWithdrawnTest
{
    @Test
    public void constructorTest() throws Exception
    {
        String correlationId = "correlationId";
        Date timestamp = new Date(0);
        String registrationId = "registrationId";

        MessageRegistrationWithdrawn registration = new MessageRegistrationWithdrawn(correlationId, timestamp, registrationId);

        assertEquals(correlationId, registration.getCorrelationId());
        assertEquals(timestamp, registration.getTimestamp());
        assertEquals(registrationId, registration.getRegistrationId());

    }

}