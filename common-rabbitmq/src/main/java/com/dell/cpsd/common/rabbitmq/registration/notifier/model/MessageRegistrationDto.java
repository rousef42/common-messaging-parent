/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.model;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.jsonschema.JsonSchema;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class MessageRegistrationDto
{
    private String                   registrationId;
    private String                   serviceName;
    private Class<?>                 messageClass;
    private String                   messageType;
    private String                   messageVersion;
    private JsonSchema               messageSchema;
    private List<MessageExchangeDto> messageExchanges;
    private List<MessageQueueDto>    messageQueues;

    public MessageRegistrationDto(String serviceName, Class<?> messageClass, String messageType, String messageVersion,
            JsonSchema messageSchema, List<MessageExchangeDto> messageExchanges, List<MessageQueueDto> messageQueues)
    {
        this.registrationId = UUID.randomUUID().toString();
        this.serviceName = serviceName;
        this.messageClass = messageClass;
        this.messageType = messageType;
        this.messageVersion = messageVersion;
        this.messageSchema = messageSchema;
        this.messageExchanges = messageExchanges;
        this.messageQueues = messageQueues;
    }

    public String getRegistrationId()
    {
        return registrationId;
    }

    public String getServiceName()
    {
        return serviceName;
    }

    public Class<?> getMessageClass()
    {
        return messageClass;
    }

    public String getMessageType()
    {
        return messageType;
    }

    public String getMessageVersion()
    {
        return messageVersion;
    }

    public JsonSchema getMessageSchema()
    {
        return messageSchema;
    }

    public List<MessageExchangeDto> getMessageExchanges()
    {
        return messageExchanges;
    }

    public List<MessageQueueDto> getMessageQueues()
    {
        return messageQueues;
    }
}
