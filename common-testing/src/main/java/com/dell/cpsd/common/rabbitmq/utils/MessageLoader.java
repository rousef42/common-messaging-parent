/*
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.fail;

/**
 * Message loading utility.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @since 1.2
 */
public class MessageLoader
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageLoader.class);

    public static final String APPLICATION_JSON = "application/json";
    public static final String TYPE_ID          = "__TypeId__";

    private MessageLoader()
    {
        //Utility class
    }

    public static Message loadFromResources(final String resourcesContentFileName, final String typeId) throws IOException
    {
        final MessageProperties properties = new MessageProperties();
        properties.setContentType(APPLICATION_JSON);
        properties.setHeader(TYPE_ID, typeId);

        LOGGER.trace("Loading: " + resourcesContentFileName);
        final URL resource = MessageLoader.class.getResource(resourcesContentFileName);
        if (resource == null)
        {
            fail("Resource not loaded: " + resourcesContentFileName);
        }
        final String originalContent = IOUtils.toString(resource);
        if (originalContent == null)
        {
            fail("Content is null for the resource: " + resourcesContentFileName);
        }

        final String body = removeAmqpToolWrapper(originalContent);

        return new Message(body.getBytes(), properties);
    }

    /**
     * Detects if the string is in AMQP tool's format. If yes, it is removed.
     * Text formatting is being lost by doing this.
     */
    public static String removeAmqpToolWrapper(final String originalContent) throws IOException
    {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode rootNode = mapper.readValue(originalContent, JsonNode.class);
        if (rootNode.has("body"))
        {
            final JsonNode bodyNode = rootNode.get("body");
            return bodyNode.toString();
        }
        else
        {
            return originalContent;
        }
    }
}
