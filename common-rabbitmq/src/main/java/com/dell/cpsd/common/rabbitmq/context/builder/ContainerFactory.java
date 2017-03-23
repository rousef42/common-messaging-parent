/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.retrypolicy.DefaultRetryPolicyFactory;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.util.ErrorHandler;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class ContainerFactory
{
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(ContainerFactory.class);

    public SimpleMessageListenerContainer createDefaultContainer(String containerName, ConnectionFactory connectionFactory,
            MessageConverter messageConverter, Object messageHandler)
    {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setAdviceChain(new Advice[] {DefaultRetryPolicyFactory.makeContainerListenerRetryPolicy()});
        container.setErrorHandler(errorHandler(containerName));
        container.setMessageConverter(messageConverter);
        container.setMessageListener(new MessageListenerAdapter(messageHandler, messageConverter));
        return container;
    }

    /**
     * The error handler for the listener container.
     *
     * @param listenerName The name of the listener.
     * @return The error handler for the listener container.
     * @since 1.0
     */
    private ErrorHandler errorHandler(final String listenerName)
    {
        return new DefaultContainerErrorHandler(listenerName);
    }
}
