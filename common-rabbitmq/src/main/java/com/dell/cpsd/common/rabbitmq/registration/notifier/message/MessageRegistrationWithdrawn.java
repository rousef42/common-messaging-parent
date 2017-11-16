/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.message;

import java.util.Date;

import com.dell.cpsd.contract.extension.amqp.annotation.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
@Message(value = "common.message.registration.withdrawn", version = "1.0")
public class MessageRegistrationWithdrawn
{
    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("timestamp")
    private Date timestamp;

    @JsonProperty("registrationId")
    private String registrationId;

    public MessageRegistrationWithdrawn(String correlationId, Date timestamp, String registrationId)
    {
        this.correlationId = correlationId;
        this.timestamp = timestamp;
        this.registrationId = registrationId;
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
}
