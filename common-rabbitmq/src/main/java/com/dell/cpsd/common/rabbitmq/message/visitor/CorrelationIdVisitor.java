
package com.dell.cpsd.common.rabbitmq.message.visitor;

import com.dell.cpsd.contract.extension.amqp.message.EventMessage;
import com.dell.cpsd.contract.extension.amqp.message.RequestMessage;
import com.dell.cpsd.contract.extension.amqp.message.ResponseMessage;
import com.dell.cpsd.contract.extension.amqp.visitor.MessageVisitor;

public class CorrelationIdVisitor implements MessageVisitor
{

    @Override
    public String visit(RequestMessage message)
    {
        return message.getMessageProperties() == null ? null : message.getMessageProperties().getCorrelationId();
    }

    @Override
    public String visit(ResponseMessage message)
    {
        return message.getMessageProperties() == null ? null : message.getMessageProperties().getCorrelationId();
    }

    @Override
    public String visit(EventMessage message)
    {
        return message.getMessageProperties() == null ? null : message.getMessageProperties().getCorrelationId();
    }
}
