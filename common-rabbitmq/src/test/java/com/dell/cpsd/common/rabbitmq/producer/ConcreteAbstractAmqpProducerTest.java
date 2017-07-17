package com.dell.cpsd.common.rabbitmq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class ConcreteAbstractAmqpProducerTest extends AbstractAmqpMessageProducer
{

    public ConcreteAbstractAmqpProducerTest(RabbitTemplate rabbitTemplate, String hostname)
    {
        super(rabbitTemplate, hostname);
       
    }

}
