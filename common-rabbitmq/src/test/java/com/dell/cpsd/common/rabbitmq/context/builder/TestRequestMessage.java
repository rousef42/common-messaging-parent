/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.rabbitmq.annotation.Message;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageRequest;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
@Message(value = "test.message.request", version = "1.0")
@MessageRequest
public class TestRequestMessage
{
    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("reply-to")
    private String replyTo;

    @JsonProperty("asdf")
    private String asdf;

    public TestRequestMessage(String correlationId, String replyTo, String asdf)
    {
        this.correlationId = correlationId;
        this.replyTo = replyTo;
        this.asdf = asdf;
    }

    public String getCorrelationId()
    {
        return correlationId;
    }

    public String getReplyTo()
    {
        return replyTo;
    }

    public String getAsdf()
    {
        return asdf;
    }
}
