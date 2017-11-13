/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

/**
 * Test data type for MessageAggregatorTest.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class TestMessagesAggregate extends AbstractAggregate
{
    private TestMessage1 message1;
    private TestMessage2 message2;

    @Override
    public boolean isComplete()
    {
        boolean is = message1 != null && message2 != null;
        System.out.println("isComplete: " + is);
        return is;
    }

    public void setMessage1(final TestMessage1 message1)
    {
        System.out.println("Setting Message1: " + message1);
        this.message1 = message1;
    }

    public void setMessage2(final TestMessage2 message2)
    {
        System.out.println("Setting Message2: " + message2);
        this.message2 = message2;
    }
}
