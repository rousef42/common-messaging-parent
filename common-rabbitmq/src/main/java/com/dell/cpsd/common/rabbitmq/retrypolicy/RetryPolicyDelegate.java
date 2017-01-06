/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.retrypolicy;

import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;

/**
 * Delegates all operations to a delegate policy.
 * Base class if you need to create a wrapper around some other policy.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public abstract class RetryPolicyDelegate implements RetryPolicy
{
    private RetryPolicy delegate;

    public RetryPolicyDelegate(RetryPolicy delegate)
    {
        this.delegate = delegate;
    }

    protected RetryPolicy getDelegate()
    {
        return delegate;
    }

    @Override
    public boolean canRetry(RetryContext context)
    {
        return delegate.canRetry(context);
    }

    @Override
    public RetryContext open(RetryContext parent)
    {
        return delegate.open(parent);
    }

    @Override
    public void close(RetryContext context)
    {
        delegate.close(context);
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable)
    {
        delegate.registerThrowable(context, throwable);
    }
}
