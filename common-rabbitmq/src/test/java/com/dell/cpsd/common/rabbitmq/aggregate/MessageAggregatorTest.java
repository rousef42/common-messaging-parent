/*
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.junit.Test;
import org.springframework.integration.aggregator.DefaultAggregatingMessageGroupProcessor;
import org.springframework.messaging.support.GenericMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void testSpringMessageAggregatorMessageCount()
    {
        //Keep track of the completed groups
        final List<Boolean> completedGroupsCounter = new ArrayList<>();

        SpringMessageAggregator handler = new SpringMessageAggregator(new TestOutputHandler(2, completedGroupsCounter),
                AggregationStrategies.getReleaseStrategy(AggregationStrategies.RELEASE_STRATEGIES.MESSAGE_COUNT, 2),
                AggregationStrategies.getCorrelationStrategy(AggregationStrategies.CORRELATION_STRATEGIES.GROUP_SUBMESSAGES));

        Map<String, Object> headers = new HashMap();
        headers.put("correlation-id", "a");
        headers.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message = new GenericMessage<>(new TestMessage1("a1"), headers);

        Map<String, Object> headers2 = new HashMap();
        headers2.put("correlation-id", "b");
        headers2.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message2 = new GenericMessage<>(new TestMessage1("b1"), headers2);

        Map<String, Object> headers3 = new HashMap();
        headers3.put("correlation-id", "a$1234");
        headers3.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message3 = new GenericMessage<>(new TestMessage1("a2"), headers3);

        Map<String, Object> headers4 = new HashMap();
        headers4.put("correlation-id", "b$1234");
        headers4.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message4 = new GenericMessage<>(new TestMessage1("b2"), headers4);

        handler.getSendChannel().send(message);
        System.out.println("Sent 1");
        handler.getSendChannel().send(message2);
        System.out.println("Sent 2");
        handler.getSendChannel().send(message3);
        System.out.println("Sent 3");
        handler.getSendChannel().send(message4);
        System.out.println("Sent 4");

        assertTrue(completedGroupsCounter.size() == 2);
    }

    @Test
    public void testSpringMessageAggregatorMessageTimeoutCount()
    {
        DefaultAggregatingMessageGroupProcessor processor = new DefaultAggregatingMessageGroupProcessor();

        //Keep track of the completed groups
        final List<Boolean> completedGroupsCounter = new ArrayList<>();

        SpringMessageAggregator handler = new SpringMessageAggregator(new TestOutputHandler(4, completedGroupsCounter),
                AggregationStrategies.getReleaseStrategy(AggregationStrategies.RELEASE_STRATEGIES.TIMEOUT_COUNT, 4, 20000L),
                AggregationStrategies.getCorrelationStrategy(AggregationStrategies.CORRELATION_STRATEGIES.GROUP_SUBMESSAGES));

        Map<String, Object> headers = new HashMap();
        headers.put("correlation-id", "a");
        headers.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message = new GenericMessage<>(new TestMessage1("a1"), headers);

        Map<String, Object> headers2 = new HashMap();
        headers2.put("correlation-id", "a$1");
        headers2.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message2 = new GenericMessage<>(new TestMessage1("a2"), headers2);

        Map<String, Object> headers3 = new HashMap();
        headers3.put("correlation-id", "a$2");
        headers3.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message3 = new GenericMessage<>(new TestMessage1("a3"), headers3);

        Map<String, Object> headers4 = new HashMap();
        headers4.put("correlation-id", "a$3");
        headers4.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message4 = new GenericMessage<>(new TestMessage1("a4"), headers4);

        handler.getSendChannel().send(message);
        System.out.println("Sent 1");
        handler.getSendChannel().send(message2);
        System.out.println("Sent 2");
        handler.getSendChannel().send(message3);
        System.out.println("Sent 3");
        handler.getSendChannel().send(message4);
        System.out.println("Sent 4");

        assertTrue(completedGroupsCounter.size() == 1);
    }

    @Test
    public void testSpringMessageAggregatorTimeoutCount()
    {
        DefaultAggregatingMessageGroupProcessor processor = new DefaultAggregatingMessageGroupProcessor();
        
        final long timeoutTime=5000L;

        //Keep track of the completed groups
        final List<Boolean> completedGroupsCounter = new ArrayList<>();

        SpringMessageAggregator handler = new SpringMessageAggregator(new TestOutputHandler(4, completedGroupsCounter),
                AggregationStrategies.getReleaseStrategy(AggregationStrategies.RELEASE_STRATEGIES.TIMEOUT_COUNT, 10, timeoutTime),
                AggregationStrategies.getCorrelationStrategy(AggregationStrategies.CORRELATION_STRATEGIES.GROUP_SUBMESSAGES));

        Map<String, Object> headers = new HashMap();
        headers.put("correlation-id", "a");
        headers.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message = new GenericMessage<>(new TestMessage1("a1"), headers);

        Map<String, Object> headers2 = new HashMap();
        headers2.put("correlation-id", "a$1");
        headers2.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message2 = new GenericMessage<>(new TestMessage1("a2"), headers2);

        Map<String, Object> headers3 = new HashMap();
        headers3.put("correlation-id", "a$2");
        headers3.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message3 = new GenericMessage<>(new TestMessage1("a3"), headers3);

        Map<String, Object> headers4 = new HashMap();
        headers4.put("correlation-id", "a$3");
        headers4.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message4 = new GenericMessage<>(new TestMessage1("a4"), headers4);

        handler.getSendChannel().send(message);
        System.out.println("Sent 1");
        handler.getSendChannel().send(message2);
        System.out.println("Sent 2");
        handler.getSendChannel().send(message3);
        System.out.println("Sent 3");
        handler.getSendChannel().send(message4);
        System.out.println("Sent 4");

        //Wait for the expiry
        try
        {
            Thread.sleep(timeoutTime);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        
        //This message triggers the release based on timeout.
        Map<String, Object> headers5 = new HashMap();
        headers5.put("correlation-id", "a$4");
        headers5.put("output-channel", "mychannel");
        GenericMessage<TestMessage1> message5 = new GenericMessage<>(new TestMessage1("a5"), headers4);
        handler.getSendChannel().send(message);
        System.out.println("Sent 5");

        assertTrue(completedGroupsCounter.size() == 1);
    }
}