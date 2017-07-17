/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo;

import com.dell.cpsd.common.rabbitmq.annotation.Message;
import com.dell.cpsd.common.rabbitmq.annotation.MessageContentType;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageError;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageEvent;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageReply;
import com.dell.cpsd.common.rabbitmq.annotation.stereotypes.MessageRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import org.jsonschema2pojo.AbstractAnnotator;

/**
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since TBD
 */
public class MessageAnnotator extends AbstractAnnotator
{
    public MessageAnnotator()
    {
        super();
    }

    @Override
    public void propertyInclusion(JDefinedClass clazz, JsonNode schema)
    {
        JsonNode meta = schema.get("_meta");
        if (meta != null)
        {
            annotateMessage(clazz, meta);
            annotateStereotype(clazz, meta);
        }
    }

    private void annotateMessage(JDefinedClass clazz, JsonNode meta)
    {
        JsonNode message = meta.get("message");
        if (message != null)
        {
            JAnnotationUse messageAnnotation = null;
            messageAnnotation = clazz.annotate(Message.class);
            messageAnnotation.param("value", message.asText());

            JsonNode version = meta.get("version");
            if (version != null)
            {
                messageAnnotation.param("version", version.asText());
            }

            JsonNode content = meta.get("content");
            if (content != null)
            {
                messageAnnotation.param("content", MessageContentType.valueOf(content.asText().toUpperCase()));
            }

            JsonNode correlationIdProperty = meta.get("correlationIdProperty");
            if (correlationIdProperty != null)
            {
                messageAnnotation.param("correlationIdProperty", correlationIdProperty.asText());
            }

            JsonNode timestampProperty = meta.get("timestampProperty");
            if (timestampProperty != null)
            {
                messageAnnotation.param("timestampProperty", timestampProperty.asText());
            }
        }
    }

    private void annotateStereotype(JDefinedClass clazz, JsonNode meta)
    {
        JsonNode stereotype = meta.get("stereotype");
        if (stereotype != null)
        {
            String stereoTypeValue = stereotype.asText();
            if ("REQUEST".equalsIgnoreCase(stereoTypeValue))
            {
                JAnnotationUse annotation = clazz.annotate(MessageRequest.class);
                JsonNode replyToProperty = meta.get("replyToProperty");
                if (replyToProperty != null)
                {
                    annotation.param("replyToProperty", replyToProperty.asText());
                }
            }
            else if ("REPLY".equalsIgnoreCase(stereoTypeValue))
            {
                clazz.annotate(MessageReply.class);
            }
            else if ("EVENT".equalsIgnoreCase(stereoTypeValue))
            {
                clazz.annotate(MessageEvent.class);
            }
            else if ("ERROR".equalsIgnoreCase(stereoTypeValue))
            {
                clazz.annotate(MessageError.class);
            }
        }
    }
}
