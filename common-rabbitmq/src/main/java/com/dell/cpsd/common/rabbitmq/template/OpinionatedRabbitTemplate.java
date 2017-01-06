/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.template;

import com.dell.cpsd.common.rabbitmq.context.MessageDescription;
import com.dell.cpsd.common.rabbitmq.context.RabbitContext;
import com.dell.cpsd.common.rabbitmq.context.RabbitContextAware;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * This OpinionatedRabbitTemplate can be used to send messages based on the opinions
 * of the RabbitContext
 *
 * @since SINCE-TBD
 */
public class OpinionatedRabbitTemplate implements RabbitContextAware
{
    private RabbitContext  rabbitContext;
    private RabbitTemplate rabbitTemplate;

    /**
     * Sends a message to the exchange using the routingKey defined by the RabbitContext
     *
     * @param message
     */
    public void send(Object message)
    {
        MessageDescription description = rabbitContext.getDescription(message.getClass());
        rabbitTemplate.convertAndSend(description.getExchange(), description.getRoutingKey(), message);
    }

    /**
     * Sends a message to the exchange defined by the RabbitContext
     *
     * @param message
     * @param routingKey
     */
    public void send(Object message, String routingKey)
    {
        MessageDescription description = rabbitContext.getDescription(message.getClass());
        rabbitTemplate.convertAndSend(description.getExchange(), routingKey, message);
    }

    @Override
    public void setRabbitContext(RabbitContext rabbitContext)
    {
        this.rabbitContext = rabbitContext;
        this.rabbitTemplate = rabbitContext.getRabbitTemplate();
    }
}
