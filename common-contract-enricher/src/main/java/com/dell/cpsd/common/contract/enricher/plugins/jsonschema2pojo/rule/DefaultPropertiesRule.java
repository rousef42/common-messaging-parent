/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule;

import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action.AddGenericInterface;
import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action.AddInterface;
import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action.ClassAction;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JDefinedClass;
import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.PropertiesRule;
import org.jsonschema2pojo.rules.RuleFactory;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Adds MessagePropertiesContainer interface to generated class if it has necessary properties.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultPropertiesRule extends PropertiesRule
{
    protected List<ClassAction> actions = asList(new AddInterface(asList("correlationId", "replyTo", "timestamp"),
                    "com.dell.cpsd.contract.extension.amqp.message.MessagePropertiesContainer"),
            new AddInterface(asList("code", "message"), "com.dell.cpsd.contract.extension.amqp.message.ErrorContainer"),
            new AddGenericInterface("messageProperties", "com.dell.cpsd.contract.extension.amqp.message.HasMessageProperties"),
            new AddGenericInterface("errors", "com.dell.cpsd.contract.extension.amqp.message.HasErrors").unwrapFieldType(List.class));

    public DefaultPropertiesRule(RuleFactory ruleFactory)
    {
        super(ruleFactory);
    }

    @Override
    public JDefinedClass apply(String nodeName, JsonNode node, JDefinedClass jClass, Schema schema)
    {
        jClass = super.apply(nodeName, node, jClass, schema);

        // Run actions once properties of jClass are populated
        for (ClassAction action : actions)
        {
            if (action.supports(jClass))
            {
                jClass = action.apply(jClass);
            }
        }
        return jClass;
    }
}
