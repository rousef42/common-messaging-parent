package com.dell.cpsd.common.rabbitmq.consumer.error;

import com.dell.cpsd.common.rabbitmq.TestErrorMessage;
import com.dell.cpsd.common.rabbitmq.TestRequestMessage;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ErrorResponseException;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseDetails;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;
import com.dell.cpsd.common.rabbitmq.validators.ValidationResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultErrorTransformerTest
{
    DefaultErrorTransformer<TestErrorMessage> transformer;
    ErrorContext<HasMessageProperties<?>> context;

    @Before
    public void setUp() throws Exception
    {
        transformer = new DefaultErrorTransformer<>("test-exchange", "test-reply", TestErrorMessage::new);

        TestRequestMessage requestMessage = new TestRequestMessage();
        requestMessage.getMessageProperties().setCorrelationId("test-id");
        requestMessage.getMessageProperties().setReplyTo("request-reply-to");
        context = new ErrorContext<>(requestMessage, "service-key");
    }

    @Test
    public void transform_ResponseMessageException() throws Exception
    {
        ResponseMessageException e = new ResponseMessageException(new RuntimeException(), "test-exchange", "test-key", "test body");

        assertSame(e, transformer.transform(e, context));
    }

    @Test
    public void transform_MessageValidationException() throws Exception
    {
        ValidationResult validationResult = new ValidationResult();
        validationResult.addError("test.validation.error");
        MessageValidationException e = new MessageValidationException(validationResult);

        ErrorResponseException result = (ErrorResponseException) transformer.transform(e, context);

        assertSame(e, result.getCause());
        assertResponseDetails("test.validation.error", result);
    }

    @Test
    public void transform_RuntimeException() throws Exception
    {
        RuntimeException e = new RuntimeException("test error");

        ErrorResponseException result = (ErrorResponseException) transformer.transform(e, context);

        assertSame(e, result.getCause());
        assertResponseDetails("test error", result);
    }

    protected void assertResponseDetails(String expectedErrorMessage, ErrorResponseException result)
    {
        ResponseDetails response = result.getResponseDetails();
        assertEquals("test-exchange", response.getExchange());
        assertEquals("service-key.error.request-reply-to", response.getRoutingKey());

        TestErrorMessage body = (TestErrorMessage) response.getBody();
        assertEquals(expectedErrorMessage, body.getErrorMessage());
        assertEquals("test-id", body.getMessageProperties().getCorrelationId());
        assertEquals("test-reply", body.getMessageProperties().getReplyTo());
        assertNotNull(body.getMessageProperties().getTimestamp());
    }
}