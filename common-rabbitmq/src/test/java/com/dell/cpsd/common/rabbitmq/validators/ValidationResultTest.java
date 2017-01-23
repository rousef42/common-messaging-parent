/*
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.validators;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Validation result test.
 * <p>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class ValidationResultTest
{
    @Test
    public void isValid_valid() throws Exception
    {
        ValidationResult result = new ValidationResult();

        assertTrue(result.isValid());
    }

    @Test
    public void isValid_invalid() throws Exception
    {
        ValidationResult result = new ValidationResult();
        result.addError("Test error");

        assertFalse(result.isValid());
    }
}