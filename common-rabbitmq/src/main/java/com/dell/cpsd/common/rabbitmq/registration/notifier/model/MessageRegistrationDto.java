/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.model;

import com.fasterxml.jackson.databind.jsonschema.JsonSchema;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
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

    public MessageRegistrationDto(String serviceName, Class<?> messageClass, String messageType, String messageVersion,
            JsonSchema messageSchema, List<MessageExchangeDto> messageExchanges)
    {
        this.registrationId = UUID.randomUUID().toString();
        this.serviceName = serviceName;
        this.messageClass = messageClass;
        this.messageType = messageType;
        this.messageVersion = messageVersion;
        this.messageSchema = messageSchema;
        this.messageExchanges = messageExchanges;
    }

    public boolean add(MessageExchangeDto messageExchangeDto)
    {
        return messageExchanges.add(messageExchangeDto);
    }

    public boolean addAll(Collection<? extends MessageExchangeDto> c)
    {
        return messageExchanges.addAll(c);
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
}
