/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.contract.extension.amqp.annotation.Message;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.ReplyMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
@Message(value = "test.message.reply", version = "1.0")
@ReplyMessage
public class TestReplyMessage
{
    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("reply-to")
    private String replyTo;

    @JsonProperty("asdf")
    private String asdf;

    public TestReplyMessage(String correlationId, String replyTo, String asdf)
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
