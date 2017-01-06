/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.retrypolicy;

import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;

/**
 * Policy designed to be used with ResponseMessageException. Takes retry attempt count from exception itself.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class ResponseMessageRetryPolicy extends SimpleRetryPolicy
{
    private static final Logger log = LoggerFactory.getLogger(ResponseMessageRetryPolicy.class);

    @Override
    public boolean canRetry(RetryContext context)
    {
        Throwable t = context.getLastThrowable();
        if (t instanceof ResponseMessageException)
        {
            int maxRetryCount = ((ResponseMessageException) t).getMaxRetryCount();
            return context.getRetryCount() < maxRetryCount;
        }

        log.warn(
                "Policy is used for a wrong exception: {}. Falling back to SimpleRetryPolicy.",
                (t == null ? null : t.getClass().getSimpleName())
        );
        return super.canRetry(context);
    }
}
