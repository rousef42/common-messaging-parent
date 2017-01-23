/*
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.i18n;

import com.dell.cpsd.common.rabbitmq.log.RabbitMQMessageCode;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * RabbitMQMessage texts bundle test.
 * <p>
 * Copyright © 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 * @since 1.1
 */
public class RabbitMQMessageBundleTest
{
    private static final RabbitMQMessageBundle bundle = new RabbitMQMessageBundle();

    @Test
    public void testUniqueKey() throws Exception
    {
        final Set<Object> set = new HashSet<>();
        for (final Object[] row : bundle.getContents())
        {
            final Object key = row[0];
            final boolean previous = set.add(key);
            if (!previous)
            {
                fail("Key used multiple times: " + key);
            }
        }
    }

    /**
     * Find messages without enum.
     */
    @Test
    public void testKeyHasEnumEntry() throws Exception
    {
        final Set<String> set = new HashSet<>(RabbitMQMessageCode.values().length);
        for (final RabbitMQMessageCode code : RabbitMQMessageCode.values())
        {
            set.add(code.getMessageCode());
        }

        for (final Object[] row : bundle.getContents())
        {
            final String key = (String) row[0];
            assertTrue("Key doesn't have enum entry: " + key, set.contains(key));
        }
    }

}
