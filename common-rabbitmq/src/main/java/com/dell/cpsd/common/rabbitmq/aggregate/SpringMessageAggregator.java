/**
 * * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.aggregate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.aggregator.AggregatingMessageHandler;
import org.springframework.integration.aggregator.CorrelationStrategy;
import org.springframework.integration.aggregator.DefaultAggregatingMessageGroupProcessor;
import org.springframework.integration.aggregator.ReleaseStrategy;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageHandler;

/**
 * Spring Message Aggregator
 * <p>
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @version 1.0
 * @since TBD
 */
public class SpringMessageAggregator extends AggregatingMessageHandler
{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringMessageAggregator.class);

    //Messages are received from this channel
    DirectChannel incomingMessageChannel;

    //An aggregated message is sent out from the aggregator to the next handler via this channel.
    DirectChannel aggregatedMessageOutputChannel;

    public SpringMessageAggregator(MessageHandler outputHandler, ReleaseStrategy releaseStrategy, CorrelationStrategy correlationStrategy)
    {
        super(new DefaultAggregatingMessageGroupProcessor());

        //Set up the situation for when aggregation is complete (relies on the ComponentsAndCredentials, like it does in the SimpleMessageAggregator
        this.setReleaseStrategy(releaseStrategy);

        //How do we group messages?  anything that is x or x$anything is correlated with x
        this.setCorrelationStrategy(correlationStrategy);

        //Ignore this coz I haven't figured it out yet.
        setExpireGroupsUponCompletion(true);
        setExpireGroupsUponTimeout(true);

        //TODO: I haven't set a timeout yet because it doesn't seem to work with GroupExpressionTimeOut
        //setGroupExpressionTimeout(?)

        incomingMessageChannel = new DirectChannel();
        aggregatedMessageOutputChannel = new DirectChannel();

        //channel out to MyHandler
        setOutputChannel(aggregatedMessageOutputChannel);

        //SpringMessageAggregator listens on this channel for messages
        incomingMessageChannel.subscribe(this);

        //DiscoveryHandler subscribes to the output channel.
        aggregatedMessageOutputChannel.subscribe(outputHandler);
    }

    //Method used by handlers
    public DirectChannel getSendChannel()
    {
        return incomingMessageChannel;
    }
}