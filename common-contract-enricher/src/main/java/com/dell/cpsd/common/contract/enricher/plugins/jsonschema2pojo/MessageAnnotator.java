/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo;

import org.jsonschema2pojo.AbstractAnnotator;

import com.dell.cpsd.contract.extension.amqp.annotation.Message;
import com.dell.cpsd.contract.extension.amqp.annotation.MessageContentType;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.ErrorMessage;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.EventMessage;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.ReplyMessage;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.RequestMessage;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.StereotypeMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMethod;

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
    
    @Override
    public void propertyGetter(JMethod getter, String propertyName) {
        if("messageProperties".equals(propertyName.trim())){
            getter.annotate(Override.class);
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
            if (StereotypeMessage.REQUEST.toString().equalsIgnoreCase(stereoTypeValue))
            {
                JAnnotationUse annotation = clazz.annotate(RequestMessage.class);
                JsonNode replyToProperty = meta.get("replyToProperty");
                if (replyToProperty != null)
                {
                    annotation.param("replyToProperty", replyToProperty.asText());
                }
            }
            else if (StereotypeMessage.REPLY.toString().equalsIgnoreCase(stereoTypeValue))
            {
                clazz.annotate(ReplyMessage.class);
            }
            else if (StereotypeMessage.EVENT.toString().equalsIgnoreCase(stereoTypeValue))
            {
                clazz.annotate(EventMessage.class);
            }
            else if (StereotypeMessage.ERROR.toString().equalsIgnoreCase(stereoTypeValue))
            {
                clazz.annotate(ErrorMessage.class);
            }
        }
    }
}
