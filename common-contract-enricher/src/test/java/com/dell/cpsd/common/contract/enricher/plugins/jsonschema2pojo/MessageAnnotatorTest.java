/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMod;

/**
 * MessageAnnotator Test.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 * </p>
 */
public class MessageAnnotatorTest
{

    MessageAnnotator messageAnnotator;
    
    
    JDefinedClass jDefinedClass ;
    
    
    @Before
    public void setUp() throws JClassAlreadyExistsException{
        JCodeModel codeModel = new JCodeModel();
        jDefinedClass = codeModel._class("com.dell.cpsd.common.rabbitmq.message.MessagePropertiesContainer");
        messageAnnotator = new MessageAnnotator();
    }
      
    
    @Test
    public void testAnnotateMessage(){
        
    }
    
    @Test
    public void testPropertyInclusion() throws JsonProcessingException, IOException{
        String jsonStr = "{\"_meta\": {\"message\":\"message\",\"version\":\"version\",\"content\":\"CLEAR\",\"correlationIdProperty\":\"correlationIdProperty\",\"timestampProperty\":\"timestampProperty\","
                + "\"stereotype\":\"REPLY\"}}";
                
                /*"{\"_meta\": {\"message\":\"message\",\"version\":\"version\",\"content\":\"content\",\"correlationIdProperty\":\"correlationIdProperty\",\"timestampProperty\":\"timestampProperty\","
            \"stereotype\":\"REPLY\"}}";*/
        
        ObjectMapper mapper = new ObjectMapper();

        JsonNode schema = mapper.readTree(jsonStr);
        
        
        messageAnnotator.propertyInclusion(jDefinedClass, schema);
        
        
    }

}