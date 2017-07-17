/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.log;

import com.dell.cpsd.common.rabbitmq.i18n.error.LocalizedError;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Common RabbitMQ messages test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.1
 */
public class RabbitMQMessageCodeTest
{
    public static final String PREFIX = "VAMQP";

    @Test
    public void testUniqueIntCode() throws Exception
    {
        final Set<Integer> set = new HashSet<>();
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            final boolean previous = set.add(code.getErrorCode());
            if (!previous)
            {
                fail("Int code used multiple times: " + code.getErrorCode());
            }
        }
    }

    @Test
    public void testIntIncludedInCode() throws Exception
    {
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            final int iCode = code.getErrorCode();
            final String sCode = code.getMessageCode();
            assertTrue(sCode.contains("" + iCode));
        }
    }

    @Test
    public void testUniqueErrorText() throws Exception
    {
        final Set<String> set = new HashSet<>();
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            final boolean previous = set.add(code.getErrorText());
            if (!previous)
            {
                //This doesn't have to be an error
                fail("Error text used multiple times: " + code.getErrorText());
            }
        }
    }

    @Test
    public void testUniqueMessageCode() throws Exception
    {
        final Set<String> set = new HashSet<>();
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            final boolean previous = set.add(code.getMessageCode());
            if (!previous)
            {
                fail("Message code used multiple times: " + code.getErrorCode());
            }
        }
    }

    @Test
    public void testUniqueMessageText() throws Exception
    {
        final Set<String> set = new HashSet<>();
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            final boolean previous = set.add(code.getMessageText());
            if (!previous)
            {
                //This doesn't have to be an error
                fail("Message text used multiple times: " + code.getErrorText());
            }
        }
    }

    @Test
    public void testLocalizedError() throws Exception
    {
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            final LocalizedError localizedError = code.getLocalizedError();
            assertNotNull(localizedError);
            assertEquals(localizedError.getMessageCode(), code.getMessageCode());
            assertEquals(localizedError.getMessage(), code.getErrorText());
        }
    }

    /**
     * Bundle returns code string if no entry found.
     */
    @Test
    public void testCodeHasBundle() throws Exception
    {
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            final String msgCode = code.getMessageCode().trim();
            final String msgText = code.getMessageText().trim();
            assertNotEquals(msgCode + " has no entry in bundle", msgCode, msgText);
        }
    }

    @Test
    public void testEnumSuffix() throws Exception
    {
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            final String enumSuffix = code.name().substring(code.name().length() - 1);
            final String codeSuffix = code.getMessageCode().substring(code.getMessageCode().length() - 1);
            assertEquals(enumSuffix, codeSuffix);
        }
    }

    @Test
    public void testCodePrefix() throws Exception
    {
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            assertTrue(code.getMessageCode().startsWith(PREFIX));
        }
    }

}
