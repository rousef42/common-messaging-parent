/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.validators;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Generic validator test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class GenericMessageValidatorTest
{
    GenericMessageValidator<Object> validator;

    @Before
    public void setUp() throws Exception
    {
        validator = new GenericMessageValidator<Object>()
        {
            @Override
            protected void validateMessage(Object message, ValidationResult result)
            {
            }
        };
    }

    @Test
    public void validate_nullMessage() throws Exception
    {
        ValidationResult result = validator.validate(null);

        assertSingleError(result);
    }

    @Test
    public void validate_unexpectedError() throws Exception
    {
        validator = new GenericMessageValidator<Object>()
        {
            @Override
            protected void validateMessage(Object message, ValidationResult result) throws Exception
            {
                throw new Exception("Test error");
            }
        };

        ValidationResult result = validator.validate(new Object());

        assertSingleError(result);
    }

    @Test
    public void validateNotNull() throws Exception
    {
        ValidationResult result;

        result = new ValidationResult();
        validator.validateNotNull(null, "testProperty", result);
        assertSingleError(result);

        result = new ValidationResult();
        validator.validateNotNull(123, "testProperty", result);
        assertValid(result);

        result = new ValidationResult();
        validator.validateNotNull("abc", "testProperty", result);
        assertValid(result);
    }

    @Test
    public void validateNotEmpty() throws Exception
    {
        ValidationResult result;

        result = new ValidationResult();
        validator.validateNotEmpty(null, "testProperty", result);
        assertSingleError(result);

        result = new ValidationResult();
        validator.validateNotEmpty("", "testProperty", result);
        assertSingleError(result);

        result = new ValidationResult();
        validator.validateNotEmpty("abc", "testProperty", result);
        assertValid(result);
    }

    private void assertSingleError(ValidationResult result)
    {
        assertFalse(result.isValid());
        assertEquals(1, result.getErrors().size());
    }

    private void assertValid(ValidationResult result)
    {
        assertTrue(result.isValid());
        assertEquals(0, result.getErrors().size());
    }
}