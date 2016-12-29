/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.retrypolicy.RetryPolicyExceptionUnpackerDelegate;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ErrorHandler;

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
public class ContainerFactory
{
    private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(ContainerFactory.class);

    public SimpleMessageListenerContainer createDefaultContainer(String containerName, ConnectionFactory connectionFactory,
            MessageConverter messageConverter, Object messageHandler)
    {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setAdviceChain(new Advice[] {retryPolicy()});
        container.setErrorHandler(errorHandler(containerName));
        container.setMessageConverter(messageConverter);
        container.setMessageListener(new MessageListenerAdapter(messageHandler, messageConverter));
        return container;
    }

    /**
     * The retry operations interceptor for the consumer.
     *
     * @return The retry operations interceptor for the consumer.
     * @since 1.0
     */
    private RetryOperationsInterceptor retryPolicy()
    {
        RetryPolicyExceptionUnpackerDelegate retryPolicyExceptionUnpackerDelegate = new RetryPolicyExceptionUnpackerDelegate(
                ourExceptionRetryPolicy());

        RetryTemplate retryTemplate = new RetryTemplate();
        BackOffPolicy backoffPolicy = new ExponentialBackOffPolicy();
        retryTemplate.setBackOffPolicy(backoffPolicy);
        retryTemplate.setRetryPolicy(retryPolicyExceptionUnpackerDelegate);

        RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
        interceptor.setRetryOperations(retryTemplate);

        return interceptor;
    }

    /*
     * The returns a exception based retry policy.
     *
     * @return  A new instance of an exception based retry policy.
     *
     * @since   1.0
     */
    private RetryPolicy ourExceptionRetryPolicy()
    {
        ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy = new ExceptionClassifierRetryPolicy();
        Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();
        policyMap.put(ClassNotFoundException.class, new NeverRetryPolicy());
        policyMap.put(Exception.class,
                new SimpleRetryPolicy()); // Default max attempts is 3. After the third attempt, the message is logged.
        policyMap.put(ListenerExecutionFailedException.class, new SimpleRetryPolicy());
        policyMap.put(MessageConversionException.class, new NeverRetryPolicy());

        exceptionClassifierRetryPolicy.setPolicyMap(policyMap);

        return exceptionClassifierRetryPolicy;
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
