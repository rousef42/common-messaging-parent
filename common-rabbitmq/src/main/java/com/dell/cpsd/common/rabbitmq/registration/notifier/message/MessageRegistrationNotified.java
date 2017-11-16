/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.message;

import java.util.Date;
import java.util.List;

import com.dell.cpsd.contract.extension.amqp.annotation.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@Message(value = "common.message.registration.notified", version = "1.0")
@JsonPropertyOrder({"correlationId", "timestamp", "registrationId", "serviceName", "messageType", "messageVersion", "messageExchanges",
        "messageSchema"})
public class MessageRegistrationNotified
{
    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("timestamp")
    private Date timestamp;

    @JsonProperty("registrationId")
    private String registrationId;

    @JsonProperty("serviceName")
    private String serviceName;

    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("messageVersion")
    private String messageVersion;

    @JsonProperty("messageExchanges")
    private List<MessageExchange> messageExchanges;

    @JsonProperty("messageSchema")
    private JsonSchema schema;

    public MessageRegistrationNotified(String correlationId, Date timestamp, String registrationId, String serviceName, String messageType,
            String messageVersion, JsonSchema schema, List<MessageExchange> messageExchanges)
    {
        this.correlationId = correlationId;
        this.timestamp = timestamp;
        this.registrationId = registrationId;
        this.serviceName = serviceName;
        this.messageType = messageType;
        this.messageVersion = messageVersion;
        this.schema = schema;
        this.messageExchanges = messageExchanges;
    }

    public String getCorrelationId()
    {
        return correlationId;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public String getRegistrationId()
    {
        return registrationId;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public String getMessageVersion()
    {
        return messageVersion;
    }

    public JsonSchema getSchema()
    {
        return schema;
    }

    public List<MessageExchange> getMessageExchanges()
    {
        return messageExchanges;
    }
}
