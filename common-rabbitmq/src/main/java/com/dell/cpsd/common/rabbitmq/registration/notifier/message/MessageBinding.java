/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class MessageBinding
{
    @JsonProperty("queueName")
    private String queueName;

    @JsonProperty("routingKey")
    private String routingKey;

    public MessageBinding(String queueName, String routingKey)
    {
        this.queueName = queueName;
        this.routingKey = routingKey;
    }

    public String getQueueName()
    {
        return queueName;
    }

    public String getRoutingKey()
    {
        return routingKey;
    }
}
