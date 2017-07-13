/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */
package com.dell.cpsd.common.rabbitmq.validators;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedError;

/**
 * Unit tests for MessageValidationException.
 *
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.3.0
 */
public class MessageValidationExceptionTest
{

    @Test
    public void testGetFirstError()
    {
        String errorMsg1 = "first error";
        String errorMsg2 = "second error";
        LocalizedError error1 = new LocalizedError();
        error1.setMessage(errorMsg1);
        LocalizedError error2 = new LocalizedError();
        error2.setMessage(errorMsg2);
        ValidationResult result = new ValidationResult();
        result.addError(error1);
        result.addError(error2);

        MessageValidationException ex = new MessageValidationException(result);
        assertEquals(errorMsg1, ex.getFirstError());
    }

}
