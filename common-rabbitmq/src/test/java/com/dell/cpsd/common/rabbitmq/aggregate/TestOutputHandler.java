/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Output handler for test cases where the number of messages expected in a group is checked
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since TBD
 */
public class TestOutputHandler implements MessageHandler
{

    private final int           expectedNumberOfMessagesInGroup;
    private final List<Boolean> completedGroupsCounter;

    public TestOutputHandler(int expectedNumberOfMessagesInGroup, List<Boolean> completedGroupsCounter)
    {
        this.expectedNumberOfMessagesInGroup = expectedNumberOfMessagesInGroup;
        this.completedGroupsCounter = completedGroupsCounter;
    }

    @Override
    public void handleMessage(final Message<?> message) throws MessagingException
    {
        System.out.println("In the assert check");
        assertTrue(((Collection<?>) (message.getPayload())).size() == expectedNumberOfMessagesInGroup);
        completedGroupsCounter.add(true);
    }
}
