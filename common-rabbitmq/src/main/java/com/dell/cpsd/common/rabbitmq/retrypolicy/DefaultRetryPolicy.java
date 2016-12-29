/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.retrypolicy;

import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.RetryableResponseMessageException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Default retry policy for standard exception.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultRetryPolicy extends ExceptionClassifierRetryPolicy
{
    public DefaultRetryPolicy()
    {
        Map<Class<? extends Throwable>, RetryPolicy> policyMap = createPolicyMap();

        setPolicyMap(policyMap);
    }

    protected Map<Class<? extends Throwable>, RetryPolicy> createPolicyMap()
    {
        Map<Class<? extends Throwable>, RetryPolicy> policyMap = new HashMap<>();

        policyMap.put(Exception.class, new SimpleRetryPolicy());
        policyMap.put(RetryableResponseMessageException.class, new SimpleRetryPolicy());

        policyMap.put(ClassNotFoundException.class, new NeverRetryPolicy());
        policyMap.put(IOException.class, new NeverRetryPolicy());
        policyMap.put(JsonParseException.class, new NeverRetryPolicy());
        policyMap.put(MessageConversionException.class, new NeverRetryPolicy());
        policyMap.put(MessageValidationException.class, new NeverRetryPolicy());
        policyMap.put(ResponseMessageException.class, new NeverRetryPolicy());

        return policyMap;
    }
}
