/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.Iterator;

import org.jsonschema2pojo.Schema;
import org.jsonschema2pojo.rules.PropertiesRule;
import org.jsonschema2pojo.rules.RuleFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo.rule.action.AddInterface;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

/**
 * DefaultPropertiesRule Test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class DefaultPropertiesRuleTest
{

    @Mock
    Schema                schema = Mockito.mock(Schema.class);
    @Mock
    PropertiesRule        propertiesRule;

    RuleFactory           ruleFactory;
    DefaultPropertiesRule defaultPropertiesRule;
    JDefinedClass         jClassGeneric;
    JDefinedClass         jClassInterface;
    @Mock
    AddInterface          addInterface;

    @Before
    public void setUp() throws Exception
    {
        JCodeModel codeModel = new JCodeModel();

        jClassGeneric = codeModel._class("com.dell.cpsd.common.rabbitmq.message.HasMessageProperties");
        jClassInterface = codeModel._class("com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer");
        ruleFactory = new RuleFactory();
        defaultPropertiesRule = new DefaultPropertiesRule(ruleFactory);

        propertiesRule = Mockito.mock(PropertiesRule.class);
        addInterface = Mockito.mock(AddInterface.class);

    }

    @Test
    public void testApplyGeneric() throws JsonParseException, JsonMappingException, IOException
    {

        ObjectNode schemaContent = new ObjectMapper().createObjectNode();
        doReturn(jClassGeneric).when(propertiesRule).apply(anyString(), any(JsonNode.class), any(JDefinedClass.class), any(Schema.class));

        schema.setJavaTypeIfEmpty(jClassGeneric);
        jClassGeneric.field(JMod.PRIVATE, String.class, "messageProperties");
        jClassGeneric = defaultPropertiesRule.apply("nodeName", schemaContent, jClassGeneric, schema);

        Iterator<JClass> interfaces = jClassGeneric._implements();
        assertTrue(interfaces.hasNext());
        assertEquals("com.dell.cpsd.common.rabbitmq.message.HasMessageProperties<java.lang.String>", interfaces.next().fullName());
    }

    @Test
    public void testApplyInterface() throws JsonParseException, JsonMappingException, IOException
    {

        ObjectNode schemaContent = new ObjectMapper().createObjectNode();
        doReturn(jClassInterface).when(propertiesRule).apply(anyString(), any(JsonNode.class), any(JDefinedClass.class), any(Schema.class));

        schema.setJavaTypeIfEmpty(jClassInterface);
        jClassInterface.field(JMod.PRIVATE, String.class, "correlationId");
        jClassInterface.field(JMod.PRIVATE, String.class, "replyTo");
        jClassInterface.field(JMod.PRIVATE, String.class, "timestamp");

        jClassInterface = defaultPropertiesRule.apply("nodeName", schemaContent, jClassInterface, schema);

        Iterator<JClass> interfaces = jClassInterface._implements();
        assertTrue(interfaces.hasNext());
        assertEquals("com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer", interfaces.next().fullName());
    }

}