/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.rabbitmq.annotation.Message;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageConsumer;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageExchange;
import com.dell.cpsd.common.rabbitmq.annotation.opinions.MessageProducer;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageReply;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@Message(value = "test.message.consumer", version = "1.0")
@MessageExchange(exchange = "exchange.test")
@MessageConsumer(routingKey = "binding.base")
@MessageProducer(routingKey = "routing.base")
@MessageReply
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
