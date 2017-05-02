/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.retrypolicy;

import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * The is a factory for default retry templates.
 * <p>
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @since 1.1
 */
public class DefaultRetryPolicyFactory
{
    /*
     * The retry template information.
     */
    private static final int    MAX_ATTEMPTS     = 10;
    private static final int    INITIAL_INTERVAL = 100;
    private static final double MULTIPLIER       = 2.0;
    private static final int    MAX_INTERVAL     = 50000;

    /**
     * This creates the <code>RetryTemplate</code> that will be assigned to a
     * rabbit template.
     *
     * @return The default <code>RetryTemplate</code> for a rabbit template.
     * @since 1.1
     */
    public static RetryTemplate makeRabbitTemplateRetry()
    {
        final RetryTemplate retryTemplate = new RetryTemplate();

        final ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(INITIAL_INTERVAL);
        backOffPolicy.setMultiplier(MULTIPLIER);
        backOffPolicy.setMaxInterval(MAX_INTERVAL);

        final SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(MAX_ATTEMPTS);

        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    /**
     * The creates a default <code>RetryOperationsInterceptor</code> for a
     * listener container.
     *
     * @return The default <code>RetryOperationsInterceptor</code>.
     * @since 1.0
     */
    public static RetryOperationsInterceptor makeContainerListenerRetryPolicy()
    {
        final RetryPolicy retryPolicy = DefaultRetryPolicyFactory.makeExceptionClassifierRetryPolicy();

        // Delegate handler that unpacks the throwable to get the root 
        // offending exception so we can set a specific policy for it.
        final RetryPolicyExceptionUnpackerDelegate retryPolicyExceptionUnpackerDelegate = new RetryPolicyExceptionUnpackerDelegate(
                retryPolicy);

        final RetryTemplate retryTemplate = new RetryTemplate();
        final BackOffPolicy backoffPolicy = new ExponentialBackOffPolicy();
        retryTemplate.setBackOffPolicy(backoffPolicy);
        retryTemplate.setRetryPolicy(retryPolicyExceptionUnpackerDelegate);

        final RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
        interceptor.setRetryOperations(retryTemplate);

        return interceptor;
    }

    /**
     * The creates a default <code>ExceptionClassifierRetryPolicy</code> for a
     * listener container.
     *
     * @return The default <code>ExceptionClassifierRetryPolicy</code>.
     * @since 1.0
     */
    public static RetryPolicy makeExceptionClassifierRetryPolicy()
    {
        final ExceptionClassifierRetryPolicy exceptionClassifierRetryPolicy = new ExceptionClassifierRetryPolicy();

        final Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();

        policyMap.put(ClassNotFoundException.class, new NeverRetryPolicy());

        // Default max attempts is 3. After the third attempt, the message is logged.
        policyMap.put(Exception.class, new SimpleRetryPolicy());

        policyMap.put(ListenerExecutionFailedException.class, new SimpleRetryPolicy());

        policyMap.put(MessageConversionException.class, new NeverRetryPolicy());

        exceptionClassifierRetryPolicy.setPolicyMap(policyMap);

        return exceptionClassifierRetryPolicy;
    }
}
