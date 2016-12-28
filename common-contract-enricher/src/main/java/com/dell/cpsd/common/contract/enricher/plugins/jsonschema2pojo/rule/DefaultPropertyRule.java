/**
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */
package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.PropertyRule;
import org.jsonschema2pojo.rules.RuleFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Adds interface to generated class if it has a specific property.<br/>
 * These interfaces provide generic access to generated messages.<br/>
 * Example: if class has 'reply-to' property, add to class definition implemented interface HasReplyTo.
 * <p>
 * <p>
 * Copyright &copy; 2016 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 */
public class DefaultPropertyRule extends PropertyRule
{
    protected Map<String, String> propertyToInterface = new HashMap<>();

    public DefaultPropertyRule(RuleFactory ruleFactory)
    {
        super(ruleFactory);

        propertyToInterface.put("correlationId", "com.dell.cpsd.common.rabbitmq.message.HasCorrelationId");
        propertyToInterface.put("reply-to", "com.dell.cpsd.common.rabbitmq.message.HasReplyTo");
    }

    @Override
    public JDefinedClass apply(String nodeName, JsonNode node, JDefinedClass jClass, Schema schema)
    {
        jClass = super.apply(nodeName, node, jClass, schema);

        String interfaceName = propertyToInterface.get(nodeName);
        if (interfaceName != null)
        {
            JCodeModel codeModel = jClass.owner();
            JClass jInterface = codeModel.directClass(interfaceName);
            jClass = jClass._implements(jInterface);
        }
        return jClass;
    }
}
