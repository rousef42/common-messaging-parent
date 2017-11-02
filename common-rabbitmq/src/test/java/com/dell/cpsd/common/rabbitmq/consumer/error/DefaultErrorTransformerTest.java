/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.error;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.dell.cpsd.common.rabbitmq.Error;
import com.dell.cpsd.common.rabbitmq.TestErrorMessage;
import com.dell.cpsd.common.rabbitmq.TestRequestMessage;
import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedError;
import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedErrorsProvider;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ErrorResponseException;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseDetails;
import com.dell.cpsd.common.rabbitmq.retrypolicy.exception.ResponseMessageException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;
import com.dell.cpsd.common.rabbitmq.validators.ValidationResult;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
@SuppressWarnings("SameParameterValue")
public class DefaultErrorTransformerTest
{
    DefaultErrorTransformer<Error, TestErrorMessage> transformer;
    TestRequestMessage                               requestMessage;
    ErrorContext<HasMessageProperties<?>>            context;

    @Before
    public void setUp() throws Exception
    {
        transformer = new DefaultErrorTransformer<>("test-exchange", "test-reply", TestErrorMessage::new, Error::new);

        requestMessage = new TestRequestMessage();
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
        validationResult.addError(error("TEST", "Localized error"));
        MessageValidationException e = new MessageValidationException(validationResult);

        ErrorResponseException result = (ErrorResponseException) transformer.transform(e, context);

        assertSame(e, result.getCause());
        assertResponseDetails("Localized error", result);
    }

    @Test
    public void transform_LocalizedException() throws Exception
    {
        Exception e = new LocalizedTestException(error("TEST", "Localized error"));

        ErrorResponseException result = (ErrorResponseException) transformer.transform(e, context);

        assertSame(e, result.getCause());
        assertResponseDetails("Localized error", result);
    }

    @Test
    public void transform_RuntimeException() throws Exception
    {
        RuntimeException e = new RuntimeException("test error");

        ErrorResponseException result = (ErrorResponseException) transformer.transform(e, context);

        assertSame(e, result.getCause());
        assertResponseDetails("VAMQP1012E Request failed with unexpected error: test error", result);
    }

    @Test
    public void transform_RuntimeException_noMessageProperties() throws Exception
    {
        requestMessage.setMessageProperties(null);
        RuntimeException e = new RuntimeException("test error");

        Exception result = transformer.transform(e, context);

        assertSame(e, result);
    }

    @Test
    public void transform_RuntimeException_noCorrelationId() throws Exception
    {
        requestMessage.getMessageProperties().setCorrelationId(null);
        RuntimeException e = new RuntimeException("test error");

        Exception result = transformer.transform(e, context);

        assertSame(e, result);
    }

    @Test
    public void transform_RuntimeException_noReplyTo() throws Exception
    {
        requestMessage.getMessageProperties().setReplyTo(null);
        RuntimeException e = new RuntimeException("test error");

        Exception result = transformer.transform(e, context);

        assertSame(e, result);
    }

    private LocalizedError error(String code, String message)
    {
        return new LocalizedError().setMessageCode(code).setMessage(message);
    }

    protected void assertResponseDetails(String expectedErrorMessage, ErrorResponseException result)
    {
        ResponseDetails response = result.getResponseDetails();
        assertEquals("test-exchange", response.getExchange());
        assertEquals("service-key.error.request-reply-to", response.getRoutingKey());

        TestErrorMessage body = (TestErrorMessage) response.getBody();
        assertEquals(1, body.getErrors().size());
        assertEquals(expectedErrorMessage, body.getErrors().get(0).getMessage());
        assertEquals("test-id", body.getMessageProperties().getCorrelationId());
        assertEquals("test-reply", body.getMessageProperties().getReplyTo());
        assertNotNull(body.getMessageProperties().getTimestamp());
    }

    private static class LocalizedTestException extends Exception implements LocalizedErrorsProvider
    {
        private static final long serialVersionUID = 7387413184662127425L;

        private LocalizedError error;

        public LocalizedTestException(LocalizedError error)
        {
            this.error = error;
        }

        @Override
        public List<LocalizedError> getLocalizedErrors()
        {
            return asList(error);
        }
    }
}