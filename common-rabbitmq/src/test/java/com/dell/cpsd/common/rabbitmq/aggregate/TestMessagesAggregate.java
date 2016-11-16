/*
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

/**
 * Test data type for MessageAggregatorTest.
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @version TBD
 * @since TBD
 */
public class TestMessagesAggregate implements Aggregate
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
        System.out.println("Setting message1");
        this.message1 = message1;
    }

    public void setMessage2(final TestMessage2 message2)
    {
        System.out.println("Setting message2");
        this.message2 = message2;
    }
}
