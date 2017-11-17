/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.validators;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.dell.cpsd.common.rabbitmq.TestMessageProperties;
import com.dell.cpsd.common.rabbitmq.TestRequestMessage;

/**
 * Unit tests for RequestMessageValidator.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.3.0
 */
public class RequestMessageValidatorTest
{
    RequestMessageValidator<TestRequestMessage> validator;

    @Before
    public void setUp() throws Exception
    {
        validator = new RequestMessageValidator<TestRequestMessage>();
    }

    @Test
    public void testValidateMessage() throws Exception
    {
        TestMessageProperties properties = new TestMessageProperties();
        properties.setCorrelationId("correlationId-1");
        properties.setReplyTo("reply-to");
        TestRequestMessage message = new TestRequestMessage();
        message.setMessageProperties(properties);
        ValidationResult result = new ValidationResult();

        validator.validateMessage(message, result);

        assertTrue(result.isValid());
    }

    @Test
    public void testValidateMessageNoCorrelationId() throws Exception
    {
        TestMessageProperties properties = new TestMessageProperties();
        properties.setReplyTo("reply-to");
        TestRequestMessage message = new TestRequestMessage();
        message.setMessageProperties(properties);
        ValidationResult result = new ValidationResult();

        validator.validateMessage(message, result);

        assertTrue(!result.isValid());
    }

    @Test
    public void testValidateMessageNoReplyTo() throws Exception
    {
        TestMessageProperties properties = new TestMessageProperties();
        properties.setCorrelationId("correlationId-1");
        TestRequestMessage message = new TestRequestMessage();
        message.setMessageProperties(properties);
        ValidationResult result = new ValidationResult();

        validator.validateMessage(message, result);

        assertTrue(!result.isValid());
    }

    @Test
    public void testValidateMessageNullProperties() throws Exception
    {
        TestRequestMessage message = new TestRequestMessage();
        message.setMessageProperties(null);
        ValidationResult result = new ValidationResult();

        validator.validateMessage(message, result);

        assertTrue(!result.isValid());
    }

}
