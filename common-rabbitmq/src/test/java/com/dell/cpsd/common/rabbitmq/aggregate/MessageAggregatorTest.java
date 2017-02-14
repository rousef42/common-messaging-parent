/*
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    private boolean deprecated         = false;

    @Test
    public void testSingleAggregate() throws Exception
    {
        String correlation1 = "correlation-id-1";
        SimpleMessageAggregator<TestMessagesAggregate> aggregator = new SimpleMessageAggregator<>(this::createAggregate, this::ready,
                this::failOnDeprecation);

        assertFalse(aggregator.checkIfCorrelationIdPresent(correlation1));
        TestMessage1 msg1 = new TestMessage1("msg1");
        aggregator.process(correlation1, testMessagesAggregate -> testMessagesAggregate.setMessage1(msg1));

        assertTrue(aggregator.checkIfCorrelationIdPresent(correlation1));
        TestMessage2 msg2 = new TestMessage2("msg2");
        aggregator.process(correlation1, testMessagesAggregate -> testMessagesAggregate.setMessage2(msg2));

        assertTrue(messagesAggregated);
        assertFalse(aggregator.checkIfCorrelationIdPresent(correlation1));
    }

    @Test
    public void testTwoAggregates() throws Exception
    {
        String correlation1 = "correlation-id-1";
        String correlation2 = "correlation-id-2";
        SimpleMessageAggregator<TestMessagesAggregate> aggregator = new SimpleMessageAggregator<>(this::createAggregate, this::ready,
                this::failOnDeprecation);

        TestMessage1 c1msg1 = new TestMessage1("c1msg1");
        aggregator.process(correlation1, testMessagesAggregate -> testMessagesAggregate.setMessage1(c1msg1));

        TestMessage1 c2msg1 = new TestMessage1("c2msg1");
        aggregator.process(correlation2, testMessagesAggregate -> testMessagesAggregate.setMessage1(c2msg1));

        TestMessage2 c1msg2 = new TestMessage2("c1msg2");
        aggregator.process(correlation1, testMessagesAggregate -> testMessagesAggregate.setMessage2(c1msg2));

        TestMessage2 c2msg2 = new TestMessage2("c2msg2");
        aggregator.process(correlation2, testMessagesAggregate -> testMessagesAggregate.setMessage2(c2msg2));

        assertTrue(messagesAggregated);

        assertFalse(aggregator.checkIfCorrelationIdPresent(correlation1));
        assertFalse(aggregator.checkIfCorrelationIdPresent(correlation2));
    }

    @Test
    public void testIfCorrelationPresent() throws Exception
    {
        String correlation1 = "correlation-id-1";
        SimpleMessageAggregator<TestMessagesAggregate> aggregator = new SimpleMessageAggregator<>(this::createAggregate, this::ready,
                this::failOnDeprecation);

        assertFalse(aggregator.checkIfCorrelationIdPresent(correlation1));
        aggregator.process(correlation1, testMessagesAggregate -> testMessagesAggregate.setMessage1(new TestMessage1("msg1")));
        assertFalse(aggregator.checkIfCorrelationIdPresent(null));
        assertFalse(aggregator.checkIfCorrelationIdPresent(""));
        assertFalse(aggregator.checkIfCorrelationIdPresent("abc"));
        assertTrue(aggregator.checkIfCorrelationIdPresent(correlation1));

        assertFalse(messagesAggregated);
    }

    @Test
    public void testDeprecation() throws Exception
    {
        String correlation1 = "correlation-id-1";
        String correlation2 = "correlation-id-2";
        SimpleMessageAggregator<TestMessagesAggregate> aggregator = new SimpleMessageAggregator<>(this::createAggregate, this::ready,
                this::deprecate);
        aggregator.setTimeToLiveSeconds(1);

        assertFalse(aggregator.checkIfCorrelationIdPresent(correlation1));
        TestMessage1 msg1 = new TestMessage1("msg1");
        aggregator.process(correlation1, testMessagesAggregate -> testMessagesAggregate.setMessage1(msg1));
        assertTrue(aggregator.checkIfCorrelationIdPresent(correlation1));
        assertFalse(deprecated);

        Thread.sleep(100);
        assertTrue(aggregator.checkIfCorrelationIdPresent(correlation1));
        assertFalse(deprecated);

        aggregator.process(correlation2, testMessagesAggregate -> testMessagesAggregate.setMessage1(new TestMessage1("c2msg1")));
        Thread.sleep(1500);//process() was not called after timeout, so it is not deprecated yet
        assertFalse(deprecated);
        assertTrue(aggregator.checkIfCorrelationIdPresent(correlation1));

        aggregator.process(correlation2, testMessagesAggregate -> testMessagesAggregate.setMessage2(new TestMessage2("c2msg2")));
        assertTrue(deprecated);
        assertFalse(aggregator.checkIfCorrelationIdPresent(correlation1));
    }

    private void ready(TestMessagesAggregate aggregate)
    {
        System.out.println("ready");
        messagesAggregated = true;
    }

    private TestMessagesAggregate createAggregate()
    {
        System.out.println("createAggregate");
        return new TestMessagesAggregate();
    }

    private void failOnDeprecation(TestMessagesAggregate aggregate)
    {
        fail("Aggregate should not deprecate");
    }

    private void deprecate(TestMessagesAggregate aggregate)
    {
        System.out.println("deprecated");
        deprecated = true;
    }

}
