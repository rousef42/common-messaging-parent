/*
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for message aggregator
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
public class MessageAggregatorTest
{
    private boolean messagesAggregated = false;

    @Test
    public void process() throws Exception
    {
        String correlation1 = "abcd1";
        SimpleMessageAggregator<TestMessagesAggregate> aggregator = new SimpleMessageAggregator<>(this::createAggregate, this::ready);
        final TestMessage1 msg1 = new TestMessage1();
        aggregator.process(correlation1, testMessagesAggregate -> testMessagesAggregate.setMessage1(msg1));

        final TestMessage2 msg2 = new TestMessage2();
        aggregator.process(correlation1, testMessagesAggregate -> testMessagesAggregate.setMessage2(msg2));
        assertTrue(messagesAggregated);
    }

    void ready(TestMessagesAggregate aggregate)
    {
        System.out.println("ready");
        messagesAggregated = true;
    }

    TestMessagesAggregate createAggregate()
    {
        System.out.println("createAggregate");
        return new TestMessagesAggregate();
    }

}