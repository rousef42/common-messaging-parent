/*
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.retrypolicy;

import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ErrorResponseException;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;
import com.dell.cpsd.common.rabbitmq.validators.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.retry.RetryContext;

import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Default retry policy test.
 * <p>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultRetryPolicyTest
{
    DefaultRetryPolicy policy;

    @Before
    public void setUp() throws Exception
    {
        policy = new DefaultRetryPolicy();
    }

    @Test
    public void registerThrowable_MessageConversionException() throws Exception
    {
        Exception e = new MessageConversionException("Test error");

        assertAttemptsCount(1, e);
    }

    @Test
    public void registerThrowable_MessageValidationException() throws Exception
    {
        Exception e = new MessageValidationException(
                new ValidationResult()
                        .addError("Test validation error")
        );

        assertAttemptsCount(1, e);
    }

    @Test
    public void registerThrowable_RuntimeException() throws Exception
    {
        Exception e = new RuntimeException("Test error");

        assertAttemptsCount(3, e);
    }

    @Test
    public void registerThrowable_ListenerExecutionFailedException() throws Exception
    {
        Exception e;

        e = new ListenerExecutionFailedException("Wrapper", new RuntimeException("Test error"));
        assertAttemptsCount(3, e);

        e = new ListenerExecutionFailedException("Wrapper", new MessageConversionException("Test error"));
        assertAttemptsCount(1, e);
    }

    @Test
    public void registerThrowable_ErrorResponseException() throws Exception
    {
        Exception e;

        e = new ErrorResponseException(new RuntimeException("Test error"), "exchange", "key", "body");
        assertAttemptsCount(3, e);

        e = new ErrorResponseException(new MessageConversionException("Test error"), "exchange", "key", "body");
        assertAttemptsCount(1, e);
    }

    @Test
    public void registerThrowable_ResponseMessageException() throws Exception
    {
        Exception e;

        e = new ResponseMessageException(null, "exchange", "key", "body");
        assertAttemptsCount(1, e);

        e = new ResponseMessageException(null, 10, "exchange", "key", "body");
        assertAttemptsCount(10, e);
    }

    private void assertAttemptsCount(int expectedRetries, Exception e)
    {
        RetryContext context = policy.open(null);

        IntStream.range(0, expectedRetries).forEachOrdered(i ->
        {
            assertTrue("Retry " + i, policy.canRetry(context));
            policy.registerThrowable(context, e);
        });

        assertFalse("All allowed retries are used", policy.canRetry(context));
        assertSame("Original exception is returned for message recoverer", e, context.getLastThrowable());
    }
}