/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.retrypolicy;

import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseDetails;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;

/**
 * Default logic to handle exception after all retry attempts failed.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultMessageRecoverer implements MessageRecoverer
{
    private static final Logger log = LoggerFactory.getLogger(DefaultMessageRecoverer.class);

    private RabbitTemplate rabbitTemplate;

    public DefaultMessageRecoverer(RabbitTemplate rabbitTemplate)
    {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void recover(Message message, Throwable cause)
    {
        log.debug("Message {} has failed all retries. Last exception was {}", message, cause);

        if (cause instanceof ResponseMessageException)
        {
            recoverFromResponseMessageException(message, (ResponseMessageException) cause);
            return;
        }

        log.error("Message {} has failed with no recovery details {}", message, cause);
    }

    protected void recoverFromResponseMessageException(Message message, ResponseMessageException exception)
    {
        ResponseDetails responseDetails = exception.getResponseDetails();
        if (responseDetails == null)
        {
            log.info("Exception {} has no response details populated", exception);
            return;
        }
        rabbitTemplate.convertAndSend(responseDetails.getExchange(), responseDetails.getRoutingKey(), responseDetails.getBody());
    }
}
