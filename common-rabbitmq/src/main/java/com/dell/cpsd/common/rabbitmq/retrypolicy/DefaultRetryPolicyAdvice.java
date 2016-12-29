/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.retrypolicy;

import com.dell.cpsd.common.logging.ILogger;
import com.dell.cpsd.common.rabbitmq.exceptions.AmqpExceptionUnwrapTrait;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQLoggingManager;
import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.rabbit.config.StatefulRetryOperationsInterceptorFactoryBean;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.support.RetryTemplate;

/**
 * Interceptor for default retry policy
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultRetryPolicyAdvice implements MethodInterceptor
{
    protected final StatefulRetryOperationsInterceptor delegate;

    public DefaultRetryPolicyAdvice(MessageRecoverer messageRecoverer)
    {
        this(messageRecoverer, new DefaultRetryPolicy());
    }

    public DefaultRetryPolicyAdvice(MessageRecoverer messageRecoverer, RetryPolicy retryPolicy)
    {
        // Delegate handler that unpacks the throwable to get the root offending exception so we can set a specific policy for it.
        RetryPolicyExceptionUnpackerDelegate retryPolicyWrapper = new RetryPolicyExceptionUnpackerDelegate(retryPolicy);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(createBackOffPolicy());
        retryTemplate.setRetryPolicy(retryPolicyWrapper);
        retryTemplate.registerListener(new RetryErrorListener());

        StatefulRetryOperationsInterceptorFactoryBean factory = new StatefulRetryOperationsInterceptorFactoryBean();
        factory.setRetryOperations(retryTemplate);
        factory.setMessageKeyGenerator(new DefaultMessageKeyGenerator());
        factory.setMessageRecoverer(messageRecoverer);

        this.delegate = factory.getObject();
    }

    protected ExponentialBackOffPolicy createBackOffPolicy()
    {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setMultiplier(5.0D);
        return backOffPolicy;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable
    {
        return delegate.invoke(invocation);
    }

    protected static class RetryErrorListener extends RetryListenerSupport implements AmqpExceptionUnwrapTrait
    {
        private static final ILogger LOGGER = RabbitMQLoggingManager.getLogger(RetryErrorListener.class);

        @Override
        public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable cause)
        {
            // Log only attempt and error. DefaultContainerErrorHandler will log details.
            Integer attempt = context == null ? null : (context.getRetryCount() + 1);
            cause = unwrap(cause);
            String message = RabbitMQMessageCode.AMQP_ERROR_RETRY_E.getMessageText(attempt, cause.getMessage());
            LOGGER.error(message);
        }
    }
}
