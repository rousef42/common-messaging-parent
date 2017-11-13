/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.PropertiesRule;
import org.jsonschema2pojo.rules.RuleFactory;

import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action.AddGenericInterface;
import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action.AddInterface;
import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action.ClassAction;
import com.dell.cpsd.contract.extension.amqp.annotation.stereotypes.StereotypeMessage;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.codemodel.JDefinedClass;

/**
 * Adds MessagePropertiesContainer interface to generated class if it has necessary properties.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultPropertiesRule extends PropertiesRule
{
    protected List<ClassAction> actions = asList(
            new AddInterface(asList("code", "message"), "com.dell.cpsd.contract.extension.amqp.message.ErrorContainer"),
            new AddGenericInterface("errors", "com.dell.cpsd.contract.extension.amqp.message.HasErrors").unwrapFieldType(List.class));

    public DefaultPropertiesRule(RuleFactory ruleFactory)
    {
        super(ruleFactory);
    }

    @Override
    public JDefinedClass apply(String nodeName, JsonNode node, JDefinedClass jClass, Schema schema)
    {
        List<ClassAction> allActions = new ArrayList<ClassAction>();
        allActions.addAll(actions);
        
        JsonNode metaNode = schema.getContent().get("_meta");
        if (null != metaNode)
        {
            JsonNode steretypeoNode = metaNode.get("stereotype");
            if(null != steretypeoNode)
            {
                addActionForStereotype(steretypeoNode, allActions);
            }
         }
       
        jClass = super.apply(nodeName, node, jClass, schema);
        
        // Run actions once properties of jClass are populated
        for (ClassAction action : allActions)
        {
            if (action.supports(jClass))
            {
                jClass = action.apply(jClass);
            }
        }
        return jClass;
    }
    
    private void addActionForStereotype(JsonNode steretypeoNode, List<ClassAction> allActions)
    {
        if (null != steretypeoNode)
        {
            String stereoTypeValue = steretypeoNode.asText();
            if (StereotypeMessage.REQUEST.toString().equalsIgnoreCase(stereoTypeValue))
            {
                allActions.add(new AddInterface("messageProperties", "com.dell.cpsd.contract.extension.amqp.message.RequestMessage"));
            }
            else if (StereotypeMessage.RESPONSE.toString().equalsIgnoreCase(stereoTypeValue))
            {
                allActions.add(new AddInterface("messageProperties", "com.dell.cpsd.contract.extension.amqp.message.ResponseMessage"));
            } 
        }
    }
}
