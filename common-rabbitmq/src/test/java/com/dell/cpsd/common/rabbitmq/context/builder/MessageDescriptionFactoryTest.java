/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context.builder;

import com.dell.cpsd.common.rabbitmq.annotation.MessageContentType;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageStereotype;
import com.dell.cpsd.common.rabbitmq.context.ApplicationConfiguration;
import com.dell.cpsd.common.rabbitmq.context.MessageDescription;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since 1.0
 */
public class MessageDescriptionFactoryTest
{
    @Test
    public void testCreate()
    {
        MessageDescriptionFactory factory = new MessageDescriptionFactory(new ApplicationConfiguration(null, null, null),
                Arrays.asList(new MessageMetaData("test.message.request", "exchange.test", "TOPIC", "routing.base")));
        MessageDescription description = factory.createDescription(TestRequestMessage.class);

        Assert.assertEquals("test.message.request", description.getType());
        Assert.assertEquals(TestRequestMessage.class, description.getMessageClass());

        Assert.assertEquals("exchange.test", description.getExchange());
        Assert.assertEquals(MessageExchangeType.TOPIC, description.getExchangeType());

        Assert.assertEquals("routing.base", description.getRoutingKey());
        Assert.assertEquals(MessageStereotype.REQUEST, description.getStereotype());
        Assert.assertEquals(MessageContentType.CLEAR, description.getContentType());
    }

    @Test
    public void testFlavour()
    {
        MessageDescriptionFactory factory = new MessageDescriptionFactory(
                new ApplicationConfiguration("appName", "appUuid", "appHostName"),
                Arrays.asList(new MessageMetaData("test.message.request", "exchange.test.{providerId}.something", "TOPIC", "routing.base")));
        MessageDescription description = factory.createDescription(TestRequestMessage.class);
        Assert.assertEquals("exchange.test.appName.something", description.getExchange());
    }
}
