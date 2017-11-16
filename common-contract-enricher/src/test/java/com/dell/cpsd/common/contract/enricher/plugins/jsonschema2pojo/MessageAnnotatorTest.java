/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved. 
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.contract.enricher.plugins.jsonschema2pojo;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

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
        jDefinedClass = codeModel._class("com.dell.cpsd.contract.extension.amqp.message.MessagePropertiesContainer");
        messageAnnotator = new MessageAnnotator();
    }
      
    @Test
    public void testPropertyInclusion() throws JsonProcessingException, IOException{
        String jsonStr = "{\"_meta\": {\"message\":\"message\",\"version\":\"version\",\"content\":\"CLEAR\",\"correlationIdProperty\":\"correlationIdProperty\",\"timestampProperty\":\"timestampProperty\","
                + "\"stereotype\":\"REPLY\"}}";
                
        ObjectMapper mapper = new ObjectMapper();

        JsonNode schema = mapper.readTree(jsonStr);
        
        
        messageAnnotator.propertyInclusion(jDefinedClass, schema);
    }

}