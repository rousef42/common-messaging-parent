/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.validators;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.dell.cpsd.common.rabbitmq.TestMessageProperties;
import com.dell.cpsd.common.rabbitmq.TestReplyMessage;

/**
 * Unit tests for ResponseMessageValidator.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 2.3.0
 */
public class ResponseMessageValidatorTest
{
    ResponseMessageValidator<TestReplyMessage> validator;

    @Before
    public void setUp() throws Exception
    {
        validator = new ResponseMessageValidator<TestReplyMessage>();
    }

    @Test
    public void testValidateMessage() throws Exception
    {
        TestMessageProperties properties = new TestMessageProperties();
        properties.setCorrelationId("correlationId-1");
        properties.setReplyTo("reply-to");
        TestReplyMessage message = new TestReplyMessage();
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
        TestReplyMessage message = new TestReplyMessage();
        message.setMessageProperties(properties);
        ValidationResult result = new ValidationResult();

        validator.validateMessage(message, result);

        assertTrue(!result.isValid());
    }

    @Test
    public void testValidateMessageNullProperties() throws Exception
    {
        TestReplyMessage message = new TestReplyMessage();
        message.setMessageProperties(null);
        ValidationResult result = new ValidationResult();

        validator.validateMessage(message, result);

        assertTrue(!result.isValid());
    }

    /**
     * ReplyTo is not required for a response Message
     * 
     * @throws Exception
     */
    @Test
    public void testValidateMessageNoReplyTo() throws Exception
    {
        TestMessageProperties properties = new TestMessageProperties();
        properties.setCorrelationId("correlationId-1");
        TestReplyMessage message = new TestReplyMessage();
        message.setMessageProperties(properties);
        ValidationResult result = new ValidationResult();

        validator.validateMessage(message, result);

        assertTrue(result.isValid());
    }

}
