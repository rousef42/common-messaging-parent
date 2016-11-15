/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.message;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
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
