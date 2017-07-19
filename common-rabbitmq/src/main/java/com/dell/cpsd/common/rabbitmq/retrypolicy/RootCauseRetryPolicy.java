/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.retrypolicy;

import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryPolicy;

/**
 * Uses root cause exception to apply retry policy.<br>
 * Note: if it's used as a root policy, only cause exception will be passed to a message recoverer.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class RootCauseRetryPolicy extends RetryPolicyDelegate
{
    public RootCauseRetryPolicy(RetryPolicy delegate)
    {
        super(delegate);
    }

    @Override
    public void registerThrowable(RetryContext context, Throwable throwable)
    {
        Throwable cause = throwable.getCause();
        if (throwable == cause)
        {
            throw new IllegalArgumentException("Exception uses references itself as a root cause", throwable);
        }
        super.registerThrowable(context, cause);
    }
}
