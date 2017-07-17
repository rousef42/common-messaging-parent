/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.consumer.handler;

import com.dell.cpsd.common.rabbitmq.TestRequestMessage;
import com.dell.cpsd.common.rabbitmq.consumer.error.ErrorTransformer;
import com.dell.cpsd.common.rabbitmq.message.HasMessageProperties;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidationException;
import com.dell.cpsd.common.rabbitmq.validators.MessageValidator;
import com.dell.cpsd.common.rabbitmq.validators.ValidationResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.function.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultMessageHandlerTest
{
    DefaultMessageHandler<TestRequestMessage> handler;
    TestRequestMessage                        message;
    MessageValidator<TestRequestMessage>      validator;
    Consumer<TestRequestMessage>              messageConsumer;
    ErrorTransformer<HasMessageProperties<?>> errorTransformer;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception
    {
        message = new TestRequestMessage();

        validator = Mockito.mock(MessageValidator.class);
        messageConsumer = Mockito.mock(Consumer.class, "Test message consumer");
        errorTransformer = Mockito.mock(ErrorTransformer.class);

        handler = new DefaultMessageHandler<TestRequestMessage>(TestRequestMessage.class, validator, "handler-key", errorTransformer)
        {
            @Override
            protected void executeOperation(TestRequestMessage message) throws Exception
            {
                messageConsumer.accept(message);
            }
        };
    }

    @Test(expected = ExpectedResponseException.class)
    public void handleMessage_validationError() throws Exception
    {
        ValidationResult validationResult = new ValidationResult().addError("test validation error");

        Mockito.when(validator.validate(message)).thenReturn(validationResult);

        Mockito.when(errorTransformer.transform(any(MessageValidationException.class), any())).thenReturn(new ExpectedResponseException());

        handler.handleMessage(message);
    }

    @Test(expected = ExpectedResponseException.class)
    public void handleMessage_executionError() throws Exception
    {
        Mockito.when(validator.validate(message)).thenReturn(new ValidationResult());

        Mockito.doThrow(new TestMessageException()).when(messageConsumer).accept(message);

        Mockito.when(errorTransformer.transform(any(TestMessageException.class), any())).thenReturn(new ExpectedResponseException());

        handler.handleMessage(message);
    }

    @Test
    public void handleMessage() throws Exception
    {
        Mockito.when(validator.validate(message)).thenReturn(new ValidationResult());

        handler.handleMessage(message);

        verifyZeroInteractions(errorTransformer);
    }

    private static class TestMessageException extends RuntimeException
    {
        private static final long serialVersionUID = -629942209282233824L;
    }

    private static class ExpectedResponseException extends RuntimeException
    {
        private static final long serialVersionUID = -3341789946796916629L;
    }
}