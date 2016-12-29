/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.retrypolicy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.amqp.rabbit.config.StatefulRetryOperationsInterceptorFactoryBean;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
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
}
