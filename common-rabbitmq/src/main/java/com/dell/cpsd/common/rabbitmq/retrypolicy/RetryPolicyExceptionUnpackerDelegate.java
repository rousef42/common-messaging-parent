/**
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.retrypolicy;

import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;

import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;

/**
 * Class acts as a delegate for <code>ExceptionClassifierRetryPolicy</code> to 
 * unpack the exceptions thrown.
 * 
 * <p/>
 * Copyright © 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * 
 * @version 1.0
 *
 * @since   SINCE-TDB
 */
public class RetryPolicyExceptionUnpackerDelegate implements RetryPolicy
{
    /*
     * The retry policy.
     */
    private final RetryPolicy delegate;
    

    /**
     * RetryPolicyExceptionUnpackerDelegate constructor.
     *
     * @param   delegate    The delegate to use.
     * 
     * @since   SINCE-TDB
     */
    public RetryPolicyExceptionUnpackerDelegate(RetryPolicy delegate)
    {
        this.delegate = delegate;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canRetry(RetryContext context) 
    {
        return delegate.canRetry(context);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public RetryContext open(RetryContext parent) 
    {
        return delegate.open(parent);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void close(RetryContext context) 
    {
        delegate.close(context);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerThrowable(RetryContext context, Throwable throwable) 
    {

        if (throwable instanceof ListenerExecutionFailedException)
        {
            delegate.registerThrowable(context, throwable.getCause());
        } 
        else 
        {
            delegate.registerThrowable(context, throwable);
        }
    }
}
