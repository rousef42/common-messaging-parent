/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.registration.notifier.model;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class BindingDataDto
{
    private String queueName;
    private String routingKey;

    public BindingDataDto(String queueName, String routingKey)
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
