/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since Vision 3.5.0
 */

import com.dell.cpsd.common.rabbitmq.annotation.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import org.jsonschema2pojo.AbstractAnnotator;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since Vision 3.5.0
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
            JsonNode message = meta.get("message");
            JAnnotationUse messageAnnotation = null;
            if (message != null)
            {
                messageAnnotation = clazz.annotate(Message.class);
                messageAnnotation.param("value", message.asText());
            }
            JsonNode version = meta.get("version");
            if (messageAnnotation != null && version != null)
            {
                messageAnnotation.param("version", version.asText());
            }
        }
    }
}
