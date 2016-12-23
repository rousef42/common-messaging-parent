/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class RabbitContext<M>
{
    private String         contextUuid;
    private RabbitAdmin    admin;
    private RabbitTemplate rabbitTemplate;

    Collection<Exchange>                 exchanges         = new ArrayList<>();
    Collection<Queue>                    queues            = new ArrayList<>();
    Collection<Binding>                  bindings          = new ArrayList<>();
    Collection<MessageListenerContainer> containers        = new ArrayList<>();
    Map<Class, MessageDescription>       descriptionLookup = new HashMap<>();

    public RabbitContext(String contextUuid, RabbitAdmin admin, RabbitTemplate rabbitTemplate, Collection<Exchange> exchanges,
            Collection<Queue> queues, Collection<Binding> bindings, Collection<MessageDescription> descriptions,
            Collection<MessageListenerContainer> containers)
    {
        this.contextUuid = contextUuid;
        this.admin = admin;
        this.rabbitTemplate = rabbitTemplate;
        this.exchanges = exchanges;
        this.queues = queues;
        this.bindings = bindings;
        this.containers = containers;

        descriptions.forEach(d -> descriptionLookup.put(d.getMessageClass(), d));
    }

    public void declare()
    {
        exchanges.forEach(exchange -> admin.declareExchange(exchange));
        queues.forEach(queue -> admin.declareQueue(queue));
        bindings.forEach(binding -> admin.declareBinding(binding));
    }

    public void start()
    {
        containers.forEach(container -> container.start());
    }

    public String getContextUuid()
    {
        return contextUuid;
    }

    public RabbitAdmin getAdmin()
    {
        return admin;
    }

    public RabbitTemplate getRabbitTemplate()
    {
        return rabbitTemplate;
    }

    public Collection<Exchange> getExchanges()
    {
        return exchanges;
    }

    public Collection<Queue> getQueues()
    {
        return queues;
    }

    public Collection<Binding> getBindings()
    {
        return bindings;
    }

    public MessageDescription getDescription(Class messageClass)
    {
        return descriptionLookup.get(messageClass);
    }

    public Collection<MessageListenerContainer> getContainers()
    {
        return containers;
    }
}
